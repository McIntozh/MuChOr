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
public class MediaLink extends de.websplatter.muchor.persistence.entity.MediaLink {

  private int priority;
  private String type;
  private String url;

  @Override
  public int getPriority() {
    return priority;
  }

  @Override
  public void setPriority(int priority) {
    this.priority = priority;
  }

  @Override
  public String getType() {
    return type;
  }

  @Override
  public void setType(String type) {
    this.type = type;
  }

  @Override
  public String getUrl() {
    return url;
  }

  @Override
  public void setUrl(String url) {
    this.url = url;
  }

}
