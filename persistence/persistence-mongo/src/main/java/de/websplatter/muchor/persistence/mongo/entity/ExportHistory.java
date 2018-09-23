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

import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.Dependent;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.PrePersist;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@Entity(value = "export_history", noClassnameStored = true)
@Indexes(
    @Index(fields = {
  @Field("sku")
  ,@Field("channelInstance")},
    options = @IndexOptions(unique = true)
    )
)
@Dependent
public class ExportHistory implements de.websplatter.muchor.persistence.entity.ExportHistory {

  @Id
  private String id;
  private String sku;
  private String channelInstance;
  private Map<String, String> status;

  @PrePersist
  private void genAndSetId() {
    id = genId(this.sku, this.channelInstance);
  }

  public static String genId(String sku, String channelInstance) {
    return sku + "|" + channelInstance;
  }

  public String getId() {
    if (id == null) {
      genAndSetId();
    }
    return id;
  }

  @Override
  public String getSku() {
    return sku;
  }

  @Override
  public void setSku(String sku) {
    this.sku = sku;
    genAndSetId();
  }

  @Override
  public String getChannelInstance() {
    return channelInstance;
  }

  @Override
  public void setChannelInstance(String channelInstance) {
    this.channelInstance = channelInstance;
    genAndSetId();
  }

  @Override
  public Map<String, String> getState() {
    if (status == null) {
      status = new HashMap<>();
    }
    return status;
  }

}
