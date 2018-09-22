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
import de.websplatter.muchor.persistence.mongo.entity.ChannelAttribute;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.mongodb.morphia.Datastore;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@ApplicationScoped
public class ChannelAttributeDAO extends de.websplatter.muchor.persistence.dao.ChannelAttributeDAO {

  @Inject
  @MuChOr
  Datastore datastore;

  @Override
  public de.websplatter.muchor.persistence.entity.ChannelAttribute findByChannelAndCategorySetAndKey(String channel, String categorySet, String key) {
    return datastore.find(ChannelAttribute.class).field("_id").equal(ChannelAttribute.genId(channel, categorySet, key)).get();
  }

  @Override
  public List<? extends de.websplatter.muchor.persistence.entity.ChannelAttribute> findByChannelAndCategorySet(String channel, String categorySet) {
    return datastore.find(ChannelAttribute.class).field("channel").equal(channel).field("categorySet").equal(categorySet).asList();
  }

  @Override
  public void create(de.websplatter.muchor.persistence.entity.ChannelAttribute entity) {
    datastore.save(entity);
  }

  @Override
  public void update(de.websplatter.muchor.persistence.entity.ChannelAttribute entity) {
    datastore.save(entity);
  }

}
