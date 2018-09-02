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

import javax.enterprise.context.Dependent;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@Dependent
public class AttributeValue extends de.websplatter.muchor.persistence.entity.AttributeValue {

  private String value;
  private String unit;

  public AttributeValue() {
  }

  public AttributeValue(String value, String unit) {
    this.value = value;
    this.unit = unit;
  }

  @Override
  public String getValue() {
    return value;
  }

  @Override
  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public String getUnit() {
    return unit;
  }

  @Override
  public void setUnit(String unit) {
    this.unit = unit;
  }

  @Override
  public String toString() {
    return "{" + "value=" + value + ", unit=" + unit + '}';
  }

}
