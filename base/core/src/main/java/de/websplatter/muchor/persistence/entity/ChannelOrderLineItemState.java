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
public interface ChannelOrderLineItemState {

  public int getQuantity();

  public void setQuantity(int quantity);

  public String getReference();

  public void setReference(String reference);

  public Date getImportTime();

  public void setImportTime(Date importTime);

  public Date getExportTime();

  public void setExportTime(Date exportTime);

  public interface Confirmation extends ChannelOrderLineItemState {

  }

  public interface Cancellation extends ChannelOrderLineItemState {

    public String getReason();

    public void setReason(String reason);
  }

  public interface CancelRequest extends ChannelOrderLineItemState {

  }

  public interface Shipping extends ChannelOrderLineItemState {

    public String getCarrier();

    public void setCarrier(String carrier);

    public String getTrackingCode();

    public void setTrackingCode(String trackingCode);
  }

  public interface Refund extends ChannelOrderLineItemState {

    public String getReason();

    public void setReason(String reason);
  }

}
