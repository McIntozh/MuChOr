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

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
public class ConfigReader {

  public Map<String, Object> read() {
    String config = System.getProperty("muchor.config");

    Map<String, Object> configuration = new HashMap<>();
    try {
      InputStream configResource = MuChOr.class.getClassLoader().getResourceAsStream("muchor.yml");
      if (configResource == null) {
        Logger.getLogger(ConfigReader.class.getName()).log(Level.INFO, "No configuration found");
        return configuration;
      }

      YamlReader reader = new YamlReader(new InputStreamReader(configResource));

      Map<String, Object> map;
      while ((map = (Map) reader.read()) != null) {
        String currentConfig = null;
        if (map.get("config") != null) {
          currentConfig = (String) map.get("config");
        }
        if (currentConfig == null) {
          configuration.putAll(map);
        } else {
          if (config != null && config.equals(currentConfig)) {
            putRecursive(configuration, map);
          }
        }

      }
    } catch (YamlException ex) {
      throw new RuntimeException(ex);
    }

    return configuration;
  }

  private void putRecursive(Map<String, Object> config, Map<String, Object> map) {
    map.keySet().forEach((key) -> {
      if (!(map.get(key) instanceof Map)) {
        config.put(key, map.get(key));
      } else {
        if (!config.containsKey(key)) {
          config.put(key, new HashMap<>());
        }
        putRecursive((Map<String, Object>) config.get(key), (Map<String, Object>) map.get(key));
      }
    });
  }

//  private Map<String, Object> flatten(Map<String, Object> map) {
//    return flatten(map, null);
//  }
//
//  private Map<String, Object> flatten(Map<String, Object> map, String parentKey) {
//    Map<String, Object> result = new HashMap<>();
//    map.keySet().forEach((key) -> {
//      String absoluteKey = key;
//      if (parentKey != null) {
//        absoluteKey = parentKey + "." + key;
//      }
//
//      if (map.get(key) instanceof Map) {
//        result.putAll(flatten((Map<String, Object>) map.get(key), absoluteKey));
//      } else {
//        result.put(absoluteKey, map.get(key));
//      }
//    });
//    return result;
//  }
}
