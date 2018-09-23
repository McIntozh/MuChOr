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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import de.websplatter.muchor.persistence.entity.ChannelOrderParty;
import de.websplatter.muchor.persistence.entity.ChannelOrderLineItem;
import de.websplatter.muchor.persistence.entity.ChannelOrderCharge;
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
@Entity(value = "channel_order", noClassnameStored = true)
@Indexes({
  @Index(fields = {
    @Field("channelInstance")
    ,@Field("orderId")},
      options = @IndexOptions(unique = true)
  )
})
@Dependent
public class ChannelOrder implements de.websplatter.muchor.persistence.entity.ChannelOrder {

  @Id
  private String id;
  private String channelInstance;
  private String orderId;
  private String orderNo;
  private String currencyCode;
  private Date orderDate;
  private String customerComment;
  private String paymentType;
  private String paymentTxnRef;
  private Date importDate;
  private Map<String, de.websplatter.muchor.persistence.mongo.entity.ChannelOrderParty> parties;
  private List<de.websplatter.muchor.persistence.mongo.entity.ChannelOrderLineItem> lineItems;
  private List<de.websplatter.muchor.persistence.mongo.entity.ChannelCharge> charges;

  @PrePersist
  private void genAndSetId() {
    id = genId(this.channelInstance, this.orderId);
  }

  public static String genId(String channelInstance, String orderId) {
    return channelInstance + "|" + orderId;
  }

  public String getId() {
    if (id == null) {
      genAndSetId();
    }
    return id;
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
  public String getOrderId() {
    return orderId;
  }

  @Override
  public void setOrderId(String orderId) {
    this.orderId = orderId;
    genAndSetId();
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
  public String getCurrencyCode() {
    return currencyCode;
  }

  @Override
  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
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
  public String getCustomerComment() {
    return customerComment;
  }

  @Override
  public void setCustomerComment(String customerComment) {
    this.customerComment = customerComment;
  }

  @Override
  public String getPaymentType() {
    return paymentType;
  }

  @Override
  public void setPaymentType(String paymentType) {
    this.paymentType = paymentType;
  }

  @Override
  public String getPaymentTxnRef() {
    return paymentTxnRef;
  }

  @Override
  public void setPaymentTxnRef(String paymentTxnRef) {
    this.paymentTxnRef = paymentTxnRef;
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
    if (parties == null) {
      parties = new HashMap<>();
    }
    return (Map<String, ChannelOrderParty>) (Map) parties;
  }

  @Override
  public List<ChannelOrderLineItem> getLineItems() {
    if (lineItems == null) {
      lineItems = new LinkedList<>();
    }
    return (List<ChannelOrderLineItem>) (List) lineItems;
  }

  @Override
  public List<ChannelOrderCharge> getCharges() {
    if (charges == null) {
      charges = new LinkedList<>();
    }
    return (List<ChannelOrderCharge>) (List) charges;
  }

}
