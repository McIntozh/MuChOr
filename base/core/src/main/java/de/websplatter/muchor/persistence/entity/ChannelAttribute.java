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
package de.websplatter.muchor.persistence.entity;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
public interface ChannelAttribute extends Keyed, Named {

  public String getChannel();

  public void setChannel(String channel);

  public String getCategorySet();

  public void setCategorySet(String categorySet);

  public String getCategoryKey();

  public void setCategoryKey(String categoryKey);

  public String getDescription();

  public void setDescription(String description);

  public boolean isMandatory();

  public void setMandatory(boolean mandatory);

  public int getSort();

  public void setSort(int sort);

  public Type getType();

  public void setType(Type type);

  public DataType getDataType();

  public void setDataType(DataType dataType);

  public List<String> getPossibleValuesKey();

  public List<String> getPossibleValuesDescription();

  public String getUnit();

  public void setUnit(String unit);

  public String getOverwritesAttributeKey();

  public void setOverwritesAttributeKey(String overwritesAttributeKey);

  public boolean isInvisible();

  public void setInvisible(boolean invisible);

  public Date getOutdated();

  public void setOutdated(Date outdated);

  public String getMapping();

  public void setMapping(String mapping);

  public static enum Type {
    SINGLE,
    MULTIPLE,
    RANGE
  }

  public static enum DataType {
    BOOLEAN,
    INTEGER,
    FLOAT,
    STRING,
    BINARY
  }
}
