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
import java.util.Date;
import javax.enterprise.context.Dependent;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@Entity
@Table(name = "channel_order_party")
@Dependent
public class ChannelOrderParty extends de.websplatter.muchor.persistence.entity.ChannelOrderParty implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;
  @Column(name = "customerNo")
  private String customerNo;
  @Column(name = "name")
  private String name;
  @Column(name = "address", length = 1024)
  private String address;
  @Column(name = "city")
  private String city;
  @Column(name = "regionCode")
  private String regionCode;
  @Column(name = "countryCode")
  private String countryCode;
  @Column(name = "phone")
  private String phone;
  @Column(name = "email")
  private String email;
  @Temporal(TemporalType.DATE)
  @Column(name = "birthDay")
  private Date birthDay;

  @Column(name = "type")
  private String type;
  @ManyToOne
  @JoinColumn(name = "channelOrderId", nullable = false)
  private ChannelOrder channelOrder;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

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

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public ChannelOrder getChannelOrder() {
    return channelOrder;
  }

  public void setChannelOrder(ChannelOrder channelOrder) {
    this.channelOrder = channelOrder;
  }

}
