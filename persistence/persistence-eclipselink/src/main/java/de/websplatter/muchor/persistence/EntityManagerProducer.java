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
package de.websplatter.muchor.persistence;

import de.websplatter.muchor.annotation.MuChOr;
import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.spi.PersistenceUnitTransactionType;
import static org.eclipse.persistence.config.PersistenceUnitProperties.*;
import org.eclipse.persistence.config.TargetServer;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@ApplicationScoped
public class EntityManagerProducer {

  private static final Boolean MUTEX = Boolean.FALSE;

  private static EntityManager em = null;

  @Inject
  private de.websplatter.muchor.MuChOr.Config config;

  @Produces
  @MuChOr
  public EntityManager getOrCreateEM() {
    synchronized (MUTEX) {
      if (em == null) {

        Map<String, String> configProps = (Map<String, String>) config.get("persistence.eclipselink");

        HashMap properties = new HashMap();
        properties.put(TRANSACTION_TYPE, PersistenceUnitTransactionType.RESOURCE_LOCAL.name());

        properties.put(JDBC_DRIVER, configProps.get("driver"));
        properties.put(JDBC_URL, configProps.get("url"));
        properties.put(JDBC_USER, configProps.get("user"));
        properties.put(JDBC_PASSWORD, configProps.get("password"));
        properties.put(LOGGING_LEVEL, configProps.getOrDefault("loggingLevel","WARNING"));
        properties.put(DDL_GENERATION, configProps.getOrDefault("ddlGeneration",NONE));

//        properties.put(LOGGING_TIMESTAMP, "false");
//        properties.put(LOGGING_THREAD, "false");
//        properties.put(LOGGING_SESSION, "false");
//        properties.put(DDL_GENERATION, DROP_AND_CREATE);
//        properties.put(DDL_GENERATION, CREATE_OR_EXTEND);

        properties.put(TARGET_SERVER, TargetServer.None);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("muchor", properties);
        em = emf.createEntityManager();
      }
    }
    return em;
  }

}
