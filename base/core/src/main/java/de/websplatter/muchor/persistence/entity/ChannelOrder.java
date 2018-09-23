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
public interface ChannelOrder {

  public String getChannelInstance();

  public void setChannelInstance(String channelInstance);

  public String getOrderId();

  public void setOrderId(String orderId);

  public String getOrderNo();

  public void setOrderNo(String orderNo);

  public Date getOrderDate();

  public void setOrderDate(Date orderDate);

  public String getCurrencyCode();

  public void setCurrencyCode(String currencyCode);

  public String getCustomerComment();

  public void setCustomerComment(String customerComment);

  public String getPaymentType();

  public void setPaymentType(String paymentType);

  public String getPaymentTxnRef();

  public void setPaymentTxnRef(String paymentTxnRef);

  public Date getImportDate();

  public void setImportDate(Date importDate);

  public Map<String, ChannelOrderParty> getParties();

  public List<ChannelOrderLineItem> getLineItems();

  public List<ChannelOrderCharge> getCharges();

}
