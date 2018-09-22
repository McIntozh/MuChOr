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
package de.websplatter.muchor.persistence.eclipselink.dao;

import de.websplatter.muchor.annotation.MuChOr;
import de.websplatter.muchor.persistence.eclipselink.entity.Manufacturer;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@ApplicationScoped
public class ManufacturerDAO extends de.websplatter.muchor.persistence.dao.ManufacturerDAO {

  @Inject
  @MuChOr
  EntityManager em;

  @Override
  public de.websplatter.muchor.persistence.entity.Manufacturer findByKey(String key) {
    return em.find(Manufacturer.class, key);
  }

  @Override
  public void create(de.websplatter.muchor.persistence.entity.Manufacturer entity) {
    EntityTransaction tx = em.getTransaction();
    tx.begin();
    em.persist(entity);
    tx.commit();
  }

  @Override
  public void update(de.websplatter.muchor.persistence.entity.Manufacturer entity) {
    EntityTransaction tx = em.getTransaction();
    tx.begin();
    em.merge(entity);
    tx.commit();
  }

}
