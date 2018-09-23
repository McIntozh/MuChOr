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

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
public abstract class ChannelOrderLineItemState implements de.websplatter.muchor.persistence.entity.ChannelOrderLineItemState {

  private int quantity;
  private String reference;
  private Date importTime;
  private Date exportTime;

  @Override
  public int getQuantity() {
    return quantity;
  }

  @Override
  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  @Override
  public String getReference() {
    return reference;
  }

  @Override
  public void setReference(String reference) {
    this.reference = reference;
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

}
