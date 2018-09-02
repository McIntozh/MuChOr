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

import de.websplatter.muchor.Job;
import de.websplatter.muchor.JobMonitor;
import de.websplatter.muchor.channel.google.GoogleChannel;
import de.websplatter.muchor.persistence.dao.ChannelAttributeDAO;
import de.websplatter.muchor.persistence.entity.ChannelAttribute;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@RequestScoped
public class AttributeImport extends Job {

  @Inject
  GoogleChannel channelConfig;
  @Inject
  ChannelAttributeDAO channelAttributeDAO;
  @Inject
  JobMonitor monitor;

  @Override
  public void run() {
    monitor.log("Creating channel attributes");

    createChannelAttributes().forEach((ca) -> {
      ChannelAttribute entity = channelAttributeDAO.findByChannelAndCategorySetAndKey(ca.getChannel(), ca.getCategorySet(), ca.getKey());
      if (entity == null) {
        entity = ca;
      } else {
        entity.setName(ca.getName());
        entity.setDescription(ca.getDescription());
        entity.setType(ca.getType());
        entity.setDataType(ca.getDataType());
        entity.getPossibleValuesKey().clear();
        entity.getPossibleValuesKey().addAll(ca.getPossibleValuesKey());
        entity.getPossibleValuesDescription().clear();
        entity.getPossibleValuesDescription().addAll(ca.getPossibleValuesDescription());
      }

      channelAttributeDAO.save(entity);
    });
  }

  private List<ChannelAttribute> createChannelAttributes() {
    List<ChannelAttribute> result = new LinkedList<>();

    result.add(createStringAttr(
        "link",
        "Link",
        "URL directly linking to your item's page on your website.",
        null
    ));
    result.add(createStringAttr(
        "mobileLink",
        "Mobile Link",
        "Link to a mobile-optimized version of the landing page.",
        null
    ));
    result.add(createStringAttr(
        "productType",
        "ProductType",
        "Your category of the item (formatted as in products feed specification).",
        null
    ));
    result.add(createStringAttr(
        "expirationDate",
        "Expiration Date",
        "Date on which the item should expire, as specified upon insertion, in ISO 8601 format. The actual expiration date in Google Shopping is exposed in productstatuses as googleExpirationDate and might be earlier if expirationDate is too far in the future.",
        null
    ));
    result.add(createStringAttr(
        "description",
        "Description",
        "Description of the item.",
        null
    ));
    result.add(createStringAttr(
        "unitPricingBaseMeasure.unit",
        "Denominator of the unit price",
        "The unit of the denominator.",
        null
    ));
    result.add(createIntegerAttr(
        "unitPricingBaseMeasure.value",
        "Denominator of the unit price",
        "The denominator of the unit price."
    ));
    result.add(createStringAttr(
        "unitPricingMeasure.unit",
        "The measure and dimension of an item.",
        "The unit of the measure.",
        null
    ));
    result.add(createFloatAttr(
        "unitPricingMeasure.value",
        "The measure and dimension of an item.",
        "The measure of an item."
    ));
    result.add(createStringAttr(
        "condition",
        "Condition",
        "Condition or state of the item.",
        new String[]{"new", "refurbished", "used"}
    ));
    result.add(createStringAttr(
        "energyEfficiencyClass",
        "EnergyEfficiencyClass",
        "The energy efficiency class as defined in EU directive 2010/30/EU.",
        new String[]{"A", "A+", "A++", "A+++", "B", "C", "D", "E", "F", "G"}
    ));
    result.add(createStringAttr(
        "material",
        "Material",
        "The material of which the item is made.",
        null
    ));
    result.add(createStringAttr(
        "color",
        "Color",
        "Color of the item.",
        null
    ));
    result.add(createStringAttr(
        "customLabel0",
        "Custom label 0",
        "Custom label 0 for custom grouping of items in a Shopping campaign.",
        null
    ));
    result.add(createStringAttr(
        "customLabel1",
        "Custom label 1",
        "Custom label 1 for custom grouping of items in a Shopping campaign.",
        null
    ));
    result.add(createStringAttr(
        "customLabel2",
        "Custom label 2",
        "Custom label 2 for custom grouping of items in a Shopping campaign.",
        null
    ));
    result.add(createStringAttr(
        "customLabel3",
        "Custom label 3",
        "Custom label 3 for custom grouping of items in a Shopping campaign.",
        null
    ));
    result.add(createStringAttr(
        "customLabel4",
        "Custom label 4",
        "Custom label 4 for custom grouping of items in a Shopping campaign.",
        null
    ));
    result.add(createStringAttr(
        "ageGroup",
        "Age group",
        "Target age group of the item.",
        new String[]{"adult", "infant", "kids", "newborn", "toddler"}
    ));
    result.add(createStringAttr(
        "gender",
        "Gender",
        "Target gender of the item.",
        new String[]{"female", "male", "unisex"}
    ));
    result.add(createStringAttr(
        "pattern",
        "Pattern",
        "The item's pattern (e.g. polka dots).",
        null
    ));
    result.add(createStringAttr(
        "sizeSystem",
        "Size system",
        "System in which the size is specified. Recommended for apparel items.",
        new String[]{"AU", "BR", "CN", "DE", "EU", "FR", "IT", "JP", "MEX", "UK", "US"}
    ));
    result.add(createStringAttr(
        "sizeType",
        "Size type",
        "The cut of the item. Recommended for apparel items.",
        new String[]{"maternity", "oversize", "petite", "regular"}
    ));

    return result;
  }

  private ChannelAttribute createIntegerAttr(String key, String name, String desc) {
    return createNumericAttr(key, name, desc, ChannelAttribute.DataType.INTEGER);
  }

  private ChannelAttribute createFloatAttr(String key, String name, String desc) {
    return createNumericAttr(key, name, desc, ChannelAttribute.DataType.FLOAT);
  }

  private ChannelAttribute createNumericAttr(String key, String name, String desc, ChannelAttribute.DataType dt) {
    ChannelAttribute attr = CDI.current().select(ChannelAttribute.class).get();
    attr.setKey(key);
    attr.setChannel(channelConfig.getKey());
    attr.setCategorySet(channelConfig.getCategorySets()[0]);

    attr.setName(name);
    attr.setDescription(desc);
    attr.setType(ChannelAttribute.Type.SINGLE);
    attr.setDataType(dt);

    return attr;
  }

  private ChannelAttribute createStringAttr(String key, String name, String desc, String[] possibleValues) {
    ChannelAttribute attr = CDI.current().select(ChannelAttribute.class).get();
    attr.setKey(key);
    attr.setChannel(channelConfig.getKey());
    attr.setCategorySet(channelConfig.getCategorySets()[0]);

    attr.setName(name);
    attr.setDescription(desc);
    attr.setType(ChannelAttribute.Type.SINGLE);
    attr.setDataType(ChannelAttribute.DataType.STRING);
    if (possibleValues != null) {
      List<String> valList = Arrays.asList(possibleValues);
      attr.getPossibleValuesKey().addAll(valList);
      attr.getPossibleValuesDescription().addAll(valList);
    }

    return attr;
  }

}
