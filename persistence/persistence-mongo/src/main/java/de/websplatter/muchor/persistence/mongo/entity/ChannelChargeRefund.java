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
public class ChannelChargeRefund implements de.websplatter.muchor.persistence.entity.ChannelOrderChargeRefund, de.websplatter.muchor.persistence.entity.ChannelOrderLineItemChargeRefund {

  private int amount;
  private Date importTime;
  private Date exportTime;
  private String reason;

  @Override
  public int getAmount() {
    return amount;
  }

  @Override
  public void setAmount(int amount) {
    this.amount = amount;
  }

  @Override
  public Date getImportTime() {
    return importTime;
  }

  @Override
  public void setImportTime(Date importTime) {
    this.importTime = importTime;
  }

  @Override
  public Date getExportTime() {
    return exportTime;
  }

  @Override
  public void setExportTime(Date exportTime) {
    this.exportTime = exportTime;
  }

  @Override
  public String getReason() {
    return reason;
  }

  @Override
  public void setReason(String reason) {
    this.reason = reason;
  }

}
