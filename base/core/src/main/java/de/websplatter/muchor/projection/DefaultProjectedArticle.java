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
package de.websplatter.muchor.projection;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * Bean that is the result of a projection for an article.
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
public class DefaultProjectedArticle {

  private String sku;
  private String channel;
  private String channelInstance;
  private String languageCode;

  private String gtin;
  private String mpn;
  private String name;
  private Brand brand;
  private Manufacturer manufacturer;
  private String catalogId;
  private Variation variation;
  private Map<String, Attribute> attributes;
  private Map<String, List<Media>> media;
  private Map<String, String> category;
  private Price price;
  private Stock stock;

  public String getSku() {
    return sku;
  }

  public void setSku(String sku) {
    this.sku = sku;
  }

  public String getChannel() {
    return channel;
  }

  public void setChannel(String channel) {
    this.channel = channel;
  }

  public String getChannelInstance() {
    return channelInstance;
  }

  public void setChannelInstance(String channelInstance) {
    this.channelInstance = channelInstance;
  }

  public String getLanguageCode() {
    return languageCode;
  }

  public void setLanguageCode(String languageCode) {
    this.languageCode = languageCode;
  }

  public String getGtin() {
    return gtin;
  }

  public void setGtin(String gtin) {
    this.gtin = gtin;
  }

  public String getMpn() {
    return mpn;
  }

  public void setMpn(String mpn) {
    this.mpn = mpn;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCatalogId() {
    return catalogId;
  }

  public void setCatalogId(String catalogId) {
    this.catalogId = catalogId;
  }

  public Brand getBrand() {
    return brand;
  }

  public void setBrand(Brand brand) {
    this.brand = brand;
  }

  public Manufacturer getManufacturer() {
    return manufacturer;
  }

  public void setManufacturer(Manufacturer manufacturer) {
    this.manufacturer = manufacturer;
  }

  public Variation getVariation() {
    return variation;
  }

  public void setVariation(Variation variation) {
    this.variation = variation;
  }

  public Map<String, Attribute> getAttributes() {
    if (attributes == null) {
      attributes = new HashMap<>();
    }
    return attributes;
  }

  public Map<String, List<Media>> getMedia() {
    if (media == null) {
      media = new HashMap<>();
    }
    return media;
  }

  public Map<String, String> getCategory() {
    if (category == null) {
      category = new HashMap<>();
    }
    return category;
  }

  public Price getPrice() {
    return price;
  }

  public void setPrice(Price price) {
    this.price = price;
  }

  public Stock getStock() {
    return stock;
  }

  public void setStock(Stock stock) {
    this.stock = stock;
  }

  public static class Brand {

    private String key;
    private String name;

    public String getKey() {
      return key;
    }

    public void setKey(String key) {
      this.key = key;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

  }

  public static class Manufacturer {

    private String key;
    private String name;

    public String getKey() {
      return key;
    }

    public void setKey(String key) {
      this.key = key;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

  }

  public static class Variation {

    private String key;
    private List<String> attributes = new LinkedList<>();

    public String getKey() {
      return key;
    }

    public void setKey(String key) {
      this.key = key;
    }

    public List<String> getAttributes() {
      return attributes;
    }

  }

  public static class Attribute {

    private String key;
    private String value;
    private String unit;

    public String getKey() {
      return key;
    }

    public void setKey(String key) {
      this.key = key;
    }

    public String getValue() {
      return value;
    }

    public void setValue(String value) {
      this.value = value;
    }

    public String getUnit() {
      return unit;
    }

    public void setUnit(String unit) {
      this.unit = unit;
    }

  }

  public static class Media implements Comparable<Media> {

    private String type;
    private int priority;
    private String url;

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }

    public int getPriority() {
      return priority;
    }

    public void setPriority(int priority) {
      this.priority = priority;
    }

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }

    @Override
    public int compareTo(Media otherMedia) {
      if (getType().equals(otherMedia.getType())) {
        return Integer.compare(otherMedia.getPriority(), getPriority());
      }
      return getType().compareTo(otherMedia.getType());
    }

  }

  public static class Price {

    private int netPrice;
    private int grossPrice;
    private String currency;
    private float vatPercentage;

    public int getNetPrice() {
      return netPrice;
    }

    public void setNetPrice(int netPrice) {
      this.netPrice = netPrice;
    }

    public int getGrossPrice() {
      return grossPrice;
    }

    public void setGrossPrice(int grossPrice) {
      this.grossPrice = grossPrice;
    }

    public String getCurrency() {
      return currency;
    }

    public void setCurrency(String currency) {
      this.currency = currency;
    }

    public float getVatPercentage() {
      return vatPercentage;
    }

    public void setVatPercentage(float vatPercentage) {
      this.vatPercentage = vatPercentage;
    }

  }

  public static class Stock {

    private int quantity;
    private int processingTimeInDays;
    private boolean restockable;
    private int restockTimeInDays;
    private int restockQuantity;
    private int shippingTimeInDays;

    public int getQuantity() {
      return quantity;
    }

    public void setQuantity(int quantity) {
      this.quantity = quantity;
    }

    public int getProcessingTimeInDays() {
      return processingTimeInDays;
    }

    public void setProcessingTimeInDays(int processingTimeInDays) {
      this.processingTimeInDays = processingTimeInDays;
    }

    public boolean isRestockable() {
      return restockable;
    }

    public void setRestockable(boolean restockable) {
      this.restockable = restockable;
    }

    public int getRestockTimeInDays() {
      return restockTimeInDays;
    }

    public void setRestockTimeInDays(int restockTimeInDays) {
      this.restockTimeInDays = restockTimeInDays;
    }

    public int getRestockQuantity() {
      return restockQuantity;
    }

    public void setRestockQuantity(int restockQuantity) {
      this.restockQuantity = restockQuantity;
    }

    public int getShippingTimeInDays() {
      return shippingTimeInDays;
    }

    public void setShippingTimeInDays(int shippingTimeInDays) {
      this.shippingTimeInDays = shippingTimeInDays;
    }

  }
}
