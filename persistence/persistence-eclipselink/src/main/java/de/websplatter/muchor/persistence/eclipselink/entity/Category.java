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

import java.io.Serializable;
import java.util.Date;
import javax.enterprise.context.Dependent;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@Entity
@Table(name = "category",
    indexes = {
      @Index(name = "category_UQ", unique = true, columnList = "channel,categorySet,categoryKey")
    })
@Dependent
@NamedQueries({
  @NamedQuery(name = "Category.byChannelAndCategorySetAndKey", query = "SELECT c FROM Category c WHERE c.channel = :channel AND c.categorySet = :categorySet AND c.key = :key")
  ,
  @NamedQuery(name = "Category.byChannelAndCategorySet", query = "SELECT c FROM Category c WHERE c.channel = :channel AND c.categorySet = :categorySet")
})
public class Category implements de.websplatter.muchor.persistence.entity.Category, Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;
  @Column(name = "channel", length = 4)
  private String channel;
  @Column(name = "categorySet", length = 10)
  private String categorySet;
  @Column(name = "categoryKey", length = 36)
  private String key;
  @Column(name = "fullPath")
  private String path;
  @Column(name = "outdated")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date outdated;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  @Override
  public String getChannel() {
    return channel;
  }

  @Override
  public void setChannel(String channel) {
    this.channel = channel;
  }

  @Override
  public String getCategorySet() {
    return categorySet;
  }

  @Override
  public void setCategorySet(String categorySet) {
    this.categorySet = categorySet;
  }

  @Override
  public String getKey() {
    return key;
  }

  @Override
  public void setKey(String key) {
    this.key = key;
  }

  @Override
  public String getPath() {
    return path;
  }

  @Override
  public void setPath(String path) {
    this.path = path;
  }

  @Override
  public Date getOutdated() {
    return outdated;
  }

  @Override
  public void setOutdated(Date outdated) {
    this.outdated = outdated;
  }

}
