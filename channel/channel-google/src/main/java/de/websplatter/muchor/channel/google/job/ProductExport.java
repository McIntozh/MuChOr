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
import com.google.api.services.content.model.Product;
import com.google.api.services.content.model.ProductsCustomBatchRequest;
import com.google.api.services.content.model.ProductsCustomBatchRequestEntry;
import com.google.api.services.content.model.ProductsCustomBatchResponse;
import de.websplatter.muchor.CommunicationArchiver;
import de.websplatter.muchor.Job;
import de.websplatter.muchor.JobMonitor;
import de.websplatter.muchor.MuChOr;
import de.websplatter.muchor.Notifier;
import static de.websplatter.muchor.Notifier.ArticleNotificationBuilder.Code.ERROR_ENCODING;
import static de.websplatter.muchor.Notifier.ArticleNotificationBuilder.Code.ERROR_OTHER;
import static de.websplatter.muchor.Notifier.ArticleNotificationBuilder.Code.WARNING_OTHER;
import de.websplatter.muchor.channel.google.GoogleChannel;
import de.websplatter.muchor.channel.google.GoogleChannel.ExportHistoryKeys;
import de.websplatter.muchor.channel.google.GoogleProductMapper;
import de.websplatter.muchor.channel.google.ShoppingContentCreator;
import de.websplatter.muchor.persistence.dao.ArticleDAO;
import de.websplatter.muchor.persistence.dao.ExportHistoryDAO;
import de.websplatter.muchor.persistence.dao.PriStoDelDAO;
import de.websplatter.muchor.persistence.entity.Article;
import de.websplatter.muchor.persistence.entity.ExportHistory;
import de.websplatter.muchor.persistence.entity.PriStoDel;
import de.websplatter.muchor.projection.DefaultArticleProjection;
import de.websplatter.muchor.projection.DefaultProjectedArticle;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import static java.util.Optional.ofNullable;
import java.util.Queue;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@RequestScoped
public class ProductExport extends Job {

  private static final int BATCH_SIZE = 500;
  private final SimpleDateFormat FILE_DATE_FORMAT = new SimpleDateFormat("HHmmss");

  @Inject
  private GoogleChannel channelConfig;
  @Inject
  private MuChOr.Config config;
  @Inject
  private PriStoDelDAO priStoDelDAO;
  @Inject
  private ArticleDAO articleDAO;
  @Inject
  private GoogleProductMapper productMapper;
  @Inject
  private DefaultArticleProjection projection;
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
    String languageCode = getStringParameter("languageCode").toLowerCase();
    String countryCode = getStringParameter("countryCode").toUpperCase();

