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

import de.websplatter.muchor.MuChOr;
import de.websplatter.muchor.channel.manomano.ManoManoChannel;
import de.websplatter.muchor.channel.manomano.api.bean.Response;
import de.websplatter.muchor.channel.manomano.api.bean.ResponseCodes;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.xml.bind.JAXB;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@ApplicationScoped
public class Api {

  private static final int CONNECT_TIMEOUT_IN_MILLIS = 9000;
  private static final int READ_TIMEOUT_IN_MILLIS = 1000 * 60 * 5;

  private String URL_TEMPLATE = "%s?login=%s&password=%s&method=%s&%s";

  @Inject
  private MuChOr.Config config;
  @Inject
  private ManoManoChannel channelDescription;

  public <RES extends Response> RES get(ApiCall<RES> apiCall) throws IOException {
    Map<String, Object> ciConfig = (Map<String, Object>) config.get("channel." + channelDescription.getKey() + "." + apiCall.getChannelInstance());

    HttpURLConnection connection = null;
    try {
      URL url = new URL(String.format(URL_TEMPLATE,
          ciConfig.get("endpoint"),
          URLEncoder.encode(ciConfig.get("login").toString(), "UTF-8"),
          URLEncoder.encode(ciConfig.get("password").toString(), "UTF-8"),
          URLEncoder.encode(apiCall.getMethod(), "UTF-8"),
          apiCall.getParameters().entrySet().stream().map(e -> {
            String key = e.getKey();
            String value = e.getValue();
            try {
              key = URLEncoder.encode(e.getKey(), "UTF-8");
              value = URLEncoder.encode(e.getValue(), "UTF-8");
            } catch (UnsupportedEncodingException ex) {
              //Ignore, should already have occured before this stream mapping
            }
            return key + "=" + value;
          }).collect(Collectors.joining("&"))
      ));

      Logger.getLogger(Api.class.getName()).log(Level.INFO, "Calling: {0}", url.toString().replaceAll("password=[^&]+&", "password=****&"));
      connection = (HttpURLConnection) url.openConnection();

      connection.setConnectTimeout(CONNECT_TIMEOUT_IN_MILLIS);
      connection.setReadTimeout(READ_TIMEOUT_IN_MILLIS);
      connection.setDoInput(true);
      connection.setRequestProperty("Accept-Charset", "UTF-8");
      connection.setRequestProperty("Content-Type", "txt/plain");

      if (connection.getResponseCode() == 200) {
        return JAXB.unmarshal(connection.getInputStream(), apiCall.getExpectedResponseClass());//TODO this is vurnerable to XXE;
      }
      if (connection.getResponseCode() == 401) {
        try {
          RES unauthorized = apiCall.getExpectedResponseClass().newInstance();
          unauthorized.setCode(ResponseCodes.ERROR_AUTH);
          return unauthorized;
        } catch (InstantiationException | IllegalAccessException ex) {
          throw new IOException(ex);
        }
      }

    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }
    return null;
  }
}
