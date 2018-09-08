/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.websplatter.muchor.persistence.entity;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
public abstract class ChannelOrder {

  public abstract String getChannelInstance();

  public abstract void setChannelInstance(String channelInstance);

  public abstract String getOrderId();

  public abstract void setOrderId(String orderId);

  public abstract String getOrderNo();

  public abstract void setOrderNo(String orderNo);

  public abstract Date getOrderDate();

  public abstract void setOrderDate(Date orderDate);

  public abstract Date getImportDate();

  public abstract void setImportDate(Date importDate);

  public abstract Map<String, ChannelOrderParty> getParties();

  public abstract List<ChannelOrderLineItem> getLineItems();

  public abstract List<ChannelOrderCharge> getCharges();

}
