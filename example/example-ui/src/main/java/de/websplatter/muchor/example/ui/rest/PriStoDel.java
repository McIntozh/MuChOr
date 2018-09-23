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
import de.websplatter.muchor.persistence.dao.PriStoDelDAO;
import java.util.Optional;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@Path("/priStoDel/{channelInstance}")
public class PriStoDel {

  @Inject
  PriStoDelDAO priStoDelDAO;

  @GET
  @Path("/")
  @Produces(MediaType.APPLICATION_JSON)
  public String get(@PathParam("channelInstance") String channelInstance) {
    return Optional.of(priStoDelDAO.findByChannelInstance(channelInstance)).map(a -> {
      return new Gson().toJson(a);
    }).orElse(null);
  }

  @GET
  @Path("/{sku}/")
  @Produces(MediaType.APPLICATION_JSON)
  public String get(@PathParam("sku") String sku, @PathParam("channelInstance") String channelInstance) {
    return Optional.of(priStoDelDAO.findBySkuAndChannelInstance(sku, channelInstance)).map(a -> {
      return new Gson().toJson(a);
    }).orElse(null);
  }

//  @PUT
//  @Path("/{sku}/")
//  @Produces(MediaType.APPLICATION_JSON)
//  public void put(@PathParam("sku") String sku, @PathParam("channelInstance") String channelInstance, String priStoDel) {
//    de.websplatter.muchor.persistence.entity.PriStoDel entity = new Gson().fromJson(priStoDel, de.websplatter.muchor.persistence.entity.PriStoDel.class);
//    entity.setChannelInstance(channelInstance);
//    entity.setSku(sku);
//    priStoDelDAO.create(entity);
//  }

}
