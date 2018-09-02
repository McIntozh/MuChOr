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

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.enterprise.context.Dependent;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.PrePersist;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexes;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@Entity(value = "channel_attribute", noClassnameStored = true)
@Indexes({
  @Index(fields = {
    @Field("channel")
    ,@Field("key")
    ,@Field("categorySet")},
      options = @IndexOptions(unique = true)
  )
  ,
    @Index(fields = {
    @Field("categoryKey")}
  )
})
@Dependent
public class ChannelAttribute extends de.websplatter.muchor.persistence.entity.ChannelAttribute {

  @Id
  private String id;
  private String channel;
  private String key;
  private String categorySet;
  private String categoryKey;
  private String name;
  private String description;
  private boolean mandatory;
  private int sort;
  private Type type;
  private DataType dataType;
  private List<String> possibleValuesKey;
  private List<String> possibleValuesDescription;
  private String unit;
  private String overwritesAttributeKey;
  private boolean invisible;
  private Date outdated;
  private String mapping;

  @PrePersist
  private void genAndSetId() {
    id = genId(this.channel, this.categorySet, this.key);
  }

  public static String genId(String channel, String categorySet, String key) {
    return channel + "|" + categorySet + "|" + key;
  }

  public String getId() {
    if (id == null) {
      genAndSetId();
    }
    return id;
  }

  @Override
  public String getKey() {
    return key;
  }

  @Override
  public void setKey(String key) {
    this.key = key;
    genAndSetId();
  }

  @Override
  public String getChannel() {
    return channel;
  }

  @Override
  public void setChannel(String channel) {
    this.channel = channel;
    genAndSetId();
  }

  @Override
  public String getCategorySet() {
    return categorySet;
  }

  @Override
  public void setCategorySet(String categorySet) {
    this.categorySet = categorySet;
    genAndSetId();
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
