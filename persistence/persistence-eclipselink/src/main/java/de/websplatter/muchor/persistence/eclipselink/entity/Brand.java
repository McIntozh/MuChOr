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
import javax.enterprise.context.Dependent;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@Entity
@Table(name = "brand")
@Dependent
public class Brand extends de.websplatter.muchor.persistence.entity.Brand implements Serializable {

  @Id
  @Column(name = "id", length = 36)
  private String key;
  @Column(name = "name")
  private String name;
  @Column(name = "manufacturerKey")
  private String manufacturerKey;

  @Override
  public String getKey() {
    return key;
  }

  @Override
  public void setKey(String key) {
    this.key = key;
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
  public String getManufacturerKey() {
    return manufacturerKey;
  }

  @Override
  public void setManufacturerKey(String manufacturerKey) {
    this.manufacturerKey = manufacturerKey;
  }

}
