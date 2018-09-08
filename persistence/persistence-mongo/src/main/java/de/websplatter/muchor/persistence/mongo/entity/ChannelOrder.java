/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
public class ChannelOrder extends de.websplatter.muchor.persistence.entity.ChannelOrder {

  @Id
  public String id;
  public String channelInstance;
  public String orderId;
  public String orderNo;
  public Date orderDate;
  public Date importDate;
  public Map<String, de.websplatter.muchor.persistence.mongo.entity.ChannelOrderParty> parties;
  public List<de.websplatter.muchor.persistence.mongo.entity.ChannelOrderLineItem> lineItems;
  public List<de.websplatter.muchor.persistence.mongo.entity.ChannelOrderCharge> charges;

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
