/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.websplatter.muchor.persistence.mongo.entity;

import javax.enterprise.context.Dependent;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@Dependent
public class ChannelOrderCharge extends de.websplatter.muchor.persistence.entity.ChannelOrderCharge {

  private String channelChargeKey;
  private String type;
  private int price;
  private String name;

  @Override
  public String getChannelChargeKey() {
    return channelChargeKey;
  }

  @Override
  public void setChannelChargeKey(String channelChargeKey) {
    this.channelChargeKey = channelChargeKey;
  }

  @Override
  public String getType() {
    return type;
  }

  @Override
  public void setType(String type) {
    this.type = type;
  }

  @Override
  public int getPrice() {
    return price;
  }

  @Override
  public void setPrice(int price) {
    this.price = price;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

}