    monitor.begin(ProductExport.class.getSimpleName() + " - " + channelInstance);
    try {

      Queue<ProductWrapper> productsToInsert = new LinkedList<>();
      ShoppingContent shoppingContent = shoppingContentCreator.createForChannelInstance(channelInstance);
      BigInteger merchantId = new BigInteger(config.get("channel." + channelConfig.getKey() + "." + channelInstance + ".merchantId").toString());

      Map<String, ExportHistory> exportHistoryBySku = exportHistoryDAO.findByChannelInstance(channelInstance)
          .stream()
          .collect(Collectors.toMap(ExportHistory::getSku, Function.identity()));

      Queue<PriStoDel> psds = new LinkedList<>(priStoDelDAO.findByChannelInstance(channelInstance));

      PriStoDel psd;
      while ((psd = psds.poll()) != null) {
        Article a = articleDAO.findBySKU(psd.getSku());
        if (a == null) {
          continue;
        }

        DefaultProjectedArticle pa = projection
            .forChannel(channelConfig.getKey())
            .forLanguage(languageCode)
            .forChannelInstance(getStringParameter("channelInstance"))
            .project(a, psd);

        Product gp = productMapper.map(pa);
        if (gp == null) {
          continue;
        }
        gp.setTargetCountry(countryCode);

        String hashCode = BigInteger.valueOf(gp.toPrettyString().hashCode()).toString(36);

        ExportHistory history = ofNullable(exportHistoryBySku.remove(pa.getSku()))
            .orElseGet(() -> {
              ExportHistory eh = CDI.current().select(ExportHistory.class).get();
              eh.setSku(pa.getSku());
              eh.setChannelInstance(channelInstance);
              exportHistoryDAO.create(eh);
              return eh;
            });

        if (hasChanged(history, hashCode)) {
          history.getState().put(ExportHistoryKeys.ProductHashInUpload.name(), hashCode);
          exportHistoryDAO.update(history);
          productsToInsert.add(new ProductWrapper(gp, history));
        }

        if (productsToInsert.size() >= BATCH_SIZE) {
          uploadBatchInsert(productsToInsert, merchantId, shoppingContent);
        }
      }
      if (!productsToInsert.isEmpty()) {
        uploadBatchInsert(productsToInsert, merchantId, shoppingContent);
      }

      //All updated or unchanged products have been removed from the history map, remaining ones with GoogleProductId need deletion
      List<ExportHistory> productsToDelete = new LinkedList<>();
      for (ExportHistory history : exportHistoryBySku.values().stream()
          .filter(eh -> eh.getState().containsKey(ExportHistoryKeys.GoogleProductId.name()))
          .collect(Collectors.toList())) {

        productsToDelete.add(history);
        if (productsToDelete.size() >= BATCH_SIZE) {
          uploadBatchDelete(productsToDelete, merchantId, shoppingContent);
        }
      }
      if (!productsToDelete.isEmpty()) {
        uploadBatchDelete(productsToDelete, merchantId, shoppingContent);
      }

      monitor.succeed();
    } catch (Exception e) {
      monitor.fail();
      Notifier.builder(Notifier.Severity.WARNING)
          .channelInstance(channelInstance)
          .job(ProductExport.class.getSimpleName())
          .exception(e)
          .publish();
    }

  }

  private void verifyParameters() {
    if (getParameter("languageCode") == null) {
      throw new RuntimeException("Parameter 'languageCode' is required");
    }
    if (getParameter("countryCode") == null) {
      throw new RuntimeException("Parameter 'countryCode' is required");
    }
    if (getParameter("channelInstance") == null) {
      throw new RuntimeException("Parameter 'channelInstance' is required");
    }
  }

  private void uploadBatchInsert(Queue<ProductWrapper> googlePropductsToUpdate, BigInteger merchantId, ShoppingContent shoppingContent) throws IOException {
    long batchId = 1;
    String channelInstance = getStringParameter("channelInstance");

    Map<Long, ProductWrapper> batchIdToWrapper = new HashMap<>();

    ProductsCustomBatchRequest batchRequest = new ProductsCustomBatchRequest();
    List<ProductsCustomBatchRequestEntry> batchEntries = new LinkedList<>();
    batchRequest.setEntries(batchEntries);

    ProductWrapper googlePropductToUpdate;
    while ((googlePropductToUpdate = googlePropductsToUpdate.poll()) != null) {
      ProductsCustomBatchRequestEntry batchEntry = new ProductsCustomBatchRequestEntry();
      batchEntry.setBatchId(batchId++);
      batchEntry.setMerchantId(merchantId);
      batchEntry.setMethod("insert");

      batchEntry.setProduct(googlePropductToUpdate.product);
      batchEntries.add(batchEntry);

      batchIdToWrapper.put(batchEntry.getBatchId(), googlePropductToUpdate);
    }

    String fileIdentifier = FILE_DATE_FORMAT.format(new Date()) + ".json";

    try (OutputStream out = CommunicationArchiver.builder()
        .channel(channelConfig.getKey())
        .channelInstance(channelInstance)
        .fileType("Product")
        .fileName("InsertRequest_" + fileIdentifier)
        .build().openStream()) {
      out.write(batchRequest.toPrettyString().getBytes());
    }

    ProductsCustomBatchResponse batchResponse = shoppingContent.products()
        .custombatch(batchRequest)
        .setDryRun("true".equalsIgnoreCase(config.get("channel." + channelConfig.getKey() + "." + channelInstance + ".dryRun").toString()))
        .execute();

    try (OutputStream out = CommunicationArchiver.builder()
        .channel(channelConfig.getKey())
        .channelInstance(channelInstance)
        .fileType("Product")
        .fileName("InsertResponse_" + fileIdentifier)
        .build().openStream()) {
      out.write(batchResponse.toPrettyString().getBytes());
    }

    batchResponse.getEntries().forEach((entry) -> {
      ProductWrapper wrapper = batchIdToWrapper.remove(entry.getBatchId());
      String sku = wrapper.product.getOfferId();

      ofNullable(entry.getErrors()).ifPresent(errs -> {
        errs.getErrors().stream().map((err) -> err.getMessage()).filter((msg) -> (msg != null)).forEach((msg) -> {
          Notifier.article(sku)
              .channelInstance(channelInstance)
              .code(msg.contains("Encoding problem")
                  ? ERROR_ENCODING.getCode()
                  : ERROR_OTHER.getCode()
              )
              .details(msg)
              .publish();
        });
      });

      if (entry.getProduct() != null) {
        //update History

        ExportHistory hist = wrapper.history;
        hist.getState().put(ExportHistoryKeys.ProductHash.name(), hist.getState().remove(ExportHistoryKeys.ProductHashInUpload.name()));
        hist.getState().put(ExportHistoryKeys.GoogleProductId.name(), entry.getProduct().getId());
        exportHistoryDAO.update(hist);
        if (entry.getProduct().getWarnings() != null) {
          entry.getProduct().getWarnings().forEach((w) -> {
            Notifier.article(sku)
                .channelInstance(hist.getChannelInstance())
                .code(WARNING_OTHER.getCode())
                .details(w.getMessage())
                .publish();
          });
        }
      }
    });
  }

  private void uploadBatchDelete(List<ExportHistory> historyToDelete, BigInteger merchantId, ShoppingContent shoppingContent) throws IOException {
    long batchId = 1;
    String channelInstance = getStringParameter("channelInstance");

    Map<Long, ExportHistory> batchIdToHistory = new HashMap<>();

    ProductsCustomBatchRequest batchRequest = new ProductsCustomBatchRequest();
    List<ProductsCustomBatchRequestEntry> batchEntries = new LinkedList<>();
    batchRequest.setEntries(batchEntries);

    for (ExportHistory history : historyToDelete) {
      ProductsCustomBatchRequestEntry batchEntry = new ProductsCustomBatchRequestEntry();
      batchEntry.setBatchId(batchId++);
      batchEntry.setMerchantId(merchantId);
      batchEntry.setMethod("delete");
      batchEntry.setProductId(history.getState().get(ExportHistoryKeys.GoogleProductId.name()));
      batchEntries.add(batchEntry);

      batchIdToHistory.put(batchEntry.getBatchId(), history);
    }

    String fileIdentifier = FILE_DATE_FORMAT.format(new Date()) + ".json";

    try (OutputStream out = CommunicationArchiver.builder()
        .channel(channelConfig.getKey())
        .channelInstance(channelInstance)
        .fileType("Product")
        .fileName("DeleteRequest_" + fileIdentifier)
        .build().openStream()) {
      out.write(batchRequest.toPrettyString().getBytes());
    }

    ProductsCustomBatchResponse batchResonse = shoppingContent.products().custombatch(batchRequest)
        .setDryRun("true".equalsIgnoreCase(config.get("channel." + channelConfig.getKey() + "." + channelInstance + ".dryRun").toString()))
        .execute();

    try (OutputStream out = CommunicationArchiver.builder()
        .channel(channelConfig.getKey())
        .channelInstance(channelInstance)
        .fileType("Product")
        .fileName("DeleteResponse_" + fileIdentifier)
        .build().openStream()) {
      out.write(batchResonse.toPrettyString().getBytes());
    }

    batchResonse.getEntries()
        .forEach(entry -> {
          ExportHistory product = batchIdToHistory.get(entry.getBatchId());

          if (entry.getErrors() == null || entry.getErrors().getCode().equals(404l)) {
            product.getState().remove(ExportHistoryKeys.GoogleProductId.name());
            product.getState().remove(ExportHistoryKeys.ProductHash.name());
            product.getState().remove(ExportHistoryKeys.ProductHashInUpload.name());
            exportHistoryDAO.update(product);
          } else {
            Notifier.article(product.getSku())
                .channelInstance(product.getChannelInstance())
                .code(ERROR_OTHER.getCode())
                .details(entry.getErrors().getMessage())
                .publish();
          }

        });
  }

  private boolean hasChanged(ExportHistory hist, String hashCode) {
    return !hashCode.equals(hist.getState().get(ExportHistoryKeys.ProductHash.name()));
  }

  private static class ProductWrapper {

    final Product product;
    final ExportHistory history;

    ProductWrapper(Product product, ExportHistory history) {
      this.product = product;
      this.history = history;
    }

  }
}
