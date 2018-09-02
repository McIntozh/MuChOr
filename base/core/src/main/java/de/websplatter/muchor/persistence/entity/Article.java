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

import java.util.Map;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
public abstract class Article implements Named, MediaLinked, Attributed {

  public abstract String getSku();

  public abstract void setSku(String sku);

  public abstract String getVariationKey();

  public abstract void setVariationKey(String variationKey);

  public abstract String getGtin();

  public abstract void setGtin(String gtin);

  public abstract String getMpn();

  public abstract void setMpn(String mpn);

  public abstract String getBrandKey();

  public abstract void setBrandKey(String brandKey);

  public abstract Map<String, LanguageSpecifics> getLanguageSpecifics();

  public abstract Map<String, ChannelSpecifics> getChannelSpecifics();

  public abstract Map<String, ChannelInstanceSpecifics> getChannelInstanceSpecifics();

}
