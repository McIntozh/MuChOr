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
package de.websplatter.muchor.channel.manomano.api;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Product {

  @XmlElement(name = "sku")
  private String sku;
  @XmlElement(name = "quantity")
  private BigInteger quantity;
  @XmlElement(name = "price")
  private BigDecimal price;
  @XmlElement(name = "vat_rate")
  private BigDecimal vatRate;
  @XmlElement(name = "shipping_vat_rate")
  private BigDecimal shippingVatRate;
  @XmlElement(name = "carrier")
  private String carrier;
  @XmlElement(name = "title")
  private String title;

  public String getSku() {
    return sku;
  }

  public BigInteger getQuantity() {
    return quantity;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public BigDecimal getVatRate() {
    return vatRate;
  }

  public BigDecimal getShippingVatRate() {
    return shippingVatRate;
  }

  public String getCarrier() {
    return carrier;
  }

  public String getTitle() {
    return title;
  }

}
