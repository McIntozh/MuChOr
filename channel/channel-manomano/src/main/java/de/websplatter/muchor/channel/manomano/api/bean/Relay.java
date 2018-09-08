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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Relay extends Response {

  @XmlElement(name = "id")
  private String id;
  @XmlElement(name = "name")
  private String name;
  @XmlElement(name = "address")
  private String address;
  @XmlElement(name = "zipcode")
  private String zipcode;
  @XmlElement(name = "city")
  private String city;
  @XmlElement(name = "country")
  private String country;
  @XmlElement(name = "country_iso")
  private String countryIso;

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getAddress() {
    return address;
  }

  public String getZipcode() {
    return zipcode;
  }

  public String getCity() {
    return city;
  }

  public String getCountry() {
    return country;
  }

  public String getCountryIso() {
    return countryIso;
  }

}
