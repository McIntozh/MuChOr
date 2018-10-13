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
package de.websplatter.muchor.persistence.mongo.entity;

import javax.enterprise.context.Dependent;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@Dependent
public class Dispatch implements de.websplatter.muchor.persistence.entity.Dispatch {

  private String carrier;
  private int shippingTimeInDays;
  private int netPrice;
  private int grossPrice;
  private String currency;
  private float vatPercentage;

  @Override
  public String getCarrier() {
    return carrier;
  }

  @Override
  public void setCarrier(String carrier) {
    this.carrier = carrier;
  }

  @Override
  public int getShippingTimeInDays() {
    return shippingTimeInDays;
  }

  @Override
  public void setShippingTimeInDays(int shippingTimeInDays) {
    this.shippingTimeInDays = shippingTimeInDays;
  }

  @Override
  public int getNetPrice() {
    return netPrice;
  }

  @Override
  public void setNetPrice(int netPrice) {
    this.netPrice = netPrice;
  }

  @Override
  public int getGrossPrice() {
    return grossPrice;
  }

  @Override
  public void setGrossPrice(int grossPrice) {
    this.grossPrice = grossPrice;
  }

  @Override
  public String getCurrency() {
    return currency;
  }

  @Override
  public void setCurrency(String currency) {
    this.currency = currency;
  }

  @Override
  public float getVatPercentage() {
    return vatPercentage;
  }

  @Override
  public void setVatPercentage(float vatPercentage) {
    this.vatPercentage = vatPercentage;
  }

}
