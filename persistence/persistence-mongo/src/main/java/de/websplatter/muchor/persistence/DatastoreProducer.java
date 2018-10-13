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

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import de.websplatter.muchor.MuChOr.Config;
import de.websplatter.muchor.annotation.MuChOr;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import org.mongodb.morphia.Datastore;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import org.mongodb.morphia.Morphia;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@ApplicationScoped
public class DatastoreProducer {

  private MongoClient mongoClient;
  private Morphia morphia;
  private Datastore datastore;
  private MongoDatabase database;
  @Inject
  private Config config;

  @PostConstruct
  public void startup() {
    MongoClientOptions mongoClientOptions;
    {
      Instance<MongoClientOptions> instance = CDI.current().select(MongoClientOptions.class, new AnnotationLiteral<MuChOr>() {
      });
      if (instance.isUnsatisfied()) {
        Logger.getLogger(DatastoreProducer.class.getName()).log(Level.INFO, "No MongoClientOptions found, using default. If you need specific MongoClientOptions please supply via a producer with the qualifier @MuChOr");
        mongoClientOptions = MongoClientOptions.builder().build();
      } else if (instance.isAmbiguous()) {
        Logger.getLogger(DatastoreProducer.class.getName()).log(Level.INFO, "Multiple MongoClientOptions found, using default");
        mongoClientOptions = MongoClientOptions.builder().build();
      } else {
        mongoClientOptions = instance.get();
      }
    }

    List<ServerAddress> serverAddresses = Arrays.stream(((String) config.get("persistence.mongo.cluster")).split(",")).map(ServerAddress::new).collect(Collectors.toList());
    MongoCredential credentials = MongoCredential.createCredential((String) config.get("persistence.mongo.user"), (String) config.get("persistence.mongo.db"), ((String) config.get("persistence.mongo.pwd")).toCharArray());

    this.mongoClient = new MongoClient(serverAddresses, Collections.singletonList(credentials), mongoClientOptions);
    this.database = mongoClient.getDatabase(credentials.getSource());
    this.database.listCollectionNames();
    this.morphia = new Morphia();
    this.morphia.mapPackage("de.websplatter.muchor.persistence.mongo.entity");
    this.datastore = this.morphia.createDatastore(this.mongoClient, credentials.getSource());
    this.datastore.ensureIndexes(false);
  }

  @Produces
  @MuChOr
  public Datastore getDatastore() {
    return this.datastore;
  }

  @Produces
  @MuChOr
  public MongoDatabase getDatabase() {
    return this.database;
  }
}
