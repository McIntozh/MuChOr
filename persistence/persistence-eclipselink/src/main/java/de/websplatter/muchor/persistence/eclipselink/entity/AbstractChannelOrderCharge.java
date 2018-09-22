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
package de.websplatter.muchor.persistence.eclipselink.entity;

import java.io.Serializable;
import javax.persistence.Column;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
public abstract class AbstractChannelOrderCharge extends de.websplatter.muchor.persistence.entity.ChannelOrderCharge implements Serializable {

  @Column(name = "channelChargeKey")
  private String channelChargeKey;
  @Column(name = "type")
  private String type;
  @Column(name = "price")
  private int price;
  @Column(name = "name")
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
