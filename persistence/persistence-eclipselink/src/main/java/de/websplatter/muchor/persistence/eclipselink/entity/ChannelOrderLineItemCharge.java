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

import java.util.LinkedList;
import java.util.List;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@Entity
@DiscriminatorValue(value = "L")
public class ChannelOrderLineItemCharge extends ChannelCharge implements de.websplatter.muchor.persistence.entity.ChannelOrderLineItemCharge {

  @ManyToOne
  @JoinColumn(name = "channelOrderLineItemId")
  private ChannelOrderLineItem channelOrderLineItem;

  @OneToMany(mappedBy = "channelOrderLineItemCharge")
  private List<de.websplatter.muchor.persistence.eclipselink.entity.ChannelOrderLineItemChargeRefund> refunds;

  public ChannelOrderLineItem getChannelOrderLineItem() {
    return channelOrderLineItem;
  }

  public void setChannelOrderLineItem(ChannelOrderLineItem channelOrderLineItem) {
    this.channelOrderLineItem = channelOrderLineItem;
  }

  @Override
  public List<de.websplatter.muchor.persistence.entity.ChannelChargeRefund> getRefunds() {
    if (refunds == null) {
      refunds = new LinkedList<>();
    }
    return (List<de.websplatter.muchor.persistence.entity.ChannelChargeRefund>) (List) refunds;
  }
}
