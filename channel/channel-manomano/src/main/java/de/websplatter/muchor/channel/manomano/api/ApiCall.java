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
package de.websplatter.muchor.channel.manomano.api;

import de.websplatter.muchor.channel.manomano.api.bean.Response;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
public final class ApiCall<RES extends Response> {

  private final Class<RES> expectedResponseClass;
  private String channelInstance;
  private String method;
  private Map<String, String> parameters = new HashMap<>();

  private ApiCall(Class<RES> expectedResponseClass) {
    this.expectedResponseClass = expectedResponseClass;
  }

  public static <RES extends Response> ApiCall builder(Class<? extends Response> expectedResponseClass) {
    return new ApiCall<>(expectedResponseClass);
  }

  public ApiCall forChannelInstance(String channelInstance) {
    this.channelInstance = channelInstance;
    return this;
  }

  public ApiCall onMethod(String method) {
    this.method = method;
    return this;
  }

  Class<RES> getExpectedResponseClass() {
    return expectedResponseClass;
  }

  String getChannelInstance() {
    return channelInstance;
  }

  String getMethod() {
    return method;
  }

  Map<String, String> getParameters() {
    return parameters;
  }

  public ApiCall<RES> build() {
    return this;
  }

  public ApiCall addParameter(String key, String value) {
    this.parameters.put(key, key);
    return this;
  }

}
