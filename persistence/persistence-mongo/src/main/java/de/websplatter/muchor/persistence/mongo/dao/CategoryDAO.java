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
import de.websplatter.muchor.persistence.mongo.entity.Category;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.mongodb.morphia.Datastore;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@ApplicationScoped
public class CategoryDAO extends de.websplatter.muchor.persistence.dao.CategoryDAO {

  @Inject
  @MuChOr
  Datastore datastore;

  @Override
  public de.websplatter.muchor.persistence.entity.Category findByChannelAndCategorySetAndKey(String channel, String categorySet, String key) {
    return datastore.find(Category.class).field("_id").equal(Category.genId(channel, categorySet, key)).get();
  }

  @Override
  public List<? extends de.websplatter.muchor.persistence.entity.Category> findByChannelAndCategorySet(String channel, String categorySet) {
    return datastore.find(Category.class).field("channel").equal(channel).field("categorySet").equal(categorySet).asList();
  }

  @Override
  public void save(de.websplatter.muchor.persistence.entity.Category entity) {
    datastore.save(entity);
  }

}
