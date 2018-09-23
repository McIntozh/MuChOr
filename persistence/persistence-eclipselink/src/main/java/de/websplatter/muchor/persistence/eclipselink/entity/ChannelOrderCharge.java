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

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@Entity
@DiscriminatorValue(value = "o")
public class ChannelOrderCharge extends ChannelCharge {

  @ManyToOne
  @JoinColumn(name = "channelOrderId")
  private ChannelOrder channelOrder;

  public ChannelOrder getChannelOrder() {
    return channelOrder;
  }

  public void setChannelOrder(ChannelOrder channelOrder) {
    this.channelOrder = channelOrder;
  }

}
