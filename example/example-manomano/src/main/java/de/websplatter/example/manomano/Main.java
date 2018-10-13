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
package de.websplatter.example.manomano;

import de.websplatter.muchor.channel.manomano.ManoManoChannel;
import de.websplatter.muchor.persistence.dao.ChannelAttributeDAO;
import de.websplatter.muchor.persistence.entity.ChannelAttribute;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Scanner;
import java.util.logging.LogManager;
import javax.enterprise.inject.spi.CDI;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
public class Main {

  public static void main(String[] args) throws IOException {
    LogManager.getLogManager().readConfiguration(Main.class.getResourceAsStream("/logging.properties"));

    Weld weld = new Weld();
    try (WeldContainer container = weld.initialize()) {
      fillExampleAttributeMappings();
      new Scanner(System.in).nextLine();
    }
  }

  public static void fillExampleAttributeMappings() {
    ManoManoChannel channelInfo = CDI.current().select(ManoManoChannel.class).get();
    ChannelAttributeDAO channelAttributeDAO = CDI.current().select(ChannelAttributeDAO.class).get();

    List<? extends ChannelAttribute> cAttrs;
    do {
      cAttrs = channelAttributeDAO.findByChannelAndCategorySet(channelInfo.getKey(), channelInfo.getCategorySets()[0]);
      try {
        if (cAttrs.isEmpty()) {
          Thread.sleep(12000l);
        }
      } catch (InterruptedException ex) {
      }
    } while (cAttrs.isEmpty());

    for (ChannelAttribute entity : cAttrs) {
      switch (entity.getKey()) {
        case "manufacturer":
          entity.setMapping("if(article.manufacturer)\n  return article.manufacturer.name; return 'Unknown'");
          break;
        case "brand":
          entity.setMapping("if(article.brand)\n  return article.brand.name; return 'Unknown'");
          break;
        case "sku_manufacturer":
          entity.setMapping("return article.mpn;");
          break;
        case "ean":
          entity.setMapping("return article.gtin;");
          break;
        case "title":
          entity.setMapping("return article.name;");
          break;
        case "description":
          entity.setMapping("return article.name;");
          break;
        case "product_price_vat_inc":
          entity.setMapping("return java.text.NumberFormat.getNumberInstance(java.util.Locale.FRANCE).format("
              + "article.price.grossPrice/100.0"
              + ");");
          break;
        case "shipping_price_vat_inc":
          entity.setMapping("if(article.dispatch) return java.text.NumberFormat.getNumberInstance(java.util.Locale.FRANCE).format(article.dispatch.price.grossPrice/100.0); return '0,0'");
          break;
        case "quantity":
          entity.setMapping("return ''+article.stock.quantity;");
          break;
        case "merchant_category":
          entity.setMapping("return 'Some category';");
          break;
        case "product_url":
          entity.setMapping("return 'http://www.yourshop.tld/article/'+article.sku;");
          break;
        case "image_1":
          entity.setMapping("if(article.media['IMAGE'] && article.media['IMAGE'].length>0)\n  return article.media['IMAGE'][0].url;");
          break;
        case "carrier":
          entity.setMapping("if(article.dispatch) return article.dispatch.carrier; return 'Default'");
          break;
        case "shipping_time":
          entity.setMapping("return java.text.NumberFormat.getNumberInstance(java.util.Locale.FRANCE).format(article.stock.processingTimeInDays+(article.dispatch?article.dispatch.shippingTimeInDays:0));");
          break;
        case "use_grid":
          entity.setMapping("return '0';");
          break;
      }

      channelAttributeDAO.update(entity);
    }

  }
}
