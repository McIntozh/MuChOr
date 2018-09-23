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
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.PrePersist;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@Entity(value = "pri_sto_del", noClassnameStored = true)
@Indexes(
    @Index(fields = {
  @Field("sku")
  ,@Field("channelInstance")},
    options = @IndexOptions(unique = true)
    )
)
@Dependent
public class PriStoDel implements de.websplatter.muchor.persistence.entity.PriStoDel {

  @Id
  private String id;
  private String sku;
  private String channelInstance;
  private int stockQuantity;
  private int netPrice;
  private int grossPrice;
  private String currency;
  private float vatPercentage;
  private int processingTimeInDays;
  private boolean restockable;
  private int restockTimeInDays;
  private int restockQuantity;
  private int shippingTimeInDays;

  @PrePersist
  private void genAndSetId() {
    id = genId(this.sku, this.channelInstance);
  }

  public static String genId(String sku, String channelInstance) {
    return sku + "|" + channelInstance;
  }

  public String getId() {
    if (id == null) {
      genAndSetId();
    }
    return id;
  }

  @Override
  public String getSku() {
    return sku;
  }

  @Override
  public void setSku(String sku) {
    this.sku = sku;
    genAndSetId();
  }

  @Override
  public String getChannelInstance() {
    return channelInstance;
  }

  @Override
  public void setChannelInstance(String channelInstance) {
    this.channelInstance = channelInstance;
    genAndSetId();
  }

  @Override
  public int getStockQuantity() {
    return stockQuantity;
  }

  @Override
  public void setStockQuantity(int stockQuantity) {
    this.stockQuantity = stockQuantity;
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

  @Override
  public int getProcessingTimeInDays() {
    return processingTimeInDays;
  }

  @Override
  public void setProcessingTimeInDays(int processingTimeInDays) {
    this.processingTimeInDays = processingTimeInDays;
  }

  @Override
  public boolean isRestockable() {
    return restockable;
  }

  @Override
  public void setRestockable(boolean restockable) {
    this.restockable = restockable;
  }

  @Override
  public int getRestockTimeInDays() {
    return restockTimeInDays;
  }

  @Override
  public void setRestockTimeInDays(int restockTimeInDays) {
    this.restockTimeInDays = restockTimeInDays;
  }

  @Override
  public int getRestockQuantity() {
    return restockQuantity;
  }

  @Override
  public void setRestockQuantity(int restockQuantity) {
    this.restockQuantity = restockQuantity;
  }

  @Override
  public int getShippingTimeInDays() {
    return shippingTimeInDays;
  }

  @Override
  public void setShippingTimeInDays(int shippingTimeInDays) {
    this.shippingTimeInDays = shippingTimeInDays;
  }

}
