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
package de.websplatter.muchor.persistence.mongo.entity;

import de.websplatter.muchor.persistence.entity.AttributeValue;
import de.websplatter.muchor.persistence.entity.MediaLink;
import de.websplatter.muchor.persistence.entity.LanguageSpecifics;
import de.websplatter.muchor.persistence.entity.ChannelSpecifics;
import de.websplatter.muchor.persistence.entity.ChannelInstanceSpecifics;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.Dependent;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@Entity(value = "article", noClassnameStored = true)
@Dependent
public class Article extends de.websplatter.muchor.persistence.entity.Article {

  @Id
  private String sku;
  private String variationKey;
  private String gtin;
  private String name;
  private String mpn;
  private String brandKey;
  private List<de.websplatter.muchor.persistence.mongo.entity.MediaLink> mediaLinks;
  private Map<String, de.websplatter.muchor.persistence.mongo.entity.AttributeValue> attributes;
  private HashMap<String, de.websplatter.muchor.persistence.mongo.entity.LanguageSpecifics> languageSpecifics;
  private HashMap<String, de.websplatter.muchor.persistence.mongo.entity.ChannelSpecifics> channelSpecifics;
  private HashMap<String, de.websplatter.muchor.persistence.mongo.entity.ChannelInstanceSpecifics> channelInstanceSpecifics;

  @Override
  public String getSku() {
    return sku;
  }

  @Override
  public void setSku(String sku) {
    this.sku = sku;
  }

  @Override
  public String getVariationKey() {
    return variationKey;
  }

  @Override
  public void setVariationKey(String variationKey) {
    this.variationKey = variationKey;
  }

  @Override
  public String getGtin() {
    return gtin;
  }

  @Override
  public void setGtin(String gtin) {
    this.gtin = gtin;
  }

  @Override
  public String getMpn() {
    return mpn;
  }

  @Override
  public void setMpn(String mpn) {
    this.mpn = mpn;
  }

  @Override
  public String getBrandKey() {
    return brandKey;
  }

  @Override
  public void setBrandKey(String brandKey) {
    this.brandKey = brandKey;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public List<MediaLink> getMediaLinks() {
    if (mediaLinks == null) {
      mediaLinks = new LinkedList<>();
    }
    return (List<MediaLink>) (List) mediaLinks;
  }

  @Override
  public Map<String, AttributeValue> getAttributes() {
    if (attributes == null) {
      attributes = new HashMap<>();
    }
    return (Map<String, AttributeValue>) (Map) attributes;
  }

  @Override
  public Map<String, LanguageSpecifics> getLanguageSpecifics() {
    if (languageSpecifics == null) {
      languageSpecifics = new HashMap<>();
    }
    return (Map<String, LanguageSpecifics>) (Map) languageSpecifics;
  }

  @Override
  public Map<String, ChannelSpecifics> getChannelSpecifics() {
    if (channelSpecifics == null) {
      channelSpecifics = new HashMap<>();
    }
    return (Map<String, ChannelSpecifics>) (Map) channelSpecifics;
  }

  @Override
  public Map<String, ChannelInstanceSpecifics> getChannelInstanceSpecifics() {
    if (channelInstanceSpecifics == null) {
      channelInstanceSpecifics = new HashMap<>();
    }
    return (Map<String, ChannelInstanceSpecifics>) (Map) channelInstanceSpecifics;
  }

}
