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

import static de.websplatter.muchor.Constants.ORDER_CHARGE_SHIPPING;
import de.websplatter.muchor.Job;
import de.websplatter.muchor.JobMonitor;
import de.websplatter.muchor.Notifier;
import de.websplatter.muchor.channel.manomano.api.Api;
import de.websplatter.muchor.channel.manomano.api.ApiCall;
import de.websplatter.muchor.channel.manomano.api.bean.Response;
import de.websplatter.muchor.channel.manomano.api.bean.ResponseCodes;
import de.websplatter.muchor.persistence.dao.ChannelOrderDAO;
import de.websplatter.muchor.persistence.entity.ChannelChargeRefund;
import de.websplatter.muchor.persistence.entity.ChannelOrder;
import de.websplatter.muchor.persistence.entity.ChannelOrderLineItem;
import de.websplatter.muchor.persistence.entity.ChannelOrderLineItemState;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@RequestScoped
public class ExportRefunds extends Job {

  private final NumberFormat PRICE_FORMAT;

  public ExportRefunds() {
    PRICE_FORMAT = NumberFormat.getNumberInstance(Locale.US);
    PRICE_FORMAT.setMinimumFractionDigits(0);
    PRICE_FORMAT.setMaximumFractionDigits(2);
    PRICE_FORMAT.setGroupingUsed(false);
  }

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

    monitor.begin(ExportRefunds.class.getSimpleName() + " - " + channelInstance);
    try {
      for (ChannelOrder co : channelOrderDAO.findWithNewOrderStatesForChannelInstance(channelInstance)) {

        for (ChannelOrderLineItem coli : co.getLineItems()) {
          List<ChannelChargeRefund> refundedCharges = coli.getCharges().stream()
              .filter(colic -> ORDER_CHARGE_SHIPPING.equals(colic.getType()))
              .map(
                  colic
                  -> colic.getRefunds().stream()
                      .filter(r -> r.getExportTime() == null)
                      .collect(Collectors.toList())
              ).flatMap(List::stream)
              .collect(Collectors.toList());

          AtomicInteger summedShippingCostsToRefund = new AtomicInteger(refundedCharges.stream()
              .collect(Collectors.summingInt(r -> r.getAmount())));

          List<Refund> refunds = coli.getStates().stream()
              .filter(colis -> (colis instanceof ChannelOrderLineItemState.Cancellation || colis instanceof ChannelOrderLineItemState.Refund) && colis.getExportTime() == null)
              .map(colis
                  -> new Refund(
                  co.getOrderId(),
                  coli.getChannelSku(),
                  colis.getQuantity(),
                  summedShippingCostsToRefund.getAndSet(0),
                  colis
              )
              ).collect(Collectors.toList());

          for (Refund refund : refunds) {
            ApiCall<Response> call = ApiCall.builder(Response.class)
                .forChannelInstance(channelInstance)
                .onMethod("create_refund")
                .addParameter("order_ref", refund.orderRef)
                .addParameter("sku", refund.sku)
                .addParameter("quantity", String.valueOf(refund.quantity));
            if (refund.shippingPrice > 0) {
              call.addParameter("shipping_price", PRICE_FORMAT.format(refund.shippingPrice));
            }

            call.build();

            Response apiResponse = api.get(call);
            if (ResponseCodes.OK.equals(apiResponse.getCode())) {
              refund.stateEntity.setExportTime(new Date());
              if (refund.shippingPrice > 0) {
                refundedCharges.forEach(r -> r.setExportTime(new Date()));
              }
            } else {
              Notifier.builder(Notifier.Severity.WARNING)
                  .channelInstance(channelInstance)
                  .job(ImportOrders.class.getSimpleName())
                  .message("Could not submit refund for order '" + refund.orderRef + "'. Got response code '" + apiResponse.getCode() + "' (" + apiResponse.getMessage() + ")")
                  .publish();
            }
          }
        }
        channelOrderDAO.update(co);
      }
      monitor.succeed();
    } catch (Exception e) {
      monitor.fail();
      Notifier.builder(Notifier.Severity.WARNING)
          .channelInstance(channelInstance)
          .job(ExportRefunds.class.getSimpleName())
          .exception(e)
          .publish();
    }
  }

  private void verifyParameters() {
    if (getParameter("channelInstance") == null) {
      throw new RuntimeException("Parameter 'channelInstance' is required");
    }
  }

  private static class Refund {

    private final String orderRef;
    private final String sku;
    private final int quantity;
    private final int shippingPrice;
    private final ChannelOrderLineItemState stateEntity;

    Refund(String orderRef, String sku, int quantity, int shippingPrice, ChannelOrderLineItemState stateEntity) {
      this.orderRef = orderRef;
      this.sku = sku;
      this.quantity = quantity;
      this.shippingPrice = shippingPrice;
      this.stateEntity = stateEntity;
    }

  }

}
