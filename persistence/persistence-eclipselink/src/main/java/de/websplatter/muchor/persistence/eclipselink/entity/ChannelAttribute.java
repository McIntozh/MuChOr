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
import java.util.LinkedList;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@Entity
@Table(name = "channel_attribute",
    indexes = {
      @Index(name = "channel_attribute_UQ", unique = true, columnList = "channel,categorySet,attributeKey")
      ,
      @Index(name = "channel_attribute_categoryKey", unique = true, columnList = "categoryKey")
    })
@Dependent
@NamedQueries({
  @NamedQuery(name = "ChannelAttribute.byChannelAndCategorySetAndKey", query = "SELECT c FROM ChannelAttribute c WHERE c.channel = :channel AND c.categorySet = :categorySet AND c.key = :key")
  ,
  @NamedQuery(name = "ChannelAttribute.byChannelAndCategorySet", query = "SELECT c FROM ChannelAttribute c WHERE c.channel = :channel AND c.categorySet = :categorySet")
})
public class ChannelAttribute extends de.websplatter.muchor.persistence.entity.ChannelAttribute implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;
  @Column(name = "channel", length = 4)
  private String channel;
  @Column(name = "attributeKey", length = 36)
  private String key;
  @Column(name = "categorySet", length = 10)
  private String categorySet;
  @Column(name = "categoryKey", length = 36)
  private String categoryKey;
  @Column(name = "name")
  private String name;
  @Column(name = "description")
  private String description;
  @Column(name = "mandatory")
  private boolean mandatory;
  @Column(name = "sort")
  private int sort;
  @Column(name = "type")
  @Enumerated(EnumType.STRING)
  private Type type;
  @Column(name = "dataType")
  @Enumerated(EnumType.STRING)
  private DataType dataType;
  @ElementCollection
  @CollectionTable(name = "channel_attribute_possible_value_key")
  private List<String> possibleValuesKey;
  @CollectionTable(name = "channel_attribute_possible_value_desc")
  private List<String> possibleValuesDescription;
  @Column(name = "unit")
  private String unit;
  @Column(name = "overwritesAttributeKey")
  private String overwritesAttributeKey;
  @Column(name = "invisible")
  private boolean invisible;
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "outdated")
  private Date outdated;
  @Column(name = "mapping")
  private String mapping;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
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
  public String getCategoryKey() {
    return categoryKey;
  }

  @Override
  public void setCategoryKey(String categoryKey) {
    this.categoryKey = categoryKey;
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
  public String getDescription() {
    return description;
  }

  @Override
  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public boolean isMandatory() {
    return mandatory;
  }

  @Override
  public void setMandatory(boolean mandatory) {
    this.mandatory = mandatory;
  }

  @Override
  public int getSort() {
    return sort;
  }

  @Override
  public void setSort(int sort) {
    this.sort = sort;
  }

  @Override
  public Type getType() {
    return type;
  }

  @Override
  public void setType(Type type) {
    this.type = type;
  }

  @Override
  public DataType getDataType() {
    return dataType;
  }

  @Override
  public void setDataType(DataType dataType) {
    this.dataType = dataType;
  }

  @Override
  public List<String> getPossibleValuesKey() {
    if (possibleValuesKey == null) {
      possibleValuesKey = new LinkedList<>();
    }
    return possibleValuesKey;
  }

  @Override
  public List<String> getPossibleValuesDescription() {
    if (possibleValuesDescription == null) {
      possibleValuesDescription = new LinkedList<>();
    }
    return possibleValuesDescription;
  }

  @Override
  public String getUnit() {
    return unit;
  }

  @Override
  public void setUnit(String unit) {
    this.unit = unit;
  }

  @Override
  public String getOverwritesAttributeKey() {
    return overwritesAttributeKey;
  }

  @Override
  public void setOverwritesAttributeKey(String overwritesAttributeKey) {
    this.overwritesAttributeKey = overwritesAttributeKey;
  }

  @Override
  public boolean isInvisible() {
    return invisible;
  }

  @Override
  public void setInvisible(boolean invisible) {
    this.invisible = invisible;
  }

  @Override
  public Date getOutdated() {
    return outdated;
  }

  @Override
  public void setOutdated(Date outdated) {
    this.outdated = outdated;
  }

  @Override
  public String getMapping() {
    return mapping;
  }

  @Override
  public void setMapping(String mapping) {
    this.mapping = mapping;
  }

}
