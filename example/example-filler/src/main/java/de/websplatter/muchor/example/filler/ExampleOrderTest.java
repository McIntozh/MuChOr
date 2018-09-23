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
package de.websplatter.muchor.example.filler;

import de.websplatter.muchor.Constants;
import de.websplatter.muchor.JobMonitor;
import de.websplatter.muchor.persistence.dao.ChannelOrderDAO;
import de.websplatter.muchor.persistence.entity.ChannelOrder;
import de.websplatter.muchor.persistence.entity.ChannelOrderCharge;
import de.websplatter.muchor.persistence.entity.ChannelOrderLineItem;
import de.websplatter.muchor.persistence.entity.ChannelOrderLineItemState.Cancellation;
import de.websplatter.muchor.persistence.entity.ChannelOrderLineItemState.Confirmation;
import de.websplatter.muchor.persistence.entity.ChannelOrderLineItemState.Refund;
import de.websplatter.muchor.persistence.entity.ChannelOrderLineItemState.Shipping;
import de.websplatter.muchor.persistence.entity.ChannelOrderParty;
import java.util.Date;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@RequestScoped
public class ExampleOrderTest {

  @Inject
  private ChannelOrderDAO channelOrderDAO;
  @Inject
  private JobMonitor monitor;

  public void run() {
    monitor.begin(ExampleOrderTest.class.getSimpleName());
    try {
//Create new order
      ChannelOrder order = CDI.current().select(ChannelOrder.class).get();
      order.setChannelInstance("TEST");
      order.setImportDate(new Date());
      order.setOrderId("TestId123");
      order.setOrderNo("TestNo123");
      order.setCustomerComment("Please deliver ASAP");
      order.setPaymentTxnRef("PP-XXYYZZAABBCC");
      order.setCurrencyCode("USD");

      order.getCharges().add(createShippingCharge());

      order.getParties().put(Constants.ORDER_PARTY_INVOICE, createParty());
      order.getLineItems().add(createLineItem());
      channelOrderDAO.create(order);

//Confirm threeline item
      order = channelOrderDAO.findByChannelInstanceAndOrderId(order.getChannelInstance(), order.getOrderId());
      order.getLineItems().get(0).setConfirmQuantity(3);
      order.getLineItems().get(0).getStates().add(createConfirmation());
      channelOrderDAO.update(order);

//Cancel one line item
      order = channelOrderDAO.findByChannelInstanceAndOrderId(order.getChannelInstance(), order.getOrderId());
      order.getLineItems().get(0).setCancelQuantity(1);
      order.getLineItems().get(0).getStates().add(createCancellation());
      channelOrderDAO.update(order);

//Ship two line items
      order = channelOrderDAO.findByChannelInstanceAndOrderId(order.getChannelInstance(), order.getOrderId());
      order.getLineItems().get(0).setShipQuantity(2);
      order.getLineItems().get(0).getStates().add(createShipment());
      channelOrderDAO.update(order);

//Refund one line item
      order = channelOrderDAO.findByChannelInstanceAndOrderId(order.getChannelInstance(), order.getOrderId());
      order.getLineItems().get(0).setRefundQuantity(1);
      order.getLineItems().get(0).getStates().add(createRefund());
      channelOrderDAO.update(order);

      monitor.succeed();
    } catch (Exception e) {
      monitor.fail();
      e.printStackTrace();
    }

  }

  private ChannelOrderParty createParty() {
    ChannelOrderParty cop = CDI.current().select(ChannelOrderParty.class).get();
    cop.setAddress("Somewhere\nOver the rainbow");
    cop.setZipCode("12345");
    cop.setCity("Ogdenville");
    cop.setCountryCode("US");
    cop.setRegionCode("ND");
    return cop;
  }

  private ChannelOrderLineItem createLineItem() {
    ChannelOrderLineItem coli = CDI.current().select(ChannelOrderLineItem.class).get();
    coli.setLineId("1");
    coli.setLineNo(0);
    coli.setSku("MNRL");
    coli.setChannelSku("MNRL-0001-123");
    coli.setName("Monorail");
    coli.setOrderQuantity(3);
    coli.setSinglePrice(9000099);
    return coli;
  }

  private Confirmation createConfirmation() {
    Confirmation c = CDI.current().select(Confirmation.class).get();
    c.setImportTime(new Date());
    c.setQuantity(3);
    c.setReference("ORDER-123");
    return c;
  }

  private Cancellation createCancellation() {
    Cancellation c = CDI.current().select(Cancellation.class).get();
    c.setImportTime(new Date());
    c.setQuantity(1);
    c.setReference("ORDER-123");
    c.setReason("STOCK");
    return c;
  }

  private Shipping createShipment() {
    Shipping s = CDI.current().select(Shipping.class).get();
    s.setImportTime(new Date());
    s.setQuantity(2);
    s.setReference("DeliveryNote-456");
    s.setCarrier("SPS");
    s.setTrackingCode("SP-123456789156XXYY");
    return s;
  }

  private Refund createRefund() {
    Refund r = CDI.current().select(Refund.class).get();
    r.setImportTime(new Date());
    r.setQuantity(1);
    r.setReference("RefundNote-789");
    r.setReason("Wrong color");
    return r;
  }

  private ChannelOrderCharge createShippingCharge() {
    ChannelOrderCharge cos = CDI.current().select(ChannelOrderCharge.class).get();
    cos.setName("Shipping");
    cos.setChannelChargeKey("S");
    cos.setPrice(12000);
    cos.setType(Constants.ORDER_CHARGE_SHIPPING);
    return cos;
  }
}
