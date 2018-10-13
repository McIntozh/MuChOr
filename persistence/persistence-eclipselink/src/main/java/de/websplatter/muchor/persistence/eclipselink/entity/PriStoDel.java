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
import java.util.Objects;
import javax.enterprise.context.Dependent;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@Entity
@IdClass(PriStoDel.PK.class)
@Table(name = "pri_sto_del")
@NamedQueries({
  @NamedQuery(name = "PriStoDel.byChannelInstance", query = "SELECT psd FROM PriStoDel psd WHERE psd.channelInstance = :channelInstance")
//  ,
//  @NamedQuery(name = "PriStoDel.byChannelInstanceAndSku", query = "SELECT psd FROM PriStoDel psd WHERE psd.channelInstance = :channelInstance AND psd.sku = :sku")
})
@Dependent
public class PriStoDel implements de.websplatter.muchor.persistence.entity.PriStoDel, Serializable {

  @Id
  @Column(name = "sku", length = 36)
  private String sku;
  @Id
  @Column(name = "channelInstance", length = 36)
  private String channelInstance;
  @Column(name = "stockQuantity")
  private int stockQuantity;
  @Column(name = "netPrice")
  private int netPrice;
  @Column(name = "grossPrice")
  private int grossPrice;
  @Column(name = "currency")
  private String currency;
  @Column(name = "vatPercentage")
  private float vatPercentage;
  @Column(name = "processingTimeInDays")
  private int processingTimeInDays;
  @Column(name = "restockable")
  private boolean restockable;
  @Column(name = "restockTimeInDays")
  private int restockTimeInDays;
  @Column(name = "restockQuantity")
  private int restockQuantity;

  @Override
  public String getSku() {
    return sku;
  }

  @Override
  public void setSku(String sku) {
    this.sku = sku;
  }

  @Override
  public String getChannelInstance() {
    return channelInstance;
  }

  @Override
  public void setChannelInstance(String channelInstance) {
    this.channelInstance = channelInstance;
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

  public static class PK implements Serializable {

    private String sku;
    private String channelInstance;

    public PK() {
    }

    public PK(String sku, String channelInstance) {
      this.sku = sku;
      this.channelInstance = channelInstance;
    }

    public String getSku() {
      return sku;
    }

    public void setSku(String sku) {
      this.sku = sku;
    }

    public String getChannelInstance() {
      return channelInstance;
    }

    public void setChannelInstance(String channelInstance) {
      this.channelInstance = channelInstance;
    }

    @Override
    public int hashCode() {
      int hash = 3;
      hash = 89 * hash + Objects.hashCode(this.sku);
      hash = 89 * hash + Objects.hashCode(this.channelInstance);
      return hash;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final PK other = (PK) obj;
      if (!Objects.equals(this.sku, other.sku)) {
        return false;
      }
      if (!Objects.equals(this.channelInstance, other.channelInstance)) {
        return false;
      }
      return true;
    }

  }
}
