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

import de.websplatter.muchor.persistence.entity.MediaLink;
import de.websplatter.muchor.persistence.entity.AttributeValue;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.Dependent;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@Dependent
public class ChannelSpecifics extends de.websplatter.muchor.persistence.entity.ChannelSpecifics {

  private String name;
  private List<de.websplatter.muchor.persistence.mongo.entity.MediaLink> mediaLinks;
  private Map<String, de.websplatter.muchor.persistence.mongo.entity.AttributeValue> attributes;
  private String catalogId;
  private Map<String, String> categoryAssignments;

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public List<MediaLink> getMediaLinks() {
    if (mediaLinks == null) {
      mediaLinks = new LinkedList<>();
    }
    return (List<MediaLink>) (List) mediaLinks;
  }

  @Override
  public Map<String, AttributeValue> getAttributes() {
    if (attributes == null) {
      attributes = new HashMap<>();
    }
    return (Map<String, AttributeValue>) (Map) attributes;
  }

  @Override
  public String getCatalogId() {
    return catalogId;
  }

  @Override
  public void setCatalogId(String catalogId) {
    this.catalogId = catalogId;
  }

  @Override
  public Map<String, String> getCategoryAssignments() {
    if (categoryAssignments == null) {
      categoryAssignments = new HashMap<>();
    }
    return categoryAssignments;
  }

}
