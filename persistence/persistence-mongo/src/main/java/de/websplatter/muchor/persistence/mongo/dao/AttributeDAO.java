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
import de.websplatter.muchor.persistence.mongo.entity.Attribute;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.mongodb.morphia.Datastore;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@ApplicationScoped
public class AttributeDAO extends de.websplatter.muchor.persistence.dao.AttributeDAO {

  @Inject
  @MuChOr
  Datastore datastore;

  @Override
  public de.websplatter.muchor.persistence.entity.Attribute findByKey(String key) {
    return datastore.find(Attribute.class).field("key").equal(key).get();
  }

  @Override
  public void create(de.websplatter.muchor.persistence.entity.Attribute entity) {
    datastore.save(entity);
  }

  @Override
  public void update(de.websplatter.muchor.persistence.entity.Attribute entity) {
    datastore.save(entity);
  }

}
