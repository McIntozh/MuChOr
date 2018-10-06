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
package de.websplatter.muchor.example.ui.page;

import de.websplatter.muchor.annotation.MuChOr;
import de.websplatter.muchor.example.ui.page.include.Navigation;
import de.websplatter.muchor.persistence.mongo.entity.ChannelOrder;
import java.net.URI;
import java.text.NumberFormat;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.FindOptions;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@RequestScoped
public class Orders extends HTMLPage {

  private static final Navigation NAVI = new Navigation();

  @Inject
  @MuChOr
  Datastore datastore;

  @Override
  protected String getBody(URI requestURI) {

    StringBuilder sb = new StringBuilder();
    sb.append(NAVI.get("orders"));
    sb.append("<table class=\"table table-striped table-hover\">");
    sb.append("<thead class=\"thead-dark\">");
    sb.append("<tr>");
    sb.append("<th>ChannelInstance</th>");
    sb.append("<th>OrderId</th>");
    sb.append("<th>OrderNo</th>");
    sb.append("<th>OrderDate</th>");
    sb.append("<th>Items</th>");
    sb.append("<th>Totalsum</th>");
    sb.append("</tr>");
    sb.append("</thead>");
    datastore.find(ChannelOrder.class).order("-importDate").asList(new FindOptions().limit(500)).forEach(co -> {
      sb.append("<tr>");
      sb.append("<td>").append(co.getChannelInstance()).append("</td>");
      sb.append("<td>").append(co.getOrderId()).append("</td>");
      sb.append("<td>").append(co.getOrderNo()).append("</td>");
      sb.append("<td>").append(co.getOrderDate()).append("</td>");
      sb.append("<td><span title=\"Lines\">")
          .append(co.getLineItems().size())
          .append("</span> / <span title=\"Total\">")
          .append(co.getLineItems().stream().collect(Collectors.summingInt(l -> l.getOrderQuantity())))
          .append("</span></td>");
      sb.append("<td>")
          .append(
              NumberFormat.getCurrencyInstance().format(
                  co.getCharges().stream().collect(Collectors.summingInt(c -> c.getPrice()))
                  + co.getLineItems().stream().collect(Collectors.summingInt(l -> l.getSinglePrice() * l.getOrderQuantity()
                  + l.getCharges().stream().collect(Collectors.summingInt(c -> c.getPrice()))
                  ))
                  / 100d
              )
          ).append(" ").append(co.getCurrencyCode())
          .append("</td>");
      sb.append("</tr>");
    });
    sb.append("</table>");
    return sb.toString();
  }

}
