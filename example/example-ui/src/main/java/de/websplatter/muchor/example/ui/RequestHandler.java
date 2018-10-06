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
package de.websplatter.muchor.example.ui;

import de.websplatter.muchor.example.ui.page.HTMLPage;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.inject.spi.CDI;
import org.jboss.weld.context.bound.BoundRequestContext;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
public class RequestHandler<K extends HTMLPage> implements HttpHandler {

  private Class<K> forClass;

  public RequestHandler(Class<K> forClass) {
    this.forClass = forClass;
  }

  @Override
  public void handle(HttpExchange t) throws IOException {
    try {
      BoundRequestContext boundRequestContext = CDI.current().select(BoundRequestContext.class).get();
      Map<String, Object> requestDataStore = new HashMap<>();
      try {
        boundRequestContext.associate(requestDataStore);
        boundRequestContext.activate();

        K handler = CDI.current().select(forClass).get();

        byte[] response;
        try {

          response = handler.getContents(t.getRequestURI()).getBytes("UTF-8");

        } catch (RuntimeException rex) {
          if (rex.getMessage() != null && rex.getMessage().matches("HTTP\\d{3}")) {
            t.sendResponseHeaders(Integer.parseInt(rex.getMessage().substring(4)), 0);
            t.getResponseBody().close();
            return;
          }
          throw rex;
        }

        t.getResponseHeaders().add("Content-Type", "text/html");
        t.sendResponseHeaders(200, response.length);

        try (OutputStream os = t.getResponseBody()) {
          os.write(response);
        } catch (IOException ex) {
          Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
      } finally {
        try {
          boundRequestContext.invalidate();
          boundRequestContext.deactivate();
        } finally {
          boundRequestContext.dissociate(requestDataStore);
        }
      }
    } catch (Exception e) {
      Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, e);
    }
  }

}
