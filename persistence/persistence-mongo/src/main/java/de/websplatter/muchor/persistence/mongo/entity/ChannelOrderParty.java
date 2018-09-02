/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.websplatter.muchor.persistence.mongo.entity;

import java.util.Date;
import javax.enterprise.context.Dependent;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@Dependent
public class ChannelOrderParty extends de.websplatter.muchor.persistence.entity.ChannelOrderParty {

  public String customerNo;
  public String name;
  public String address;
  public String city;
  public String regionCode;
  public String countryCode;
  public String phone;
  public String email;
  public Date birthDay;

  public String getCustomerNo() {
    return customerNo;
  }

  public void setCustomerNo(String customerNo) {
    this.customerNo = customerNo;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getRegionCode() {
    return regionCode;
  }

  public void setRegionCode(String regionCode) {
    this.regionCode = regionCode;
  }

  public String getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Date getBirthDay() {
    return birthDay;
  }

  public void setBirthDay(Date birthDay) {
    this.birthDay = birthDay;
  }

}
