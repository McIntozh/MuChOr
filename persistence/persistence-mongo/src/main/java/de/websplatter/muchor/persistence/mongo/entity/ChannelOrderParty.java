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

import java.util.Date;
import javax.enterprise.context.Dependent;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@Dependent
public class ChannelOrderParty implements de.websplatter.muchor.persistence.entity.ChannelOrderParty {

  private String customerNo;
  private String name;
  private String address;
  private String zipCode;
  private String city;
  private String regionCode;
  private String countryCode;
  private String phone;
  private String email;
  private Date birthDay;

  @Override
  public String getCustomerNo() {
    return customerNo;
  }

  @Override
  public void setCustomerNo(String customerNo) {
    this.customerNo = customerNo;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getAddress() {
    return address;
  }

  @Override
  public void setAddress(String address) {
    this.address = address;
  }

  @Override
  public String getZipCode() {
    return zipCode;
  }

  @Override
  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  @Override
  public String getCity() {
    return city;
  }

  @Override
  public void setCity(String city) {
    this.city = city;
  }

  @Override
  public String getRegionCode() {
    return regionCode;
  }

  @Override
  public void setRegionCode(String regionCode) {
    this.regionCode = regionCode;
  }

  @Override
  public String getCountryCode() {
    return countryCode;
  }

  @Override
  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  @Override
  public String getPhone() {
    return phone;
  }

  @Override
  public void setPhone(String phone) {
    this.phone = phone;
  }

  @Override
  public String getEmail() {
    return email;
  }

  @Override
  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public Date getBirthDay() {
    return birthDay;
  }

  @Override
  public void setBirthDay(Date birthDay) {
    this.birthDay = birthDay;
  }

}
