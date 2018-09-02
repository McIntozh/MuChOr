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
package de.websplatter.muchor.channel.manomano.job;

import com.google.gson.Gson;
import de.websplatter.muchor.channel.manomano.api.Address;
import de.websplatter.muchor.channel.manomano.api.Order;
import de.websplatter.muchor.channel.manomano.api.OrdersResponse;
import de.websplatter.muchor.channel.manomano.api.Product;
import de.websplatter.muchor.channel.manomano.api.Relay;
import de.websplatter.muchor.Job;
import de.websplatter.muchor.JobMonitor;
import de.websplatter.muchor.Notifier;
import de.websplatter.muchor.channel.manomano.Api;
import de.websplatter.muchor.channel.manomano.api.ResponseCodes;
import de.websplatter.muchor.persistence.dao.ChannelOrderDAO;
import de.websplatter.muchor.persistence.entity.ChannelOrder;
import de.websplatter.muchor.persistence.entity.ChannelOrderLineItem;
import de.websplatter.muchor.persistence.entity.ChannelOrderParty;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.xml.bind.JAXB;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@RequestScoped
public class ImportOrders extends Job {

  @Inject
  private JobMonitor monitor;
  @Inject
  private Api api;
  @Inject
  private ChannelOrderDAO channelOrderDAO;

  @Override
  public void run() {
    verifyParameters();
    String channelInstance = getStringParameter("channelInstance");

    monitor.begin(ImportOrders.class.getSimpleName() + " - " + channelInstance);
    try {
      OrdersResponse apiResponse = api.get("http://ws.monechelle.com/?login=mon_login&password=mon_password&method=get_orders", OrdersResponse.class);
//      OrdersResponse apiResponse = JAXB.unmarshal(ImportOrders.class.getResourceAsStream("/example_orders.xml"), OrdersResponse.class);
      if (!ResponseCodes.OK.equals(apiResponse.getCode())) {
        monitor.fail();
        Notifier.builder(Notifier.Severity.WARNING).message("Got response code '" + apiResponse.getCode() + "' (" + apiResponse.getMessage() + ")").publish();
      }

      for (Order manoOrder : apiResponse.getOrderList().getOrders()) {
        ChannelOrder co = channelOrderDAO.findByChannelInstanceAndOrderId(channelInstance, manoOrder.getOrderRef());
        if (co == null) {
          co = map(manoOrder);
          channelOrderDAO.save(co);
          System.out.println(new Gson().toJson(co));
        }
      }
      monitor.succeed();
    } catch (Exception e) {
      monitor.fail();
      Notifier.builder(Notifier.Severity.WARNING).exception(e).publish();
    }
  }

  private static final SimpleDateFormat MANO_ORDER_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  private ChannelOrder map(Order manoOrder) {
    ChannelOrder co = CDI.current().select(ChannelOrder.class).get();
    co.setOrderId(manoOrder.getOrderRef());
    co.setOrderNo(manoOrder.getOrderRef());
    co.setChannelInstance(getStringParameter("channelInstance"));
    co.setImportDate(new Date());
    synchronized (MANO_ORDER_DATE_FORMAT) {
      try {
        co.setOrderDate(MANO_ORDER_DATE_FORMAT.parse(manoOrder.getOrderTime()));
      } catch (ParseException ex) {
        co.setOrderDate(new Date());
        //TODO log?
      }
    }

    if (manoOrder.getBillingAddress() != null) {
      co.getParties().put("invoice", map(manoOrder.getBillingAddress()));
    }
    if (manoOrder.getShippingAddress() != null) {
      co.getParties().put("shipping", map(manoOrder.getShippingAddress()));
    }
    if (manoOrder.getRelay() != null) {
      co.getParties().put("relay", map(manoOrder.getRelay()));
    }

    int lineItemCounter = 0;
    for (Product manoProd : manoOrder.getProductList().getProducts()) {
      ChannelOrderLineItem coli = map(manoProd);
      coli.setLineId(String.valueOf(lineItemCounter));
      coli.setLineNo(String.valueOf(lineItemCounter));
      co.getLineItems().add(coli);
      lineItemCounter++;
    }
    if (manoOrder.getShippingPrice().signum() != 0) {
      ChannelOrderLineItem coli = CDI.current().select(ChannelOrderLineItem.class).get();
      coli.setSku(getStringParameter("shippingCostsSKU"));
      coli.setName("Shipping");
      coli.setOrderQuantity(1);
      coli.setSinglePrice(manoOrder.getShippingPrice().movePointRight(2).intValue());
      coli.setLineId(String.valueOf(lineItemCounter));
      coli.setLineNo(String.valueOf(lineItemCounter));
      co.getLineItems().add(coli);
    }

    return co;
  }

  private ChannelOrderParty map(Address manoAddress) {
    ChannelOrderParty cop = CDI.current().select(ChannelOrderParty.class).get();

    String receiver = null;
    cop.setName(manoAddress.getFirstname() + " " + manoAddress.getLastname());
    if (manoAddress.getCompany() != null) {
      receiver = cop.getName();
      cop.setName(manoAddress.getCompany());
    }
    cop.setAddress(
        Arrays.stream(
            new String[]{manoAddress.getAddress1(),
              manoAddress.getAddress2(),
              manoAddress.getAddress3(),
              receiver
            })
            .filter(part -> part != null)
            .collect(Collectors.joining("\n")));

    cop.setCity(manoAddress.getZipcode() + " " + manoAddress.getCity());
    cop.setCountryCode(manoAddress.getCountryIso());

    cop.setPhone(manoAddress.getPhone());
    cop.setEmail(manoAddress.getEmail());
    return cop;
  }

  private ChannelOrderParty map(Relay manoRelay) {
    ChannelOrderParty cop = CDI.current().select(ChannelOrderParty.class).get();

    cop.setName(manoRelay.getName());
    cop.setAddress(manoRelay.getAddress());

    cop.setCity(manoRelay.getZipcode() + " " + manoRelay.getCity());
    cop.setCountryCode(manoRelay.getCountryIso());

    return cop;
  }

  private ChannelOrderLineItem map(Product manoProd) {
    ChannelOrderLineItem coli = CDI.current().select(ChannelOrderLineItem.class).get();
    coli.setSku(manoProd.getSku());
    coli.setChannelSku(manoProd.getSku());
    coli.setName(manoProd.getTitle());
    coli.setOrderQuantity(manoProd.getQuantity().intValue());
    coli.setSinglePrice(manoProd.getPrice().movePointRight(2).intValue());
    return coli;
  }

  private void verifyParameters() {
    if (getParameter("channelInstance") == null) {
      throw new RuntimeException("Parameter 'channelInstance' is required");
    }
    if (getParameter("shippingCostsSKU") == null) {
      throw new RuntimeException("Parameter 'shippingCostsSKU' is required");
    }
  }

}
