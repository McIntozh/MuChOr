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

import de.websplatter.muchor.Constants;
import de.websplatter.muchor.channel.manomano.api.bean.Address;
import de.websplatter.muchor.channel.manomano.api.bean.Order;
import de.websplatter.muchor.channel.manomano.api.bean.OrdersResponse;
import de.websplatter.muchor.channel.manomano.api.bean.Product;
import de.websplatter.muchor.channel.manomano.api.bean.Relay;
import de.websplatter.muchor.Job;
import de.websplatter.muchor.JobMonitor;
import de.websplatter.muchor.Notifier;
import de.websplatter.muchor.channel.manomano.api.Api;
import de.websplatter.muchor.channel.manomano.api.ApiCall;
import de.websplatter.muchor.channel.manomano.api.bean.Response;
import de.websplatter.muchor.channel.manomano.api.bean.ResponseCodes;
import de.websplatter.muchor.persistence.dao.ChannelOrderDAO;
import de.websplatter.muchor.persistence.entity.ChannelOrder;
import de.websplatter.muchor.persistence.entity.ChannelOrderCharge;
import de.websplatter.muchor.persistence.entity.ChannelOrderLineItem;
import de.websplatter.muchor.persistence.entity.ChannelOrderParty;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;

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
      ApiCall<OrdersResponse> call = ApiCall.builder(OrdersResponse.class)
          .forChannelInstance(channelInstance)
          .onMethod("get_orders").build();

      OrdersResponse apiResponse = api.get(call);
//      OrdersResponse apiResponse = JAXB.unmarshal(ImportOrders.class.getResourceAsStream("../example_orders.xml"), OrdersResponse.class);
      if (!ResponseCodes.OK.equals(apiResponse.getCode())) {
        monitor.fail();
        Notifier.builder(Notifier.Severity.WARNING)
            .channelInstance(channelInstance)
            .job(ImportOrders.class.getSimpleName())
            .message("Got response code '" + apiResponse.getCode() + "' (" + apiResponse.getMessage() + ")")
            .publish();
        return;
      }

      for (Order manoOrder : apiResponse.getOrderList().getOrders()) {
        ChannelOrder co = channelOrderDAO.findByChannelInstanceAndOrderId(channelInstance, manoOrder.getOrderRef());
        if (co == null) {
          co = map(manoOrder);
          channelOrderDAO.create(co);
        }
        acceptOrder(manoOrder);//Accept order, even if already imported, perhaps last time accept failed
      }
      monitor.succeed();
    } catch (Exception e) {
      monitor.fail();
      Notifier.builder(Notifier.Severity.WARNING)
          .channelInstance(channelInstance)
          .job(ImportOrders.class.getSimpleName())
          .exception(e)
          .publish();
    }
  }

  private final SimpleDateFormat MANO_ORDER_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  private ChannelOrder map(Order manoOrder) {
    ChannelOrder co = CDI.current().select(ChannelOrder.class).get();
    co.setOrderId(manoOrder.getOrderRef());
    co.setOrderNo(manoOrder.getOrderRef());
    co.setChannelInstance(getStringParameter("channelInstance"));
    co.setImportDate(new Date());
    co.setPaymentType(manoOrder.getPaymentSolution());
    co.setCurrencyCode(manoOrder.getCurrencyCode());

    try {
      co.setOrderDate(MANO_ORDER_DATE_FORMAT.parse(manoOrder.getOrderTime()));
    } catch (ParseException ex) {
      co.setOrderDate(new Date());
      //TODO log?
    }

    if (manoOrder.getBillingAddress() != null) {
      co.getParties().put(Constants.ORDER_PARTY_INVOICE, map(manoOrder.getBillingAddress()));
    }
    if (manoOrder.getRelay() != null) {
      co.getParties().put(Constants.ORDER_PARTY_SHIPPING, map(manoOrder.getRelay()));
    } else {
      if (manoOrder.getShippingAddress() != null) {
        co.getParties().put(Constants.ORDER_PARTY_SHIPPING, map(manoOrder.getShippingAddress()));
      }
    }

    int lineItemCounter = 0;
    for (Product manoProd : manoOrder.getProductList().getProducts()) {
      ChannelOrderLineItem coli = map(manoProd);
      coli.setLineId(String.valueOf(lineItemCounter));
      coli.setLineNo(lineItemCounter++);
      co.getLineItems().add(coli);
    }
    if (manoOrder.getShippingPrice().signum() != 0) {
      ChannelOrderCharge charge = CDI.current().select(ChannelOrderCharge.class).get();
      charge.setType(Constants.ORDER_CHARGE_SHIPPING);
      charge.setPrice(manoOrder.getShippingPrice().movePointRight(2).intValue());
      charge.setName(getOrDefaultStringParameter("shippingCostChargeName", "Shipping"));
      co.getCharges().add(charge);
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
            .filter(part -> part != null && !part.trim().isEmpty())
            .collect(Collectors.joining("\n")));

    cop.setZipCode(manoAddress.getZipcode());
    cop.setCity(manoAddress.getCity());
    cop.setCountryCode(manoAddress.getCountryIso());

    cop.setPhone(manoAddress.getPhone());
    cop.setEmail(manoAddress.getEmail());
    return cop;
  }

  private ChannelOrderParty map(Relay manoRelay) {
    ChannelOrderParty cop = CDI.current().select(ChannelOrderParty.class).get();

    cop.setName(manoRelay.getName());
    cop.setAddress(manoRelay.getAddress() + "\n" + manoRelay.getId());

    cop.setZipCode(manoRelay.getZipcode());
    cop.setCity(manoRelay.getZipcode());
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
    coli.setShippingType(manoProd.getCarrier());
    return coli;
  }

  private void verifyParameters() {
    if (getParameter("channelInstance") == null) {
      throw new RuntimeException("Parameter 'channelInstance' is required");
    }
  }

  private void acceptOrder(Order manoOrder) throws IOException {
    String channelInstance = getStringParameter("channelInstance");
    ApiCall<Response> call = ApiCall.builder(Response.class)
        .forChannelInstance(channelInstance)
        .onMethod("accept_order")
        .addParameter("order_ref", manoOrder.getOrderRef())
        .build();

    Response apiResponse = api.get(call);
    if (!ResponseCodes.OK.equals(apiResponse.getCode())) {
      Notifier.builder(Notifier.Severity.WARNING)
          .channelInstance(channelInstance)
          .job(ImportOrders.class.getSimpleName())
          .message("Could not mark order '" + manoOrder.getOrderRef() + "' as accepted. Got response code '" + apiResponse.getCode() + "' (" + apiResponse.getMessage() + ")")
          .publish();
    }
  }

}
