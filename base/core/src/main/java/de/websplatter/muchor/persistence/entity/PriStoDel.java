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
package de.websplatter.muchor.persistence.entity;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
public abstract class PriStoDel {

  public abstract String getSku();

  public abstract void setSku(String sku);

  public abstract String getChannelInstance();

  public abstract void setChannelInstance(String channelInstance);

  public abstract int getStockQuantity();

  public abstract void setStockQuantity(int stockQuantity);

  public abstract int getNetPrice();

  public abstract void setNetPrice(int netPrice);

  public abstract int getGrossPrice();

  public abstract void setGrossPrice(int grossPrice);

  public abstract String getCurrency();

  public abstract void setCurrency(String currency);

  public abstract float getVatPercentage();

  public abstract void setVatPercentage(float vatPercentage);

  public abstract int getProcessingTimeInDays();

  public abstract void setProcessingTimeInDays(int processingTimeInDays);

  public abstract boolean isRestockable();

  public abstract void setRestockable(boolean restockable);

  public abstract int getRestockTimeInDays();

  public abstract void setRestockTimeInDays(int restockTimeInDays);

  public abstract int getRestockQuantity();

  public abstract void setRestockQuantity(int restockQuantity);

  public abstract int getShippingTimeInDays();

  public abstract void setShippingTimeInDays(int shippingTimeInDays);

}
