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
package de.websplatter.muchor.channel.google;

import com.google.api.services.content.model.Product;
import de.websplatter.muchor.CategoryAttributeMapper;
import de.websplatter.muchor.Notifier;
import de.websplatter.muchor.persistence.dao.ChannelAttributeDAO;
import de.websplatter.muchor.persistence.entity.ChannelAttribute;
import de.websplatter.muchor.projection.DefaultProjectedArticle;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@ApplicationScoped
public class GoogleProductMapper {

  @Inject
  GoogleChannel channelConfig;
  @Inject
  ChannelAttributeDAO channelAttributeDAO;
  @Inject
  CategoryAttributeMapper catAttrMapper;

  public Product map(DefaultProjectedArticle pa) {
    String channelInstance = pa.getChannelInstance();

    Product gp = new Product();

    gp.setChannel("online");//Verkaufbar im Online-Shop
    gp.setContentLanguage(pa.getLanguageCode());
    gp.setOfferId(pa.getSku());

    gp.setMpn(pa.getMpn());

    if (pa.getGtin() != null) {
      gp.setGtin(pa.getGtin());
    } else {
      Notifier.article(pa.getSku())
          .channelInstance(channelInstance)
          .code("ERROR_MISSING_GTIN")//TODO define codes
          .publish();
      return null;
    }

//      gp.setAvailability(mapAvailability(pa));
//      gp.setPrice(mapPrice(pa));
//      gp.setTaxes(mapTaxes(pa, channelInstance));
    if (pa.getBrand() != null) {
      gp.setBrand(pa.getBrand().getName());
    }

    gp.setIdentifierExists((gp.getGtin() != null || (gp.getMpn() != null && gp.getBrand() != null)));

    String title = pa.getName();
    if (title == null) {
      Notifier.article(pa.getSku())
          .channelInstance(channelInstance)
          .code("ERROR_NAME_MISSING")//TODO define codes
          .publish();
      return null;
    }
    if (title.length() > 150) {
      title = title.substring(0, 150);
      Notifier.article(pa.getSku())
          .channelInstance(channelInstance)
          .code("WARN_NAME_SHORTENED")//TODO define codes
          .publish();
    }
    gp.setTitle(title);

    String categoryKey = pa.getCategory().get(channelConfig.getCategorySets()[0]);
    if (categoryKey != null) {
      gp.setGoogleProductCategory(categoryKey);
    } else {
      Notifier.article(pa.getSku())
          .channelInstance(channelInstance)
          .code("ERROR_NO_CATEGORIZATION")//TODO define codes
          .publish();
      return null;
    }

    //TODO
    //gp.setShipping(list);
    for (ChannelAttribute ca : channelAttributeDAO.findByChannelAndCategorySet(channelConfig.getKey(), channelConfig.getCategorySets()[0])) {
      Object result = catAttrMapper.map(pa, ca);
      if (result == null) {
        continue;
      }

      if (ca.getPossibleValuesKey() == null || ca.getPossibleValuesKey().isEmpty() || new HashSet<>(ca.getPossibleValuesKey()).contains(result.toString())) {
        reflectionMagic(gp, ca.getKey(), result);
      }
    }

    if (gp.getImageLink() == null || gp.getImageLink().trim().isEmpty()) {
      Notifier.article(pa.getSku())
          .channelInstance(channelInstance)
          .code("ERROR_NO_PICTURES")//TODO define codes
          .publish();
      return null;
    }

    return gp;
  }

  void reflectionMagic(Product gp, String attributeKey, Object result) {
    try {
      Object objectToSet = gp;
      Queue<String> keyPath = new LinkedList<>(Arrays.asList(attributeKey.split("\\.")));
      while (!keyPath.isEmpty()) {
        String key = keyPath.poll();

        String getter = Optional.of(key).map(s -> {
          return "get" + s.substring(0, 1).toUpperCase() + s.substring(1);
        }).get();
        String setter = Optional.of(key).map(s -> {
          return "set" + s.substring(0, 1).toUpperCase() + s.substring(1);
        }).get();

        Method getterMethod = null;
        Method setterMethod = null;
        for (Method m : objectToSet.getClass().getMethods()) {
          if (m.getName().equals(getter)) {
            getterMethod = m;
          } else if (m.getName().equals(setter)) {
            setterMethod = m;
          } else {
            continue;
          }

          if (getterMethod != null && setterMethod != null) {
            break;
          }
        }
        if (getterMethod == null || setterMethod == null) {
          break;
        }
        if (keyPath.isEmpty()) {
          Class c = setterMethod.getParameterTypes()[0];
          if (c == Double.class) {
            setterMethod.invoke(objectToSet, Double.parseDouble(result.toString()));
          } else if (c == Float.class) {
            setterMethod.invoke(objectToSet, Float.parseFloat(result.toString()));
          } else if (c == Integer.class) {
            setterMethod.invoke(objectToSet, Integer.parseInt(result.toString()));
          } else if (c == Long.class) {
            setterMethod.invoke(objectToSet, Long.parseLong(result.toString()));
          } else if (c == Boolean.class) {
            setterMethod.invoke(objectToSet, Boolean.parseBoolean(result.toString()));
          } else if (c == String.class) {
            setterMethod.invoke(objectToSet, result.toString());
          } else if (c == List.class) {
            if (result instanceof List) {
              setterMethod.invoke(objectToSet, result);
            }
            if (result.getClass().isArray()) {
              setterMethod.invoke(objectToSet, Arrays.asList((Object[]) result));
            }

          }
        } else {
          Object newParam = getterMethod.invoke(objectToSet);
          if (newParam == null) {
            newParam = setterMethod.getParameterTypes()[0].newInstance();
          }
          setterMethod.invoke(objectToSet, newParam);
          objectToSet = newParam;
        }
      }
    } catch (InvocationTargetException | IllegalArgumentException | IllegalAccessException | InstantiationException | SecurityException e) {
      Logger.getLogger(GoogleProductMapper.class.getName()).log(Level.WARNING, "Could net set attribute '" + attributeKey + "' ", e);
    }
  }
}
