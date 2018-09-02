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
package de.websplatter.muchor;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Destroyed;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@ApplicationScoped
public class MuChOr {

  private final Config config;

  public MuChOr() {
    Logger.getLogger(MuChOr.class.getName()).log(Level.INFO, "Initializing MuChOr");
    config = new Config(new ConfigReader().read());
  }

  public void startup(@Observes @Initialized(ApplicationScoped.class) Object init) {
    Logger.getLogger(MuChOr.class.getName()).log(Level.INFO, "/Initializing MuChOr");
  }

  public void shutdown(@Observes @Destroyed(ApplicationScoped.class) Object deinit) {
    Logger.getLogger(MuChOr.class.getName()).log(Level.INFO, "Shutting down MuChOr");
  }

  @Produces
  public Config getConfig() {
    return config;
  }

  public static class Config {

    private final Map<String, Object> params;

    private Config(Map<String, Object> params) {
      this.params = params;
    }

    public Object get(String key) {
      if (key.indexOf('.') > 0) {
        Queue<String> q = new LinkedList<>(Arrays.asList(key.split("\\.")));
        Object current = params;
        while (!q.isEmpty()) {
          if (current instanceof Map) {
            current = ((Map) current).get(q.poll());
          } else {
            return null;
          }
        }
        return current;
      } else {
        return params.get(key);
      }
    }

  }

}
