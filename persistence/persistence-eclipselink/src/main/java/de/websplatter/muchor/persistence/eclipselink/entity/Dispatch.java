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

import java.io.Serializable;
import javax.enterprise.context.Dependent;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@Entity
@Table(name = "dispatch")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "target", discriminatorType = DiscriminatorType.CHAR)
@DiscriminatorValue(value = "X")
@Dependent
public class Dispatch implements de.websplatter.muchor.persistence.entity.Dispatch, Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;
  @Column(name = "carrier")
  private String carrier;
  @Column(name = "shippingTimeInDays")
  private int shippingTimeInDays;
  @Column(name = "netPrice")
  private int netPrice;
  @Column(name = "grossPrice")
  private int grossPrice;
  @Column(name = "currency")
  private String currency;
  @Column(name = "vatPercentage")
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
  public void setGrossPrice(int netPrice) {
    this.netPrice = netPrice;
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
