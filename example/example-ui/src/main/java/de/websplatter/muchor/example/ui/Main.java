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

import java.io.IOException;

import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
public class Main {

  private static final URI BASE_URI = URI.create("http://localhost:8080/api");

  public static void main(String[] args) throws IOException {
    final ResourceConfig rc = new ResourceConfig().packages("de.websplatter.muchor.example.ui.rest");

    final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    server.getServerConfiguration().addHttpHandler(
        new org.glassfish.grizzly.http.server.CLStaticHttpHandler(Main.class.getClassLoader(), "/webapp/"), "/");

    server.start();
    System.out.println(String.format("Application started.\nTry out %s\nStop the application using CTRL+C",
        BASE_URI));
    System.in.read();
    server.shutdownNow();

  }

}
