/*
 * Copyright 2018 Dennis Schwarz <McIntozh@gmx.net>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.websplatter.muchor.channel.google.job;

import com.google.api.services.content.ShoppingContent;
import com.google.api.services.content.model.Errors;
import com.google.api.services.content.model.Inventory;
import com.google.api.services.content.model.InventoryCustomBatchRequest;
import com.google.api.services.content.model.InventoryCustomBatchRequestEntry;
import com.google.api.services.content.model.InventoryCustomBatchResponse;
import de.websplatter.muchor.CommunicationArchiver;
import de.websplatter.muchor.Job;
import de.websplatter.muchor.JobMonitor;
import de.websplatter.muchor.MuChOr;
import de.websplatter.muchor.Notifier;
import de.websplatter.muchor.channel.google.GoogleChannel;
import de.websplatter.muchor.channel.google.GoogleChannel.ExportHistoryKeys;
import de.websplatter.muchor.channel.google.GoogleInventoryMapper;
import de.websplatter.muchor.channel.google.ShoppingContentCreator;
import de.websplatter.muchor.persistence.dao.ExportHistoryDAO;
import de.websplatter.muchor.persistence.dao.PriStoDelDAO;
import de.websplatter.muchor.persistence.entity.ExportHistory;
import de.websplatter.muchor.persistence.entity.PriStoDel;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@RequestScoped
public class InventoryExport extends Job {

  private static final int BATCH_SIZE = 500;
  private static final SimpleDateFormat FILE_DATE_FORMAT = new SimpleDateFormat("HHmmss");

  @Inject
  private GoogleChannel channelConfig;
  @Inject
  private MuChOr.Config config;
  @Inject
  private PriStoDelDAO priStoDelDAO;
  @Inject
  private GoogleInventoryMapper inventoryMapper;
  @Inject
  private ShoppingContentCreator shoppingContentCreator;
  @Inject
  private ExportHistoryDAO exportHistoryDAO;
  @Inject
  private JobMonitor monitor;

  @Override
  public void run() {
    verifyParameters();
    String channelInstance = getStringParameter("channelInstance");

    monitor.begin(InventoryExport.class.getSimpleName() + " - " + channelInstance);
    try {

      Queue<InventoryWrapper> inventoriesBatch = new LinkedList<>();
      ShoppingContent shoppingContent = shoppingContentCreator.createForChannelInstance(channelInstance);
      BigInteger merchantId = new BigInteger(config.get("channel." + channelConfig.getKey() + "." + channelInstance + ".merchantId").toString());

      Map<String, ExportHistory> exportHistoryBySku = exportHistoryDAO.findByChannelInstance(channelInstance)
          .stream()
          .filter(eh -> eh.getStatus().containsKey(ExportHistoryKeys.GoogleProductId.name()))
          .collect(Collectors.toMap(ExportHistory::getSku, Function.identity()));

      Queue<PriStoDel> psds = new LinkedList<>(priStoDelDAO.findByChannelInstance(channelInstance));

      PriStoDel psd;
      while ((psd = psds.poll()) != null) {
        ExportHistory history = exportHistoryBySku.remove(psd.getSku());
        if (history == null) {
          continue;//We can only export inventory for uploaded products
        }

        Inventory inventory = inventoryMapper.map(psd);

        String hashCode = BigInteger.valueOf(inventory.toPrettyString().hashCode()).toString(36);

        if (hasChanged(history, hashCode)) {
          history.getStatus().put(ExportHistoryKeys.InventoryHashInUpload.name(), hashCode);
          exportHistoryDAO.update(history);
          inventoriesBatch.add(new InventoryWrapper(inventory, history));
        }

        if (inventoriesBatch.size() >= BATCH_SIZE) {
          uploadBatch(inventoriesBatch, merchantId, shoppingContent);
        }
      }
      if (!inventoriesBatch.isEmpty()) {
        uploadBatch(inventoriesBatch, merchantId, shoppingContent);
      }

      monitor.succeed();
    } catch (Exception e) {
      monitor.fail();
      Notifier.builder(Notifier.Severity.WARNING)
          .channelInstance(channelInstance)
          .job(InventoryExport.class.getSimpleName())
          .exception(e).publish();
    }

  }

  private void verifyParameters() {
    if (getParameter("channelInstance") == null) {
      throw new RuntimeException("Parameter 'channelInstance' is required");
    }
  }

  private void uploadBatch(Queue<InventoryWrapper> inventoriesToSet, BigInteger merchantId, ShoppingContent shoppingContent) throws IOException {
    long batchId = 1;
    String channelInstance = getStringParameter("channelInstance");

    Map<Long, InventoryWrapper> batchIdToInventory = new HashMap<>();

    InventoryCustomBatchRequest batchRequest = new InventoryCustomBatchRequest();
    List<InventoryCustomBatchRequestEntry> batchEntries = new LinkedList<>();
    batchRequest.setEntries(batchEntries);

    InventoryWrapper inventoryToSet;
    while ((inventoryToSet = inventoriesToSet.poll()) != null) {
      InventoryCustomBatchRequestEntry batchEntry = new InventoryCustomBatchRequestEntry();
      batchEntry.setMerchantId(merchantId);
      batchEntry.setBatchId(batchId++);
      batchEntry.setProductId(inventoryToSet.history.getStatus().get(ExportHistoryKeys.GoogleProductId.name()));
      batchEntry.setInventory(inventoryToSet.inventory);
      batchEntry.setStoreCode("online");

      batchEntries.add(batchEntry);
      batchIdToInventory.put(batchEntry.getBatchId(), inventoryToSet);
    }

    String fileIdentifier;
    synchronized (FILE_DATE_FORMAT) {
      fileIdentifier = FILE_DATE_FORMAT.format(new Date()) + ".json";
    }

    try (OutputStream out = CommunicationArchiver.builder()
        .channel(channelConfig.getKey())
        .channelInstance(channelInstance)
        .fileType("Inventory")
        .fileName("Request_" + fileIdentifier)
        .build().openStream()) {
      out.write(batchRequest.toPrettyString().getBytes());
    }

    InventoryCustomBatchResponse batchResponse = shoppingContent.inventory()
        .custombatch(batchRequest)
        .setDryRun("true".equalsIgnoreCase(config.get("channel." + channelConfig.getKey() + "." + channelInstance + ".dryRun").toString()))
        .execute();

    try (OutputStream out = CommunicationArchiver.builder()
        .channel(channelConfig.getKey())
        .channelInstance(channelInstance)
        .fileType("Inventory")
        .fileName("Response_" + fileIdentifier)
        .build().openStream()) {
      out.write(batchResponse.toPrettyString().getBytes());
    }

    batchResponse.getEntries().forEach((entry) -> {
      InventoryWrapper inventory = batchIdToInventory.remove(entry.getBatchId());
      ExportHistory history = inventory.history;

      Errors errs = entry.getErrors();
      if (errs == null) {
        history.getStatus().put(ExportHistoryKeys.InventoryHash.name(), history.getStatus().remove(ExportHistoryKeys.InventoryHashInUpload.name()));
        exportHistoryDAO.update(history);
      } else {
        String sku = history.getSku();
        if (errs.getCode() == 404) {
          //Product was not found - reset all hashes and GoogleProductId to trigger a product export
          history.getStatus().clear();
          exportHistoryDAO.update(history);
          Notifier.article(sku)
              .code("WARN_PRODUCT_NOT_FOUND")//TODO code
              .publish();
        } else {
          Notifier.article(sku)
              .code("ERROR_UNKNOWN")//TODO code
              .message(errs.getMessage())
              .publish();
        }
      }
    });
  }

  private boolean hasChanged(ExportHistory hist, String hashCode) {
    return !hashCode.equals(hist.getStatus().get(ExportHistoryKeys.InventoryHash.name()));
  }

  private static class InventoryWrapper {

    final Inventory inventory;
    final ExportHistory history;

    InventoryWrapper(Inventory inventory, ExportHistory history) {
      this.inventory = inventory;
      this.history = history;
    }

  }
}
