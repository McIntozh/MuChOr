/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.websplatter.muchor.persistence.entity;

import java.util.Date;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
public interface ChannelOrderParty extends Named {

  public String getCustomerNo();

  public void setCustomerNo(String customerNo);

  public String getAddress();

  public void setAddress(String address);

  public String getZipCode();

  public void setZipCode(String zipCode);

  public String getCity();

  public void setCity(String city);

  public String getRegionCode();

  public void setRegionCode(String regionCode);

  public String getCountryCode();

  public void setCountryCode(String countryCode);

  public String getPhone();

  public void setPhone(String phone);

  public String getEmail();

  public void setEmail(String email);

  public Date getBirthDay();

  public void setBirthDay(Date birthDay);

}
