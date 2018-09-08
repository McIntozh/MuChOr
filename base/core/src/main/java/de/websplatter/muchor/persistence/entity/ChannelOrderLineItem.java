/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.websplatter.muchor.persistence.entity;

import java.util.List;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
public abstract class ChannelOrderLineItem {

  public abstract String getLineNo();

  public abstract void setLineNo(String lineNo);

  public abstract String getLineId();

  public abstract void setLineId(String lineId);

  public abstract String getSku();

  public abstract void setSku(String sku);

  public abstract String getChannelSku();

  public abstract void setChannelSku(String channelSku);

  public abstract int getOrderQuantity();

  public abstract void setOrderQuantity(int orderQuantity);

  public abstract int getConfirmQuantity();

  public abstract void setConfirmQuantity(int confirmQuantity);

  public abstract int getShipQuantity();

  public abstract void setShipQuantity(int shipQuantity);

  public abstract int getCancelQuantity();

  public abstract void setCancelQuantity(int cancelQuantity);

  public abstract int getReturnQuantity();

  public abstract void setReturnQuantity(int returnQuantity);

  public abstract int getSinglePrice();

  public abstract void setSinglePrice(int singlePrice);

  public abstract String getName();

  public abstract void setName(String name);

  public abstract List<ChannelOrderCharge> getCharges();

}
