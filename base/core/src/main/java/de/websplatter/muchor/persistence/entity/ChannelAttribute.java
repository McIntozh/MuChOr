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
public abstract class ChannelAttribute implements Keyed, Named {

  public abstract String getChannel();

  public abstract void setChannel(String channel);

  public abstract String getCategorySet();

  public abstract void setCategorySet(String categorySet);

  public abstract String getCategoryKey();

  public abstract void setCategoryKey(String categoryKey);

  public abstract String getDescription();

  public abstract void setDescription(String description);

  public abstract boolean isMandatory();

  public abstract void setMandatory(boolean mandatory);

  public abstract int getSort();

  public abstract void setSort(int sort);

  public abstract Type getType();

  public abstract void setType(Type type);

  public abstract DataType getDataType();

  public abstract void setDataType(DataType dataType);

  public abstract List<String> getPossibleValuesKey();

  public abstract List<String> getPossibleValuesDescription();

  public abstract String getUnit();

  public abstract void setUnit(String unit);

  public abstract String getOverwritesAttributeKey();

  public abstract void setOverwritesAttributeKey(String overwritesAttributeKey);

  public abstract boolean isInvisible();

  public abstract void setInvisible(boolean invisible);

  public abstract Date getOutdated();

  public abstract void setOutdated(Date outdated);

  public abstract String getMapping();

  public abstract void setMapping(String mapping);

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
