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
import static de.websplatter.muchor.Notifier.ArticleNotificationBuilder.Code.ERROR_INVALID_GTIN;
import static de.websplatter.muchor.Notifier.ArticleNotificationBuilder.Code.ERROR_NAME_MISSING;
import static de.websplatter.muchor.Notifier.ArticleNotificationBuilder.Code.ERROR_NO_CATEGORIZATION;
import static de.websplatter.muchor.Notifier.ArticleNotificationBuilder.Code.ERROR_NO_PICTURES;
import static de.websplatter.muchor.Notifier.ArticleNotificationBuilder.Code.WARNING_NAME_SHORTENED;
import de.websplatter.muchor.persistence.dao.ChannelAttributeDAO;
import de.websplatter.muchor.persistence.entity.ChannelAttribute;
import de.websplatter.muchor.projection.DefaultProjectedArticle;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collections;
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

    gp.setChannel("online");//Sellable online
    gp.setContentLanguage(pa.getLanguageCode());
    gp.setOfferId(pa.getSku());

    gp.setMpn(pa.getMpn());

    if (pa.getGtin() != null) {
      gp.setGtin(pa.getGtin());
    } else {
      Notifier.article(pa.getSku())
          .channelInstance(channelInstance)
          .code(ERROR_INVALID_GTIN.getCode())
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
          .code(ERROR_NAME_MISSING.getCode())
          .publish();
      return null;
    }
    if (title.length() > 150) {
      title = title.substring(0, 150);
      Notifier.article(pa.getSku())
          .channelInstance(channelInstance)
          .code(WARNING_NAME_SHORTENED.getCode())
          .publish();
    }
    gp.setTitle(title);

    String categoryKey = pa.getCategory().get(channelConfig.getCategorySets()[0]);
    if (categoryKey != null) {
      gp.setGoogleProductCategory(categoryKey);
    } else {
      Notifier.article(pa.getSku())
          .channelInstance(channelInstance)
          .code(ERROR_NO_CATEGORIZATION.getCode())
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
          .code(ERROR_NO_PICTURES.getCode())
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

          Object value = tryParsingToClass(c, result);

          if (c == List.class) {
            if (result instanceof List) {
              setterMethod.invoke(objectToSet, result);
            } else if (result.getClass().isArray()) {
              setterMethod.invoke(objectToSet, Arrays.asList((Object[]) result));
            } else {
              ParameterizedType genericType = (ParameterizedType) setterMethod.getGenericParameterTypes()[0];
              c = (Class) genericType.getActualTypeArguments()[0];

              setterMethod.invoke(objectToSet, Collections.singletonList(tryParsingToClass(c, result)));
            }
          } else {
            setterMethod.invoke(objectToSet, value);
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

  private Object tryParsingToClass(Class c, Object o) {
    if (c == Double.class) {
      return Double.parseDouble(o.toString());
    } else if (c == Float.class) {
      return Float.parseFloat(o.toString());
    } else if (c == Integer.class) {
      return Integer.parseInt(o.toString());
    } else if (c == Long.class) {
      return Long.parseLong(o.toString());
    } else if (c == Boolean.class) {
      return Boolean.parseBoolean(o.toString());
    } else if (c == String.class) {
      return o.toString();
    }
    return o;
  }
}
