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
package de.websplatter.muchor.persistence.eclipselink.entity;

import de.websplatter.muchor.persistence.entity.AttributeValue;
import de.websplatter.muchor.persistence.entity.MediaLink;
import de.websplatter.muchor.persistence.entity.LanguageSpecifics;
import de.websplatter.muchor.persistence.entity.ChannelSpecifics;
import de.websplatter.muchor.persistence.entity.ChannelInstanceSpecifics;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.enterprise.context.Dependent;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@Entity
@Table(name = "article")
@Dependent
public class Article extends de.websplatter.muchor.persistence.entity.Article implements Serializable {

  @Id
  @Column(name = "sku")
  private String sku;
  @Column(name = "variationKey")
  private String variationKey;
  @Column(name = "gtin")
  private String gtin;
  @Column(name = "name")
  private String name;
  @Column(name = "mpn")
  private String mpn;
  @Column(name = "brandKey")
  private String brandKey;
  @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
  private List<de.websplatter.muchor.persistence.eclipselink.entity.ArticleMediaLink> mediaLinks;

  @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
  @MapKey(name = "attribute")
  private Map<String, de.websplatter.muchor.persistence.eclipselink.entity.ArticleAttributeValue> attributes;

  @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
  @MapKey(name = "language")
  private Map<String, de.websplatter.muchor.persistence.eclipselink.entity.LanguageSpecifics> languageSpecifics;
  @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
  @MapKey(name = "channel")
  private Map<String, de.websplatter.muchor.persistence.eclipselink.entity.ChannelSpecifics> channelSpecifics;
  @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
  @MapKey(name = "channelInstance")
  private Map<String, de.websplatter.muchor.persistence.eclipselink.entity.ChannelInstanceSpecifics> channelInstanceSpecifics;

  @PrePersist
  @PreUpdate
  public void prePersist() {
    Optional.ofNullable(mediaLinks).ifPresent(n -> n.forEach(e -> {
      e.setArticle(this);
    }));
    Optional.ofNullable(attributes).ifPresent(n -> n.entrySet().forEach(e -> {
      e.getValue().setArticle(this);
      e.getValue().setAttribute(e.getKey());
    }));
    Optional.ofNullable(languageSpecifics).ifPresent(n -> n.entrySet().forEach(e -> {
      e.getValue().setArticle(this);
      e.getValue().setLanguage(e.getKey());
    }));
    Optional.ofNullable(channelSpecifics).ifPresent(n -> n.entrySet().forEach(e -> {
      e.getValue().setArticle(this);
      e.getValue().setChannel(e.getKey());
    }));
    Optional.ofNullable(channelInstanceSpecifics).ifPresent(n -> n.entrySet().forEach(e -> {
      e.getValue().setArticle(this);
      e.getValue().setChannelInstance(e.getKey());
    }));
  }

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
    return (List<MediaLink>) (List) mediaLinks;
  }

  @Override
  public Map<String, AttributeValue> getAttributes() {
    return (Map<String, AttributeValue>) (Map) attributes;
  }

  @Override
  public Map<String, LanguageSpecifics> getLanguageSpecifics() {
    return (Map<String, LanguageSpecifics>) (Map) languageSpecifics;
  }

  @Override
  public Map<String, ChannelSpecifics> getChannelSpecifics() {
    return (Map<String, ChannelSpecifics>) (Map) channelSpecifics;
  }

  @Override
  public Map<String, ChannelInstanceSpecifics> getChannelInstanceSpecifics() {
    return (Map<String, ChannelInstanceSpecifics>) (Map) channelInstanceSpecifics;
  }

}
