/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.websplatter.muchor.persistence.mongo.entity;

import java.util.LinkedList;
import java.util.List;
import javax.enterprise.context.Dependent;
import de.websplatter.muchor.persistence.entity.ChannelOrderCharge;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@Dependent
public class ChannelOrderLineItem extends de.websplatter.muchor.persistence.entity.ChannelOrderLineItem {

  private String lineNo;
  private String lineId;
  public String sku;
  public String channelSku;
  private int orderQuantity;
  private int confirmQuantity;
  private int shipQuantity;
  private int cancelQuantity;
  private int returnQuantity;
  private int singlePrice;
  private String name;
  public List<de.websplatter.muchor.persistence.mongo.entity.ChannelOrderCharge> charges;

  public String getLineNo() {
    return lineNo;
  }

  public void setLineNo(String lineNo) {
    this.lineNo = lineNo;
  }

  public String getLineId() {
    return lineId;
  }

  public void setLineId(String lineId) {
    this.lineId = lineId;
  }

  public String getSku() {
    return sku;
  }

  public void setSku(String sku) {
    this.sku = sku;
  }

  public String getChannelSku() {
    return channelSku;
  }

  public void setChannelSku(String channelSku) {
    this.channelSku = channelSku;
  }

  public int getOrderQuantity() {
    return orderQuantity;
  }

  public void setOrderQuantity(int orderQuantity) {
    this.orderQuantity = orderQuantity;
  }

  public int getConfirmQuantity() {
    return confirmQuantity;
  }

  public void setConfirmQuantity(int confirmQuantity) {
    this.confirmQuantity = confirmQuantity;
  }

  public int getShipQuantity() {
    return shipQuantity;
  }

  public void setShipQuantity(int shipQuantity) {
    this.shipQuantity = shipQuantity;
  }

  public int getCancelQuantity() {
    return cancelQuantity;
  }

  public void setCancelQuantity(int cancelQuantity) {
    this.cancelQuantity = cancelQuantity;
  }

  public int getReturnQuantity() {
    return returnQuantity;
  }

  public void setReturnQuantity(int returnQuantity) {
    this.returnQuantity = returnQuantity;
  }

  public int getSinglePrice() {
    return singlePrice;
  }

  public void setSinglePrice(int singlePrice) {
    this.singlePrice = singlePrice;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

    @Override
  public List<ChannelOrderCharge> getCharges() {
    if (charges == null) {
      charges = new LinkedList<>();
    }
    return (List<ChannelOrderCharge>) (List) charges;
  }
}
