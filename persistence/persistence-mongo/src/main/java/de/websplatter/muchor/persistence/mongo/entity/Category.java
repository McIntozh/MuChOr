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
import javax.enterprise.context.Dependent;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexes;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@Entity(value = "category", noClassnameStored = true)
@Indexes(
    @Index(fields = {
  @Field("channel")
  ,
    @Field("categorySet")
  ,
    @Field("key")},
    options = @IndexOptions(unique = true)
    )
)
@Dependent
public class Category implements de.websplatter.muchor.persistence.entity.Category {

  @Id
  private String id;
  private String channel;
  private String categorySet;
  private String key;
  private String path;
  private Date outdated;

  public String getId() {
    if (id == null) {
      genAndSetId();
    }
    return id;
  }

  private void genAndSetId() {
    id = genId(this.channel, this.categorySet, this.key);
  }

  public static String genId(String channel, String categorySet, String key) {
    return channel + "|" + categorySet + "|" + key;
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
  public String getKey() {
    return key;
  }

  @Override
  public void setKey(String key) {
    this.key = key;
    genAndSetId();
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
