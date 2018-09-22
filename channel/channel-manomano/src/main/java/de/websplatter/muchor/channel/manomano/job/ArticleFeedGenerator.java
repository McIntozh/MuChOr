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
import java.text.NumberFormat;
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
          csv.append(
              feedFields.stream().map(l -> Optional.ofNullable(row.get(l[0])).map(v -> v.replaceAll("\"", "\\\"")).orElse("")).collect(Collectors.joining("\"\t\"", "\"", "\""))
          ).append('\n');
        }
      }

      //TODO save via CommunicationArchiver
      try (MuchorProtocol prot = MuchorProtocol.get((Map<String, String>) getParameter("target"))) {
        String targetFile = getOrDefaultStringParameter("fileName", "Export.csv");

        String tmpFile = targetFile + ".tmp";
        prot.remove("", tmpFile);
        prot.save("",
            tmpFile,
            csv.toString().getBytes("UTF-8"));
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
    Set<String> errors = new HashSet<>();

    Map<String, String> row = new HashMap<>();
    row.put("sku", pa.getSku());

    for (ChannelAttribute ca : channelAttributeDAO.findByChannelAndCategorySet(channelConfig.getKey(), channelConfig.getCategorySets()[0])) {
      Object result = catAttrMapper.map(pa, ca);
      if (ca.isMandatory() && !validString(result)) {
        switch (ca.getKey()) {
          case "sku_manufacturer":
            errors.add("ERROR_MISSING_MPN");//TODO Code
            break;
          case "ean":
            errors.add("ERROR_MISSING_GTIN");//TODO Code
            break;
          case "manufacturer":
            errors.add("ERROR_MISSING_MANUFACTURER");//TODO Code
            break;
          case "brand":
            errors.add("ERROR_MISSING_BRAND");//TODO Code
            break;
          case "title":
            errors.add("ERROR_NAME_MISSING");//TODO Code
            break;
          default:
            errors.add("ERROR_OTHER");//TODO Code
        }
      } else {
        if (result != null) {
          row.put(ca.getKey(), result.toString());
        }
      }
    }

    if (errors.isEmpty()) {
      return row;
    }

    errors.stream().forEach(e -> {
      Notifier.article(pa.getSku())
          .channelInstance(pa.getChannelInstance())
          .code(e)
          .publish();
    });

    return null;
  }

  private boolean validString(Object str) {
    return str != null && str.toString().trim().length() > 0;
  }
}
