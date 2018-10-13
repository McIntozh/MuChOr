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
package de.websplatter.muchor.example.ui.page;

import de.websplatter.muchor.example.ui.page.include.Navigation;
import de.websplatter.muchor.persistence.dao.ArticleDAO;
import de.websplatter.muchor.persistence.dao.AttributeDAO;
import de.websplatter.muchor.persistence.dao.BrandDAO;
import de.websplatter.muchor.persistence.entity.Attribute;
import de.websplatter.muchor.persistence.entity.AttributeValue;
import de.websplatter.muchor.persistence.entity.Brand;
import de.websplatter.muchor.persistence.entity.ChannelInstanceSpecifics;
import de.websplatter.muchor.persistence.entity.ChannelSpecifics;
import de.websplatter.muchor.persistence.entity.Dispatch;
import de.websplatter.muchor.persistence.entity.LanguageSpecifics;
import de.websplatter.muchor.persistence.entity.MediaLink;
import java.net.URI;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@RequestScoped
public class Article extends HTMLPage {

  private static final Navigation NAVI = new Navigation();

  @Inject
  private ArticleDAO articleDAO;
  @Inject
  private AttributeDAO attributeDAO;
  private final Map<String, Attribute> attributeCache = new HashMap<>();

  @Inject
  private BrandDAO brandDAO;
  private final Map<String, Brand> brandCache = new HashMap<>();

  @Override
  protected String getBody(URI requestURI) {
    String[] pathParts = requestURI.getPath().replaceFirst("^/", "").split("/");
    String sku = pathParts[1].trim();

    de.websplatter.muchor.persistence.entity.Article a = articleDAO.findBySKU(sku);
    if (a == null) {
      throw new RuntimeException("HTTP404");
    }

    StringBuilder sb = new StringBuilder();
    sb.append(NAVI.get("articles"));
    sb.append("<h1>")
        .append(a.getSku())
        .append("</h1>");

    sb.append("<table class=\"table\">");

    Brand brand = brandCache.get(a.getBrandKey());
    if (brand == null) {
      brand = brandDAO.findByKey(a.getBrandKey());
      brandCache.put(a.getBrandKey(), brand);
    }

    sb.append(keyValue("GTIN", a.getGtin()))
        .append(keyValue("MPN", a.getMpn()))
        .append(keyValue("Brand (key)", brand != null ? brand.getName() + " (" + a.getBrandKey() + ")" : "-"));
    if (a.getName() != null) {
      sb.append(keyValue("Name (default)", a.getName()));
    }
    sb.append(keyValue("Variation of", a.getVariationKey()))
        .append(keyValue("Dispatch", createDisptachTable(a.getDispatch())))
        .append(keyValue("Attributes", createAttributeTable(a.getAttributes())))
        .append(keyValue("Media", createMediaTable(a.getMediaLinks())))
        .append(keyValue("per Language", createTabHead(a.getLanguageSpecifics().keySet()).append(createTabLanguageSpecificsContent(a.getLanguageSpecifics()))))
        .append(keyValue("per Channel", createTabHead(a.getChannelSpecifics().keySet()).append(createChannelSpecificsTabContent(a.getChannelSpecifics()))))
        .append(keyValue("per Instance", createTabHead(a.getChannelInstanceSpecifics().keySet()).append(createChannelInstancsSpecificsTabContent(a.getChannelInstanceSpecifics()))))
        .append("</table>");

    return sb.toString();
  }

  private StringBuilder createTabHead(Collection<String> keys) {
    StringBuilder sb = new StringBuilder();

    sb.append("<nav>");
    sb.append("<div class=\"nav nav-tabs\" id=\"nav-tab\" role=\"tablist\">");

    AtomicBoolean first = new AtomicBoolean(true);
    keys.stream().forEachOrdered(l -> {
      sb.append("<a class=\"nav-item nav-link")
          .append(first.getAndSet(false) ? " active" : "")
          .append("\" id=\"nav-")
          .append(l)
          .append("-tab\" data-toggle=\"tab\" href=\"#nav-")
          .append(l)
          .append("\" role=\"tab\" aria-controls=\"nav-")
          .append(l).append("\" aria-selected=\"true\">")
          .append(l)
          .append("</a>");
    });

    sb.append("</div>");
    sb.append("</nav>");
    return sb;
  }

