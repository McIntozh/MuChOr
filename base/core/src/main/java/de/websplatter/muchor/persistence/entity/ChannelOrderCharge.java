/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.websplatter.muchor.persistence.entity;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
public abstract class ChannelOrderCharge implements Named {

  public abstract String getChannelChargeKey();

  public abstract void setChannelChargeKey(String channelChargeKey);

  public abstract String getType();

  public abstract void setType(String type);

  public abstract int getPrice();

  public abstract void setPrice(int price);

}
