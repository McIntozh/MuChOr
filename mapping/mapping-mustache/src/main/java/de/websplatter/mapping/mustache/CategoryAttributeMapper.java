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
package de.websplatter.mapping.mustache;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import de.websplatter.muchor.persistence.entity.ChannelAttribute;
import de.websplatter.muchor.projection.DefaultProjectedArticle;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.RequestScoped;

/**
 *
 * Maps category attributes using Mustache templates.<br>
 * ProjectedArticle is available as <tt>'article'</tt><br>
 * <br>
 * <b>Example templates:</b> <br>
 * <tt>'{{article.name}}'</tt> will return the name of the article<br>
 * <tt>'{{article.attributes.someAttr.value}}'</tt> will return the value of the
 * attribute with the key 'someAttr'
 *
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@RequestScoped
public class CategoryAttributeMapper extends de.websplatter.muchor.CategoryAttributeMapper {

  private final MustacheFactory mf;

  private final Map<String, Mustache> cache = new HashMap<>();

  public CategoryAttributeMapper() {
    mf = new DefaultMustacheFactory();
  }

  @Override
  public Object map(DefaultProjectedArticle pa, ChannelAttribute ca) {
    if (ca.getMapping() == null) {
      return null;
    }

    HashMap<String, Object> scopes = new HashMap<>();
    scopes.put("article", pa);

    Mustache mustache = cache.get(ca.getMapping());
    if (mustache == null) {
      mustache = mf.compile(new StringReader(ca.getMapping()), ca.getCategorySet() + "|" + ca.getKey());
      cache.put(ca.getMapping(), mustache);
    }

    StringWriter sw = new StringWriter();
    mustache.execute(sw, scopes);
    return sw.toString();
  }

}
