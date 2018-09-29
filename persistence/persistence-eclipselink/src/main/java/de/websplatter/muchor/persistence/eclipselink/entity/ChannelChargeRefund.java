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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@Entity
@Table(name = "channel_charge_refund")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "target", discriminatorType = DiscriminatorType.CHAR)
@DiscriminatorValue(value = "X")
@Dependent
public abstract class ChannelChargeRefund implements de.websplatter.muchor.persistence.entity.ChannelChargeRefund, Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;
  @Column(name = "amount")
  private int amount;
  @Column(name = "importTime")
  @Temporal(TemporalType.TIMESTAMP)
  private Date importTime;
  @Column(name = "exportTime")
  @Temporal(TemporalType.TIMESTAMP)
  private Date exportTime;
  @Column(name = "reason")
  private String reason;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  @Override
  public int getAmount() {
    return amount;
  }

  @Override
  public void setAmount(int amount) {
    this.amount = amount;
  }

  @Override
  public Date getImportTime() {
    return importTime;
  }

  @Override
  public void setImportTime(Date importTime) {
    this.importTime = importTime;
  }

  @Override
  public Date getExportTime() {
    return exportTime;
  }

  @Override
  public void setExportTime(Date exportTime) {
    this.exportTime = exportTime;
  }

  @Override
  public String getReason() {
    return reason;
  }

  @Override
  public void setReason(String reason) {
    this.reason = reason;
  }

}
