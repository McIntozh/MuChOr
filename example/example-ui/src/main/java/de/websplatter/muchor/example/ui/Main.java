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

import com.sun.net.httpserver.HttpServer;
import de.websplatter.muchor.example.ui.page.Article;
import de.websplatter.muchor.example.ui.page.Articles;
import de.websplatter.muchor.example.ui.page.Index;
import de.websplatter.muchor.example.ui.page.Orders;
import java.io.IOException;
import java.net.InetSocketAddress;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
public class Main {

  public static void main(String[] args) throws IOException {
    Weld weld = new Weld();
    try (WeldContainer container = weld.initialize()) {
      Logger.getLogger(Main.class.getName()).log(Level.INFO, "Starting HttpServer");
      HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
      server.createContext("/", new RequestHandler(Index.class));
      server.createContext("/articles", new RequestHandler(Articles.class));
      server.createContext("/orders", new RequestHandler(Orders.class));
      server.setExecutor(null);
      server.start();
      Logger.getLogger(Main.class.getName()).log(Level.INFO, "Starting HttpServer done");
      System.in.read();
    }
  }

}
