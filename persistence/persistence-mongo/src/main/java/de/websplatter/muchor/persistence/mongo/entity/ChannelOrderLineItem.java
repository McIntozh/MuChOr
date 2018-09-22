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
  private String sku;
  private String channelSku;
  private int orderQuantity;
  private int confirmQuantity;
  private int shipQuantity;
  private int cancelQuantity;
  private int returnQuantity;
  private int singlePrice;
  private String name;
  private List<de.websplatter.muchor.persistence.mongo.entity.ChannelOrderCharge> charges;

  @Override
  public String getLineNo() {
    return lineNo;
  }

  @Override
  public void setLineNo(String lineNo) {
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
  public int getReturnQuantity() {
    return returnQuantity;
  }

  @Override
  public void setReturnQuantity(int returnQuantity) {
    this.returnQuantity = returnQuantity;
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
  public List<ChannelOrderCharge> getCharges() {
    if (charges == null) {
      charges = new LinkedList<>();
    }
    return (List<ChannelOrderCharge>) (List) charges;
  }
}