  private StringBuilder createTabLanguageSpecificsContent(Map<String, LanguageSpecifics> languageSpecifics) {
    StringBuilder sb = new StringBuilder();

    sb.append("<div class=\"tab-content\" id=\"nav-tabContent\">");
    AtomicBoolean first = new AtomicBoolean(true);
    languageSpecifics.keySet().stream().forEachOrdered(l -> {
      sb.append("<div class=\"tab-pane fade show")
          .append(first.getAndSet(false) ? " active" : "")
          .append("\" id=\"nav-")
          .append(l)
          .append("\" role=\"tabpanel\" aria-labelledby=\"nav-")
          .append(l)
          .append("-tab\">");

      LanguageSpecifics ls = languageSpecifics.get(l);
      sb.append("<table class=\"table\">");
      if (ls.getName() != null) {
        sb.append(keyValue("Name", ls.getName()));
      }
      sb.append(keyValue("Attributes", createAttributeTable(ls.getAttributes())));
      sb.append(keyValue("Media", createMediaTable(ls.getMediaLinks())));
      sb.append("</table>");

      sb.append("</div>");
    });

    sb.append("</div>");

    return sb;
  }

  private StringBuilder createChannelSpecificsTabContent(Map<String, ChannelSpecifics> channelSpecifics) {
    StringBuilder sb = new StringBuilder();

    sb.append("<div class=\"tab-content\" id=\"nav-tabContent\">");
    AtomicBoolean first = new AtomicBoolean(true);
    channelSpecifics.keySet().stream().forEachOrdered(l -> {
      sb.append("<div class=\"tab-pane fade show")
          .append(first.getAndSet(false) ? " active" : "")
          .append("\" id=\"nav-")
          .append(l)
          .append("\" role=\"tabpanel\" aria-labelledby=\"nav-")
          .append(l)
          .append("-tab\">");

      ChannelSpecifics cs = channelSpecifics.get(l);
      sb.append("<table class=\"table\">");
      sb.append("<tr>")
          .append("<th>").append("CatalogId").append("</th>")
          .append("<td>").append(cs.getCatalogId() != null ? cs.getCatalogId() : "-").append("</td>")
          .append("</tr>");
      if (cs.getName() != null) {
        sb.append(keyValue("Name", cs.getName()));
      }
      sb.append("<tr>")
          .append("<th>").append("Categories").append("</th>")
          .append("<td>TODO: ").append(cs.getCategoryAssignments().size()).append("</td>")
          .append("</tr>");
      sb.append(keyValue("Dispatch", createDisptachTable(cs.getDispatch())));
      sb.append(keyValue("Attributes", createAttributeTable(cs.getAttributes())));
      sb.append(keyValue("Media", createMediaTable(cs.getMediaLinks())));
      sb.append("</table>");

      sb.append("</div>");
    });

    sb.append("</div>");

    return sb;
  }

  private StringBuilder createChannelInstancsSpecificsTabContent(Map<String, ChannelInstanceSpecifics> channelInstanceSpecifics) {
    StringBuilder sb = new StringBuilder();

    sb.append("<div class=\"tab-content\" id=\"nav-tabContent\">");
    AtomicBoolean first = new AtomicBoolean(true);
    channelInstanceSpecifics.keySet().stream().forEachOrdered(l -> {
      sb.append("<div class=\"tab-pane fade show")
          .append(first.getAndSet(false) ? " active" : "")
          .append("\" id=\"nav-")
          .append(l)
          .append("\" role=\"tabpanel\" aria-labelledby=\"nav-")
          .append(l)
          .append("-tab\">");

      ChannelInstanceSpecifics cs = channelInstanceSpecifics.get(l);
      sb.append("<table class=\"table\">");
      if (cs.getName() != null) {
        sb.append(keyValue("Name", cs.getName()));
      }
      sb.append("<tr>")
          .append("<th>").append("Categories").append("</th>")
          .append("<td>TODO: ").append(cs.getCategoryAssignments().size()).append("</td>")
          .append("</tr>");
      sb.append(keyValue("Dispatch", createDisptachTable(cs.getDispatch())));
      sb.append(keyValue("Attributes", createAttributeTable(cs.getAttributes())));
      sb.append(keyValue("Media", createMediaTable(cs.getMediaLinks())));
      sb.append("</table>");

      sb.append("</div>");
    });

    sb.append("</div>");

    return sb;
  }

