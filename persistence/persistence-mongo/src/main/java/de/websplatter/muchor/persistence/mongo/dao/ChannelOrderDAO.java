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
package de.websplatter.muchor.persistence.mongo.dao;

import de.websplatter.muchor.annotation.MuChOr;
import de.websplatter.muchor.persistence.mongo.entity.ChannelOrder;
import de.websplatter.muchor.persistence.mongo.entity.ChannelOrderLineItem;
import de.websplatter.muchor.persistence.mongo.entity.ChannelOrderLineItemState;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.mongodb.morphia.Datastore;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@ApplicationScoped
public class ChannelOrderDAO extends de.websplatter.muchor.persistence.dao.ChannelOrderDAO {

  @Inject
  @MuChOr
  Datastore datastore;

  @Override
  public de.websplatter.muchor.persistence.entity.ChannelOrder findByChannelInstanceAndOrderId(String channelInstance, String orderId) {
    return datastore.find(ChannelOrder.class).field("id").equal(ChannelOrder.genId(channelInstance, orderId)).get();
  }

  @Override
  public List<de.websplatter.muchor.persistence.entity.ChannelOrder> findByChannelInstance(String channelInstance) {
    return (List<de.websplatter.muchor.persistence.entity.ChannelOrder>) (List) datastore.find(ChannelOrder.class).field("channelInstance").equal(channelInstance).asList();
  }

  @Override
  public List<de.websplatter.muchor.persistence.entity.ChannelOrder> findWithNewOrderStatesForChannelInstance(String channelInstance) {
    return (List<de.websplatter.muchor.persistence.entity.ChannelOrder>) (List) datastore.find(ChannelOrder.class)
        .disableValidation()
        .field("channelInstance").equal(channelInstance)
        .field("lineItems").elemMatch(
        datastore.createQuery(ChannelOrderLineItem.class).disableValidation().field("states").elemMatch(
            datastore.createQuery(ChannelOrderLineItemState.class).disableValidation().field("exportTime").not().exists()
        )
    )
        .asList();
  }

  @Override
  public void create(de.websplatter.muchor.persistence.entity.ChannelOrder entity) {
    datastore.save(entity);
  }

  @Override
  public void update(de.websplatter.muchor.persistence.entity.ChannelOrder entity) {
    datastore.save(entity);
  }

}
