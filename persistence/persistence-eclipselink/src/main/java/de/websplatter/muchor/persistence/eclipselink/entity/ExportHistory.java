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
import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.Dependent;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@Entity
@Table(name = "export_history",
    indexes = {
      @Index(name = "export_history_UQ", unique = true, columnList = "sku,channelInstance")
    })
@Dependent
@NamedQueries({
  @NamedQuery(name = "ExportHistory.byChannelInstance", query = "SELECT e FROM ExportHistory e WHERE e.channelInstance = :channelInstance")
  ,
  @NamedQuery(name = "ExportHistory.byChannelInstanceAndSku", query = "SELECT e FROM ExportHistory e WHERE e.channelInstance = :channelInstance AND e.sku = :sku")
})
public class ExportHistory extends de.websplatter.muchor.persistence.entity.ExportHistory implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;
  @Column(name = "sku", length = 36)
  private String sku;
  @Column(name = "channelInstance", length = 36)
  private String channelInstance;
  @ElementCollection
  @CollectionTable(name = "export_history_status", joinColumns = @JoinColumn(name = "export_history_id"))
  @MapKeyColumn(name = "status_key")
  @Column(name = "status_value")
  private Map<String, String> status;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
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
  public String getChannelInstance() {
    return channelInstance;
  }

  @Override
  public void setChannelInstance(String channelInstance) {
    this.channelInstance = channelInstance;
  }

  @Override
  public Map<String, String> getStatus() {
    if (status == null) {
      status = new HashMap<>();
    }
    return status;
  }

}
