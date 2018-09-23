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

import java.util.Date;
import java.util.List;
import java.util.Map;
import de.websplatter.muchor.persistence.entity.ChannelOrderParty;
import de.websplatter.muchor.persistence.entity.ChannelOrderLineItem;
import de.websplatter.muchor.persistence.entity.ChannelOrderCharge;
import java.io.Serializable;
import java.util.Optional;
import javax.enterprise.context.Dependent;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.MapKey;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@Entity
@Table(name = "channel_order",
    indexes = {
      @Index(name = "channel_order_UQ", unique = true, columnList = "channelInstance,orderId")
    })
@Dependent
@NamedQueries({
  @NamedQuery(name = "ChannelOrder.byChannelInstance", query = "SELECT co FROM ChannelOrder co WHERE co.channelInstance = :channelInstance")
  ,
  @NamedQuery(name = "ChannelOrder.byChannelInstanceAndOrderId", query = "SELECT co FROM ChannelOrder co WHERE co.channelInstance = :channelInstance AND co.orderId = :orderId")
})
public class ChannelOrder extends de.websplatter.muchor.persistence.entity.ChannelOrder implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;
  @Column(name = "channelInstance", length = 36)
  private String channelInstance;
  @Column(name = "orderId", length = 50)
  private String orderId;
  @Column(name = "orderNo")
  private String orderNo;
  @Column(name = "orderDate")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date orderDate;
  @Column(name = "importDate")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date importDate;

  @OneToMany(mappedBy = "channelOrder", cascade = CascadeType.ALL)
  @MapKey(name = "type")
  public Map<String, de.websplatter.muchor.persistence.eclipselink.entity.ChannelOrderParty> parties;
  @OneToMany(mappedBy = "channelOrder", cascade = CascadeType.ALL)
  public List<de.websplatter.muchor.persistence.eclipselink.entity.ChannelOrderLineItem> lineItems;
  @OneToMany(mappedBy = "channelOrder", cascade = CascadeType.ALL)
  public List<de.websplatter.muchor.persistence.eclipselink.entity.ChannelOrderCharge> charges;

  @PrePersist
  @PreUpdate
  public void prePersist() {
    Optional.ofNullable(parties).ifPresent(n -> n.entrySet().forEach(e -> {
      e.getValue().setChannelOrder(this);
      e.getValue().setType(e.getKey());
    }));
    Optional.ofNullable(lineItems).ifPresent(n -> n.forEach(e -> {
      e.setChannelOrder(this);
    }));
    Optional.ofNullable(charges).ifPresent(n -> n.forEach(e -> {
      e.setChannelOrder(this);
    }));
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
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
  public String getOrderId() {
    return orderId;
  }

  @Override
  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  @Override
  public String getOrderNo() {
    return orderNo;
  }

  @Override
  public void setOrderNo(String orderNo) {
    this.orderNo = orderNo;
  }

  @Override
  public Date getOrderDate() {
    return orderDate;
  }

  @Override
  public void setOrderDate(Date orderDate) {
    this.orderDate = orderDate;
  }

  @Override
  public Date getImportDate() {
    return importDate;
  }

  @Override
  public void setImportDate(Date importDate) {
    this.importDate = importDate;
  }

  @Override
  public Map<String, ChannelOrderParty> getParties() {
    return (Map<String, ChannelOrderParty>) (Map) parties;
  }

  @Override
  public List<ChannelOrderLineItem> getLineItems() {
    return (List<ChannelOrderLineItem>) (List) lineItems;
  }

  @Override
  public List<ChannelOrderCharge> getCharges() {
    return (List<ChannelOrderCharge>) (List) charges;
  }

}
