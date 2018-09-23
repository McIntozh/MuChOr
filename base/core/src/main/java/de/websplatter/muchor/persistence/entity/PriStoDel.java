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
public interface PriStoDel {

  public String getSku();

  public void setSku(String sku);

  public String getChannelInstance();

  public void setChannelInstance(String channelInstance);

  public int getStockQuantity();

  public void setStockQuantity(int stockQuantity);

  public int getNetPrice();

  public void setNetPrice(int netPrice);

  public int getGrossPrice();

  public void setGrossPrice(int grossPrice);

  public String getCurrency();

  public void setCurrency(String currency);

  public float getVatPercentage();

  public void setVatPercentage(float vatPercentage);

  public int getProcessingTimeInDays();

  public void setProcessingTimeInDays(int processingTimeInDays);

  public boolean isRestockable();

  public void setRestockable(boolean restockable);

  public int getRestockTimeInDays();

  public void setRestockTimeInDays(int restockTimeInDays);

  public int getRestockQuantity();

  public void setRestockQuantity(int restockQuantity);

  public int getShippingTimeInDays();

  public void setShippingTimeInDays(int shippingTimeInDays);

}
