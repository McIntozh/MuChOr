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
public interface ChannelCharge extends Named {

  public String getChannelChargeKey();

  public void setChannelChargeKey(String channelChargeKey);

  public String getType();

  public void setType(String type);

  public int getPrice();

  public void setPrice(int price);

  public List<ChannelChargeRefund> getRefunds();

}
