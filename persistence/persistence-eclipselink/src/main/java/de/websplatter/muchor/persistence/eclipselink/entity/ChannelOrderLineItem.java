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

import java.util.List;
import javax.enterprise.context.Dependent;
import de.websplatter.muchor.persistence.entity.ChannelOrderLineItemCharge;
import de.websplatter.muchor.persistence.entity.ChannelOrderLineItemState;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@Entity
@Table(name = "channel_order_line_item")
@Dependent
public class ChannelOrderLineItem implements de.websplatter.muchor.persistence.entity.ChannelOrderLineItem, Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;
  @Column(name = "lineNo")
  private int lineNo;
  @Column(name = "lineId")
  private String lineId;
  @Column(name = "sku")
  private String sku;
  @Column(name = "channelSku")
  private String channelSku;
  @Column(name = "shippingType")
  private String shippingType;
  @Column(name = "orderQuantity")
  private int orderQuantity;
  @Column(name = "confirmQuantity")
  private int confirmQuantity;
  @Column(name = "shipQuantity")
  private int shipQuantity;
  @Column(name = "cancelQuantity")
  private int cancelQuantity;
  @Column(name = "refundQuantity")
  private int refundQuantity;
  @Column(name = "singlePrice")
  private int singlePrice;
  @Column(name = "name")
  private String name;
  @OneToMany(mappedBy = "channelOrderLineItem", cascade = CascadeType.ALL)
  private List<de.websplatter.muchor.persistence.eclipselink.entity.ChannelOrderLineItemCharge> charges;
  @OneToMany(mappedBy = "channelOrderLineItem", cascade = CascadeType.ALL)
  private List<de.websplatter.muchor.persistence.eclipselink.entity.ChannelOrderLineItemState> states;

  @ManyToOne
  @JoinColumn(name = "channelOrderId", nullable = false)
  private ChannelOrder channelOrder;

  @PrePersist
  @PreUpdate
  public void prePersist() {
    Optional.ofNullable(charges).ifPresent(n -> n.forEach(e -> {
      e.setChannelOrderLineItem(this);
    }));
    Optional.ofNullable(states).ifPresent(n -> n.forEach(e -> {
      e.setChannelOrderLineItem(this);
    }));
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  @Override
  public int getLineNo() {
    return lineNo;
  }

  @Override
  public void setLineNo(int lineNo) {
    this.lineNo = lineNo;
  }

  @Override
  public String getLineId() {
    return lineId;
  }

  @Override
  public void setLineId(String lineId) {
    this.lineId = lineId;
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
  public String getShippingType() {
    return shippingType;
  }

  @Override
  public void setShippingType(String shippingType) {
    this.shippingType = shippingType;
  }

  @Override
  public String getChannelSku() {
    return channelSku;
  }

  @Override
  public void setChannelSku(String channelSku) {
    this.channelSku = channelSku;
  }

  @Override
  public int getOrderQuantity() {
    return orderQuantity;
  }

  @Override
  public void setOrderQuantity(int orderQuantity) {
    this.orderQuantity = orderQuantity;
  }

  @Override
  public int getConfirmQuantity() {
    return confirmQuantity;
  }

  @Override
  public void setConfirmQuantity(int confirmQuantity) {
    this.confirmQuantity = confirmQuantity;
  }

  @Override
  public int getShipQuantity() {
    return shipQuantity;
  }

  @Override
  public void setShipQuantity(int shipQuantity) {
    this.shipQuantity = shipQuantity;
  }

  @Override
  public int getCancelQuantity() {
    return cancelQuantity;
  }

  @Override
  public void setCancelQuantity(int cancelQuantity) {
    this.cancelQuantity = cancelQuantity;
  }

  @Override
  public int getRefundQuantity() {
    return refundQuantity;
  }

  @Override
  public void setRefundQuantity(int refundQuantity) {
    this.refundQuantity = refundQuantity;
  }

  @Override
  public int getSinglePrice() {
    return singlePrice;
  }

  @Override
  public void setSinglePrice(int singlePrice) {
    this.singlePrice = singlePrice;
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
  public List<ChannelOrderLineItemCharge> getCharges() {
    if (charges == null) {
      charges = new LinkedList<>();
    }
    return (List<ChannelOrderLineItemCharge>) (List) charges;
  }

  @Override
  public List<ChannelOrderLineItemState> getStates() {
    if (states == null) {
      states = new LinkedList<>();
    }
    return (List<ChannelOrderLineItemState>) (List) states;
  }

  public ChannelOrder getChannelOrder() {
    return channelOrder;
  }

  public void setChannelOrder(ChannelOrder channelOrder) {
    this.channelOrder = channelOrder;
  }

}
