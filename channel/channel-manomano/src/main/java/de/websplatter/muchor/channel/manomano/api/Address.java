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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Address extends Response {

  @XmlElement(name = "lastname")
  private String lastname;
  @XmlElement(name = "firstname")
  private String firstname;
  @XmlElement(name = "company")
  private String company;
  @XmlElement(name = "is_professional")
  private String professional;
  @XmlElement(name = "address_1")
  private String address1;
  @XmlElement(name = "address_2")
  private String address2;
  @XmlElement(name = "address_3")
  private String address3;
  @XmlElement(name = "region_1")
  private String region1;
  @XmlElement(name = "region_2")
  private String region2;
  @XmlElement(name = "region_3")
  private String region3;
  @XmlElement(name = "zipcode")
  private String zipcode;
  @XmlElement(name = "city")
  private String city;
  @XmlElement(name = "country")
  private String country;
  @XmlElement(name = "country_iso")
  private String countryIso;
  @XmlElement(name = "phone")
  private String phone;
  @XmlElement(name = "email")
  private String email;

  public String getLastname() {
    return lastname;
  }

  public String getFirstname() {
    return firstname;
  }

  public String getCompany() {
    return company;
  }

  public String getProfessional() {
    return professional;
  }

  public String getAddress1() {
    return address1;
  }

  public String getAddress2() {
    return address2;
  }

  public String getAddress3() {
    return address3;
  }

  public String getRegion1() {
    return region1;
  }

  public String getRegion2() {
    return region2;
  }

  public String getRegion3() {
    return region3;
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

  public String getPhone() {
    return phone;
  }

  public String getEmail() {
    return email;
  }
}
