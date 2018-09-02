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
package de.websplatter.muchor;

import java.util.Map;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
public abstract class Job {

  private Map<String, Object> parameters;

  public abstract void run();

  public void setParameters(Map<String, Object> parameters) {
    this.parameters = parameters;
  }

  public Object getParameter(String param) {
    return parameters.get(param);
  }

  public Object getOrDefaultParameter(String param, Object defaultValue) {
    return parameters.getOrDefault(param, defaultValue);
  }

  public String getStringParameter(String param) {
    return (String) parameters.get(param);
  }

  public String getOrDefaultStringParameter(String param, String defaultValue) {
    return (String) parameters.getOrDefault(param, defaultValue);
  }

}
