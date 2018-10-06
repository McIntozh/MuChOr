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
package de.websplatter.muchor.example.ui.page;

import de.websplatter.muchor.annotation.MuChOr;
import de.websplatter.muchor.example.ui.page.include.Navigation;
import de.websplatter.muchor.persistence.mongo.entity.Article;
import java.net.URI;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.FindOptions;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@RequestScoped
public class Articles extends HTMLPage {

  private static final Navigation NAVI = new Navigation();

  @Inject
  @MuChOr
  Datastore datastore;
  @Inject
  de.websplatter.muchor.example.ui.page.Article articlePage;

  @Override
  protected String getBody(URI requestURI) {
    String[] pathParts = requestURI.getPath().replaceFirst("^/", "").split("/");
    if (pathParts.length > 1 && pathParts[1].trim().length() > 0) {
      return articlePage.getBody(requestURI);
    }

    StringBuilder sb = new StringBuilder();
    sb.append(NAVI.get("articles"));
    sb.append("<table class=\"table table-striped table-hover\">");
    sb.append("<thead class=\"thead-dark\">");
    sb.append("<tr>");
    sb.append("<th>SKU</th>");
    sb.append("<th>GTIN</th>");
    sb.append("<th>Brand</th>");
    sb.append("<th>MPN</th>");
    sb.append("<th>Name</th>");
    sb.append("</tr>");
    sb.append("</thead>");

    datastore.find(Article.class).asList(new FindOptions().limit(500)).forEach(a -> {
      sb.append("<tr>");
      sb.append("<td><a href=\"/articles/").append(a.getSku()).append("\">").append(a.getSku()).append("</a></td>");
      sb.append("<td>").append(a.getGtin()).append("</td>");
      sb.append("<td>").append(a.getBrandKey()).append("</td>");
      sb.append("<td>").append(a.getMpn()).append("</td>");
      sb.append("<td>").append(a.getName()).append("</td>");
      sb.append("</tr>");
    });
    sb.append("</table>");
    return sb.toString();
  }

}
