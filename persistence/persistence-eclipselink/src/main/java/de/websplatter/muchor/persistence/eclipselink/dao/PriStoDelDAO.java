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
import de.websplatter.muchor.persistence.eclipselink.entity.PriStoDel;
import java.util.Collection;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@ApplicationScoped
public class PriStoDelDAO extends de.websplatter.muchor.persistence.dao.PriStoDelDAO {

  @Inject
  @MuChOr
  EntityManager em;

  @Override
  public de.websplatter.muchor.persistence.entity.PriStoDel findBySkuAndChannelInstance(String sku, String channelInstance) {
//    return em.createNamedQuery("PriStoDel.byChannelInstanceAndSku", PriStoDel.class)
//        .setParameter("channelInstance", channelInstance)
//        .setParameter("sku", sku)
//        .getResultList().stream()
//        .findFirst().orElse(null);
    return em.find(PriStoDel.class, new PriStoDel.PK(sku, channelInstance));
  }

  @Override
  public Collection<? extends de.websplatter.muchor.persistence.entity.PriStoDel> findByChannelInstance(String channelInstance) {
    return em.createNamedQuery("PriStoDel.byChannelInstance", PriStoDel.class)
        .setParameter("channelInstance", channelInstance)
        .getResultList();
  }

  @Override
  public void create(de.websplatter.muchor.persistence.entity.PriStoDel entity) {
    EntityTransaction tx = em.getTransaction();
    tx.begin();
    em.persist(entity);
    tx.commit();
  }

  @Override
  public void update(de.websplatter.muchor.persistence.entity.PriStoDel entity) {
    EntityTransaction tx = em.getTransaction();
    tx.begin();
    em.merge(entity);
    tx.commit();
  }

}
