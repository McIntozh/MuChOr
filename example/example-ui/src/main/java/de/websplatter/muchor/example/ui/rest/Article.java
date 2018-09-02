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
package de.websplatter.muchor.example.ui.rest;

import com.google.gson.Gson;
import de.websplatter.muchor.persistence.dao.ArticleDAO;
import java.util.Optional;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@Path("/article")
public class Article {

  @Inject
  ArticleDAO articleDAO;

  @GET
  @Path("/{sku}/")
  @Produces(MediaType.APPLICATION_JSON)
  public String get(@PathParam("sku") String sku) {
    return Optional.of(articleDAO.findBySKU(sku)).map(a -> {
      return new Gson().toJson(a);
    }).orElse(null);
  }

}