  private StringBuilder createAttributeTable(Map<String, AttributeValue> attributesByKey) {
    StringBuilder sb = new StringBuilder();

    sb.append("<table class=\"table\">");

    attributesByKey.entrySet().stream().forEach(e -> {

      Attribute attr = attributeCache.get(e.getKey());
      if (attr == null) {
        attr = attributeDAO.findByKey(e.getKey());
        attributeCache.put(e.getKey(), attr);
      }

      sb.append("<tr>");
      sb.append("<th>");
      if (attr == null) {
        sb.append(e.getKey());
      } else {
        sb.append(attr.getName()).append(" (").append(e.getKey()).append(")");
      }
      sb.append("</th>");
      sb.append("<td>").append(e.getValue().getValue()).append(e.getValue().getUnit() != null ? " " + e.getValue().getUnit() : "").append("</td>")
          .append("</tr>");
    });
    sb.append("</table>");

    return sb;
  }

  private StringBuilder createMediaTable(List<MediaLink> mediaLinks) {
    StringBuilder sb = new StringBuilder();

    sb.append("<table class=\"table\">");

    mediaLinks.stream().collect(Collectors.groupingBy(ml -> ml.getType())).entrySet().forEach(e -> {
      sb.append("<tr>")
          .append("<th>").append(e.getKey()).append("</th>").append("<td>");
      e.getValue().stream().forEachOrdered(ml -> {
        sb.append(ml.getUrl()).append("<br/>");
      });
      sb.append("</td>").append("</tr>");
    });
    sb.append("</table>");

    return sb;
  }

  private StringBuilder keyValue(String key, String value) {
    return new StringBuilder().append("<tr>")
        .append("<th>").append(key).append("</th>")
        .append("<td>").append(value).append("</td>")
        .append("</tr>");
  }

  private StringBuilder keyValue(String key, StringBuilder value) {
    return new StringBuilder().append("<tr>")
        .append("<th>").append(key).append("</th>")
        .append("<td>").append(value).append("</td>")
        .append("</tr>");
  }

  private StringBuilder createDisptachTable(Dispatch dispatch) {
    StringBuilder sb = new StringBuilder();

    if (dispatch != null) {
      NumberFormat nf = NumberFormat.getCurrencyInstance();
      nf.setCurrency(Currency.getInstance(dispatch.getCurrency()));

      sb.append("<table class=\"table\">")
          .append("<tr>").append("<th>Carrier</th>").append("<td>").append(dispatch.getCarrier()).append("</td>").append("</tr>")
          .append("<tr>").append("<th>Shipping time</th>").append("<td>").append(dispatch.getShippingTimeInDays()).append(" day(s)</td>").append("</tr>")
          .append("<tr>").append("<th>Gross price</th>").append("<td>").append(nf.format(dispatch.getGrossPrice() / 100f)).append("</td>").append("</tr>")
          .append("<tr>").append("<th>Net price</th>").append("<td>").append(nf.format(dispatch.getNetPrice() / 100f)).append("</td>").append("</tr>")
          .append("<tr>").append("<th>VAT</th>").append("<td>").append(dispatch.getVatPercentage()).append("%</td>").append("</tr>")
          .append("</table>");
    }

    return sb;
  }

}
