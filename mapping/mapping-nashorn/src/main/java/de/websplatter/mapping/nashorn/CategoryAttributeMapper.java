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
package de.websplatter.mapping.nashorn;

import com.google.gson.Gson;
import de.websplatter.muchor.Notifier;
import de.websplatter.muchor.persistence.entity.ChannelAttribute;
import de.websplatter.muchor.projection.DefaultProjectedArticle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.RequestScoped;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.objects.NativeArray;

/**
 *
 * Maps category attributes by scripts using Nashorn Javascript framework.<br>
 * ProjectedArticle is available as <tt>'article'</tt><br>
 * <br>
 * <b>Example scripts:</b> <br>
 * <tt>'return article.name'</tt> will return the name of the article<br>
 * <tt>'return article.attributes[someAttr].value'</tt> will return the value of
 * the attribute with the key 'someAttr'
 *
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@RequestScoped
public class CategoryAttributeMapper extends de.websplatter.muchor.CategoryAttributeMapper {

  private final ScriptEngine scriptEngine = new NashornScriptEngineFactory().getScriptEngine();

  private final Map<String, CompiledScript> compiledScipts = new HashMap<>();

  @Override
  public Object map(DefaultProjectedArticle pa, ChannelAttribute ca) {
    if (ca.getMapping() == null) {
      return null;
    }

    if (scriptEngine.getBindings(ScriptContext.ENGINE_SCOPE) == null) {
      scriptEngine.setBindings(scriptEngine.createBindings(), ScriptContext.ENGINE_SCOPE);
    } else {
      scriptEngine.getBindings(ScriptContext.ENGINE_SCOPE).clear();
    }

    CompiledScript script = compiledScipts.get(ca.getMapping());
    if (script == null) {
      try {
        script = ((Compilable) scriptEngine).compile("function f(){" + ca.getMapping() + "}\nf(); ");
      } catch (ScriptException ex) {
        Notifier.builder(Notifier.Severity.WARNING)
            .message("Could not compile script for attribute '" + ca.getKey() + "' in categoryset '" + ca.getCategorySet() + "'")
            .exception(ex)
            .publish();
        return null;
      }
      compiledScipts.put(ca.getMapping(), script);
    }

    Gson g = new Gson();
    try {
      scriptEngine.put("article", scriptEngine.eval("(" + g.toJson(pa) + ")"));
    } catch (ScriptException ex) {
      Notifier.builder(Notifier.Severity.WARNING)
          .message("Could not convert projected article '" + pa.getSku() + "' to JSON")
          .exception(ex)
          .publish();
      return null;
    }
    try {
      return getNashornComplexResultAsList(script.eval());
    } catch (ScriptException ex) {
      Notifier.builder(Notifier.Severity.WARNING)
          .message("Could not evaulate script for attribute '" + ca.getKey() + "' in categoryset '" + ca.getCategorySet() + "' for article '" + pa.getSku() + "'")
          .exception(ex)
          .publish();
      return null;
    }
  }

  private Object getNashornComplexResultAsList(Object output) {
    if (output == null) {
      return null;
    }

    if (output instanceof ScriptObjectMirror) {
      ScriptObjectMirror scriptObjectMirror = (ScriptObjectMirror) output;

      if (scriptObjectMirror.isArray()) {
        Object[] resultArray = scriptObjectMirror.to(Object[].class);
        ArrayList<Object> resultList = new ArrayList<>(resultArray.length);
        for (Object subResult : resultArray) {

          Object listValue = null;
          if (subResult != null) {
            listValue = getNashornComplexResultAsList(subResult);
          }
          resultList.add(listValue);
        }
        return resultList;
      }
    }

    if (output instanceof NativeArray) {
      NativeArray nativeArray = (NativeArray) output;
      Object[] objArray = nativeArray.asObjectArray();
      return Arrays.asList(objArray);
    }

    return output;

  }

}
