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
package de.websplatter.muchor.channel.manomano.api.bean;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Order extends Response {

  @XmlElement(name = "order_ref")
  private String orderRef;
  @XmlElement(name = "order_time")
  private String orderTime;
  @XmlElement(name = "total_price")
  private BigDecimal totalPrice;
  @XmlElement(name = "payment_solution")
  private String paymentSolution;
  @XmlElement(name = "payment_solution_value")
  private String paymentSolutionValue;
  @XmlElement(name = "products_price")
  private BigDecimal productsPrice;
  @XmlElement(name = "shipping_price")
  private BigDecimal shippingPrice;
  @XmlElement(name = "firstname")
  private String firstname;
  @XmlElement(name = "lastname")
  private String lastname;
  @XmlElement(name = "fiscalnb")
  private String fiscalnb;
  @XmlElement(name = "billing_address")
  private Address billingAddress;
  @XmlElement(name = "shipping_address")
  private Address shippingAddress;
  @XmlElement(name = "relay")
  private Relay relay;
  @XmlElement(name = "products")
  private ProductList productList;

  public String getOrderRef() {
    return orderRef;
  }

  public String getOrderTime() {
    return orderTime;
  }

  public BigDecimal getTotalPrice() {
    return totalPrice;
  }

  public String getPaymentSolution() {
    return paymentSolution;
  }

  public String getPaymentSolutionValue() {
    return paymentSolutionValue;
  }

  public BigDecimal getProductsPrice() {
    return productsPrice;
  }

  public BigDecimal getShippingPrice() {
    return shippingPrice;
  }

  public String getFirstname() {
    return firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public String getFiscalnb() {
    return fiscalnb;
  }

  public Address getBillingAddress() {
    return billingAddress;
  }

  public Address getShippingAddress() {
    return shippingAddress;
  }

  public Relay getRelay() {
    return relay;
  }

  public ProductList getProductList() {
    return productList;
  }

}
