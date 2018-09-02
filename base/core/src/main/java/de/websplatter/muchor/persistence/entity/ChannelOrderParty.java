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
public abstract class ChannelOrderParty {

  public abstract String getCustomerNo();

  public abstract void setCustomerNo(String customerNo);

  public abstract String getName();

  public abstract void setName(String name);

  public abstract String getAddress();

  public abstract void setAddress(String address);

  public abstract String getCity();

  public abstract void setCity(String city);

  public abstract String getRegionCode();

  public abstract void setRegionCode(String regionCode);

  public abstract String getCountryCode();

  public abstract void setCountryCode(String countryCode);

  public abstract String getPhone();

  public abstract void setPhone(String phone);

  public abstract String getEmail();

  public abstract void setEmail(String email);

  public abstract Date getBirthDay();

  public abstract void setBirthDay(Date birthDay);

}
