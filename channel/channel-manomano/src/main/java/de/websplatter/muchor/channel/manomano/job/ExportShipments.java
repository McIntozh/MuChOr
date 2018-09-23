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

import de.websplatter.muchor.Job;
import de.websplatter.muchor.JobMonitor;
import de.websplatter.muchor.Notifier;
import de.websplatter.muchor.channel.manomano.api.Api;
import de.websplatter.muchor.channel.manomano.api.ApiCall;
import de.websplatter.muchor.channel.manomano.api.bean.Response;
import de.websplatter.muchor.channel.manomano.api.bean.ResponseCodes;
import de.websplatter.muchor.persistence.dao.ChannelOrderDAO;
import de.websplatter.muchor.persistence.entity.ChannelOrder;
import de.websplatter.muchor.persistence.entity.ChannelOrderLineItem;
import de.websplatter.muchor.persistence.entity.ChannelOrderLineItemState.Shipping;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@RequestScoped
public class ExportShipments extends Job {

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

    monitor.begin(ExportShipments.class.getSimpleName() + " - " + channelInstance);
    try {
      for (ChannelOrder co : channelOrderDAO.findWithNewOrderStatesForChannelInstance(channelInstance)) {
        Map<String, Shipment> shipmentsByCarrierTrackingNumber = new HashMap<>();

        for (ChannelOrderLineItem coli : co.getLineItems()) {
          coli.getStates().stream()
              .filter(colis -> colis instanceof Shipping && colis.getExportTime() == null)
              .map(s -> (Shipping) s).forEach(s -> {
            String key = String.join("|", s.getCarrier(), s.getTrackingCode());

            Shipment shipment = shipmentsByCarrierTrackingNumber.get(key);
            if (shipment == null) {
              shipment = new Shipment(co.getOrderId(), s.getCarrier(), s.getTrackingCode());
              shipmentsByCarrierTrackingNumber.put(key, shipment);
            }
            shipment.products.add(new ShipmentItem(coli.getChannelSku(), s));
          });
        }

        for (Shipment shipment : shipmentsByCarrierTrackingNumber.values()) {
          ApiCall<Response> call = ApiCall.builder(Response.class)
              .forChannelInstance(channelInstance)
              .onMethod("create_shipping")
              .addParameter("order_ref", shipment.orderRef)
              .addParameter("tracking_number", shipment.trackingNumber)
              .addParameter("carrier", shipment.carrier);

          int i = 1;
          for (ShipmentItem item : shipment.products) {
            call.addParameter("products[" + i + "][sku]", item.sku)
                .addParameter("products[" + i + "][quantity]", String.valueOf(item.entity.getQuantity()));
            i++;
          }
          call.build();

          Response apiResponse = api.get(call);
          if (ResponseCodes.OK.equals(apiResponse.getCode())) {
            shipment.products.forEach((item) -> {
              item.entity.setExportTime(new Date());
            });
          } else {
            Notifier.builder(Notifier.Severity.WARNING)
                .channelInstance(channelInstance)
                .job(ImportOrders.class.getSimpleName())
                .message("Could not add shipment for order '" + shipment.orderRef + "'. Got response code '" + apiResponse.getCode() + "' (" + apiResponse.getMessage() + ")")
                .publish();
          }
        }
        channelOrderDAO.update(co);
      }
      monitor.succeed();
    } catch (Exception e) {
      monitor.fail();
      Notifier.builder(Notifier.Severity.WARNING)
          .channelInstance(channelInstance)
          .job(ExportShipments.class.getSimpleName())
          .exception(e)
          .publish();
    }
  }

  private void verifyParameters() {
    if (getParameter("channelInstance") == null) {
      throw new RuntimeException("Parameter 'channelInstance' is required");
    }
  }

  private static class Shipment {

    private final String orderRef;
    private final String carrier;
    private final String trackingNumber;
    private final List<ShipmentItem> products = new LinkedList<>();

    private Shipment(String orderRef, String carrier, String tracking_number) {
      this.orderRef = orderRef;
      this.carrier = carrier;
      this.trackingNumber = tracking_number;
    }

  }

  private static class ShipmentItem {

    private final String sku;
    private final Shipping entity;

    private ShipmentItem(String sku, Shipping entity) {
      this.sku = sku;
      this.entity = entity;
    }

  }
}
