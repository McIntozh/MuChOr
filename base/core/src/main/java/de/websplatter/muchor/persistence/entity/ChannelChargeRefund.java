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
public interface ChannelChargeRefund {

  public int getAmount();

  public void setAmount(int amount);

  public Date getImportTime();

  public void setImportTime(Date importTime);

  public Date getExportTime();

  public void setExportTime(Date exportTime);

  public String getReason();

  public void setReason(String reason);

}
