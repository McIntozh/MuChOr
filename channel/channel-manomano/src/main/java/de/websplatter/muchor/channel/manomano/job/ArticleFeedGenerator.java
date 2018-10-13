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
package de.websplatter.muchor.channel.manomano.job;

import de.websplatter.muchor.CategoryAttributeMapper;
import de.websplatter.muchor.CommunicationArchiver;
import de.websplatter.muchor.Job;
import de.websplatter.muchor.MuchorProtocol;
import de.websplatter.muchor.Notifier;
import de.websplatter.muchor.channel.manomano.ManoManoChannel;
import de.websplatter.muchor.persistence.dao.ArticleDAO;
import de.websplatter.muchor.persistence.dao.ChannelAttributeDAO;
import de.websplatter.muchor.persistence.dao.PriStoDelDAO;
import de.websplatter.muchor.persistence.entity.Article;
import de.websplatter.muchor.persistence.entity.ChannelAttribute;
import de.websplatter.muchor.persistence.entity.PriStoDel;
import de.websplatter.muchor.projection.DefaultArticleProjection;
import de.websplatter.muchor.projection.DefaultProjectedArticle;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@RequestScoped
public class ArticleFeedGenerator extends Job {

  @Inject
  private PriStoDelDAO priStoDelDAO;
  @Inject
  private ArticleDAO articleDAO;
  @Inject
  private DefaultArticleProjection projection;
  @Inject
  private ManoManoChannel channelConfig;
  @Inject
  private ChannelAttributeDAO channelAttributeDAO;
  @Inject
  private CategoryAttributeMapper catAttrMapper;

  private final NumberFormat PRICE_FORMAT;

  public ArticleFeedGenerator() {
    PRICE_FORMAT = NumberFormat.getNumberInstance(Locale.US);
    PRICE_FORMAT.setMinimumFractionDigits(0);
    PRICE_FORMAT.setMaximumFractionDigits(2);
    PRICE_FORMAT.setGroupingUsed(false);
  }

  @Override
  public void run() {
    verifyParameters();
    String channelInstance = getStringParameter("channelInstance");
    String languageCode = getStringParameter("languageCode").toLowerCase();

    try {
      Queue<PriStoDel> psds = new LinkedList<>(priStoDelDAO.findByChannelInstance(channelInstance));

      List<String[]> feedFields = AttributeImport.getFeedFields();
      feedFields.add(0, new String[]{"sku", "true"});

      StringBuilder csv = new StringBuilder();
      csv.append(
          feedFields.stream().map(l -> l[0]).collect(Collectors.joining("\"\t\"", "\"", "\""))
      ).append('\n');

      Set<String> gtins = new HashSet<>();

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

        Map<String, String> row = mapArticle(pa);
        if (row != null) {
          if (!gtins.add(row.get("ean"))) {
            Notifier.ArticleNotificationBuilder n = Notifier.article(pa.getSku())
                .channelInstance(pa.getChannelInstance());
            n.code(Notifier.ArticleNotificationBuilder.Code.ERROR_AMBIGOUS_GTIN.getCode());
            n.publish();
            continue;
          }

          csv.append(
              feedFields.stream().map(l -> Optional.ofNullable(row.get(l[0])).map(v -> v.replaceAll("\"", "\"\"")).orElse("")).collect(Collectors.joining("\"\t\"", "\"", "\""))
          ).append('\n');
        }
      }

      byte[] output = csv.toString().getBytes("UTF-8");
      csv.setLength(0);//Cleanup StringBuilder ;)

      String fileIdentifier = new SimpleDateFormat("HHmmss").format(new Date()) + ".csv";
      try (OutputStream out = CommunicationArchiver.builder()
          .channel(channelConfig.getKey())
          .channelInstance(channelInstance)
          .fileType("Catalog")
          .fileName("Catalog_" + fileIdentifier)
          .build().openStream()) {
        out.write(output);
      }

      try (MuchorProtocol prot = MuchorProtocol.get((Map<String, String>) getParameter("target"))) {
        String targetFile = getOrDefaultStringParameter("fileName", "Catalog.csv");

        String tmpFile = targetFile + ".tmp";
        prot.remove("", tmpFile);
        prot.save("", tmpFile, output);
        prot.remove("", targetFile);
        prot.rename("", tmpFile, targetFile);
      }

    } catch (Exception ex) {
      Logger.getLogger(ArticleFeedGenerator.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private void verifyParameters() {
    if (getParameter("channelInstance") == null) {
      throw new RuntimeException("Parameter 'channelInstance' is required");
    }
    if (getParameter("languageCode") == null) {
      throw new RuntimeException("Parameter 'languageCode' is required");
    }
  }

  private Map<String, String> mapArticle(DefaultProjectedArticle pa) {
    boolean rowIsValid = true;

    Map<String, String> row = new HashMap<>();
    row.put("sku", pa.getSku());

    for (ChannelAttribute ca : channelAttributeDAO.findByChannelAndCategorySet(channelConfig.getKey(), channelConfig.getCategorySets()[0])) {
      Object result = catAttrMapper.map(pa, ca);
      if (ca.isMandatory() && !validString(result)) {
        rowIsValid = false;

        Notifier.ArticleNotificationBuilder n = Notifier.article(pa.getSku())
            .channelInstance(pa.getChannelInstance());

        switch (ca.getKey()) {
          case "sku_manufacturer":
            n.code(Notifier.ArticleNotificationBuilder.Code.ERROR_MISSING_MPN.getCode());
            break;
          case "ean":
            n.code(Notifier.ArticleNotificationBuilder.Code.ERROR_INVALID_GTIN.getCode());
            break;
          case "manufacturer":
            n.code(Notifier.ArticleNotificationBuilder.Code.ERROR_MANUFACTURER_MISSING.getCode());
            break;
          case "brand":
            n.code(Notifier.ArticleNotificationBuilder.Code.ERROR_BRAND_MISSING.getCode());
            break;
          case "title":
            n.code(Notifier.ArticleNotificationBuilder.Code.ERROR_NAME_MISSING.getCode());
            break;
          case "image_1":
            n.code(Notifier.ArticleNotificationBuilder.Code.ERROR_NO_PICTURES.getCode());
            break;
          default:
            n.code(Notifier.ArticleNotificationBuilder.Code.ERROR_MANDATORY_FIELD_MISSING.getCode());
            n.details(ca.getKey());
        }
        n.publish();
      } else {
        if (result != null) {
          row.put(ca.getKey(), result.toString());
        }
      }
    }

    if (rowIsValid) {
      return row;
    }

    return null;

  }

  private boolean validString(Object str) {
    return str != null && str.toString().trim().length() > 0;
  }
}
