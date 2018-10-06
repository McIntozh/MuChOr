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
package de.websplatter.muchor.example.ui.page.include;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
public class Navigation {

  public String get(String activeTab) {
    return new StringBuilder()
        .append("<nav class=\"navbar navbar-expand-lg navbar-light bg-light\">\n"
            + "<a class=\"navbar-brand\" href=\"/\">MuChOr</a>\n"
            + "<div class=\"collapse navbar-collapse\">\n"
            + "<div class=\"navbar-nav\">\n")
        .append("<a class=\"nav-item nav-link")
        .append("home".equals(activeTab) ? " active" : "")
        .append("\" href=\"/\">Home</a>\n")
        .append("<a class=\"nav-item nav-link")
        .append("articles".equals(activeTab) ? " active" : "")
        .append("\" href=\"/articles\">Articles</a>\n")
        .append("<a class=\"nav-item nav-link")
        .append("orders".equals(activeTab) ? " active" : "")
        .append("\" href=\"/orders\">Orders</a>\n")
        .append("</div>\n"
            + "</div>\n"
            + "</nav>").toString();
  }
}
