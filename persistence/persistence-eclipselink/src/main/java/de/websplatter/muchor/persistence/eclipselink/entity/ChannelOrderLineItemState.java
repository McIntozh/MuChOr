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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@Entity
@Table(name = "channel_order_line_item_state")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "target", discriminatorType = DiscriminatorType.CHAR)
@DiscriminatorValue(value = "X")
public abstract class ChannelOrderLineItemState implements de.websplatter.muchor.persistence.entity.ChannelOrderLineItemState, Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;
  @Column(name = "quantity")
  private int quantity;
  @Column(name = "reference")
  private String reference;
  @Column(name = "importTime")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date importTime;
  @Column(name = "exportTime")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date exportTime;

  @ManyToOne
  @JoinColumn(name = "channelOrderLineItemId")
  private ChannelOrderLineItem channelOrderLineItem;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  @Override
  public int getQuantity() {
    return quantity;
  }

  @Override
  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  @Override
  public String getReference() {
    return reference;
  }

  @Override
  public void setReference(String reference) {
    this.reference = reference;
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

  public ChannelOrderLineItem getChannelOrderLineItem() {
    return channelOrderLineItem;
  }

  public void setChannelOrderLineItem(ChannelOrderLineItem channelOrderLineItem) {
    this.channelOrderLineItem = channelOrderLineItem;
  }

}
