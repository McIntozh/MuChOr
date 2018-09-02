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
package de.websplatter.muchor.channel.manomano;

import de.websplatter.muchor.channel.manomano.api.Response;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.xml.bind.JAXB;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
public class Test {

  private static final String PATH = "https://ws.monechelle.com/";
//  private static final String PATH = "https://ws.monechelle-test.com/";
//    private static final String PATH = "http://ws.monechelle.com/?login=mon_login&password=mon_password&method=get_orders";
  private static final int CONNECT_TIMEOUT_IN_MILLIS = 9000;
  private static final int READ_TIMEOUT_IN_MILLIS = 1000 * 60 * 5;

  public static void main(String[] args) {
    HttpURLConnection connection = null;
    try {
      connection = (HttpURLConnection) new URL(PATH).openConnection();

      connection.setConnectTimeout(CONNECT_TIMEOUT_IN_MILLIS);
      connection.setReadTimeout(READ_TIMEOUT_IN_MILLIS);
      connection.setDoInput(true);
      connection.setRequestProperty("Accept-Charset", "UTF-8");
      connection.setRequestProperty("Content-Type", "txt/plain");

      if (connection.getResponseCode() != 200) {
//        Notifier.builder(Notifier.Severity.WARNING)
//            .message("Got HTTP error: " + connection.getHeaderField(0))
//            .publish();
//        monitor.fail();
        return;
      }

      Response r = JAXB.unmarshal(connection.getInputStream(), Response.class);//TODO this is vurnerable to XXE
      System.out.println(r);
    } catch (IOException ex) {
//      Notifier.builder(Notifier.Severity.WARNING)
//          .message("Got error downloading taxonomy")
//          .exception(ex)
//          .publish();
//      monitor.fail();
      return;
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }

  }
}
