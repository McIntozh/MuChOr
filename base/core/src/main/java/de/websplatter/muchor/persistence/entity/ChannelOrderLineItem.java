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
public interface ChannelOrderLineItem extends Named {

  public int getLineNo();

  public void setLineNo(int lineNo);

  public String getLineId();

  public void setLineId(String lineId);

  public String getSku();

  public void setSku(String sku);

  public String getShippingType();

  public void setShippingType(String shippingType);

  public String getChannelSku();

  public void setChannelSku(String channelSku);

  public int getOrderQuantity();

  public void setOrderQuantity(int orderQuantity);

  public int getConfirmQuantity();

  public void setConfirmQuantity(int confirmQuantity);

  public int getShipQuantity();

  public void setShipQuantity(int shipQuantity);

  public int getCancelQuantity();

  public void setCancelQuantity(int cancelQuantity);

  public int getRefundQuantity();

  public void setRefundQuantity(int refundQuantity);

  public int getSinglePrice();

  public void setSinglePrice(int singlePrice);

  public List<ChannelOrderLineItemCharge> getCharges();

  public List<ChannelOrderLineItemState> getStates();

}
