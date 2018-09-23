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
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@Entity
@Table(name = "media_link")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "target", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue(value = "X")
@Dependent
public class MediaLink extends de.websplatter.muchor.persistence.entity.MediaLink implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;
  @Column(name = "priority")
  private int priority;
  @Column(name = "type")
  private String type;
  @Column(name = "url")
  private String url;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  @Override
  public int getPriority() {
    return priority;
  }

  @Override
  public void setPriority(int priority) {
    this.priority = priority;
  }

  @Override
  public String getType() {
    return type;
  }

  @Override
  public void setType(String type) {
    this.type = type;
  }

  @Override
  public String getUrl() {
    return url;
  }

  @Override
  public void setUrl(String url) {
    this.url = url;
  }

}
