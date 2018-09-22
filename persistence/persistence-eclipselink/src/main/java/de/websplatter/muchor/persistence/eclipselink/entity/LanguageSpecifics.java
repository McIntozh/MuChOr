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
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.enterprise.context.Dependent;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "article_language_specifics")
@Dependent
public class LanguageSpecifics extends de.websplatter.muchor.persistence.entity.LanguageSpecifics implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;
  @Column(name = "name")
  private String name;
  @OneToMany(mappedBy = "languageSpecific", cascade = CascadeType.ALL)
  private List<de.websplatter.muchor.persistence.eclipselink.entity.LanguageMediaLink> mediaLinks;
  @OneToMany(mappedBy = "languageSpecific", cascade = CascadeType.ALL)
  @MapKey(name = "attribute")
  private Map<String, de.websplatter.muchor.persistence.eclipselink.entity.LanguageAttributeValue> attributes;

  @ManyToOne
  @JoinColumn(name = "sku", nullable = false)
  private Article article;
  @Column(name = "language")
  private String language;

  @PrePersist
  @PreUpdate
  public void prePersist() {
    Optional.ofNullable(mediaLinks).ifPresent(n -> n.forEach(e -> {
      e.setLanguageSpecific(this);
    }));
    Optional.ofNullable(attributes).ifPresent(n -> n.entrySet().forEach(e -> {
      e.getValue().setLanguageSpecific(this);
      e.getValue().setAttribute(e.getKey());
    }));
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
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
    if (attributes == null) {
      attributes = new HashMap<>();
    }
    return (Map<String, AttributeValue>) (Map) attributes;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public Article getArticle() {
    return article;
  }

  public void setArticle(Article article) {
    this.article = article;
  }

}
