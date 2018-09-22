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
package de.websplatter.muchor.example.filler;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import de.websplatter.muchor.Job;
import de.websplatter.muchor.JobMonitor;
import de.websplatter.muchor.persistence.dao.ArticleDAO;
import de.websplatter.muchor.persistence.dao.AttributeDAO;
import de.websplatter.muchor.persistence.dao.PriStoDelDAO;
import de.websplatter.muchor.persistence.dao.VariationDAO;
import de.websplatter.muchor.persistence.eclipselink.entity.Article;
import de.websplatter.muchor.persistence.eclipselink.entity.Attribute;
import de.websplatter.muchor.persistence.eclipselink.entity.PriStoDel;
import de.websplatter.muchor.persistence.eclipselink.entity.Variation;
import java.io.InputStreamReader;
import java.util.Arrays;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@RequestScoped
public class ExampleDataImport extends Job {

  @Inject
  private AttributeDAO attributeDAO;
  @Inject
  private VariationDAO variationDAO;
  @Inject
  private ArticleDAO articleDAO;
  @Inject
  private PriStoDelDAO priStoDelDAO;
  @Inject
  private JobMonitor monitor;

  @Override
  public void run() {
    monitor.begin(ExampleDataImport.class.getSimpleName());
    try {

      Gson g = new Gson();

      monitor.log("Importing Attributes");
      Arrays.stream((Attribute[]) g.fromJson(new JsonReader(new InputStreamReader(getClass().getResourceAsStream("/data/attributes.json"))), Attribute[].class))
          .forEach(attributeDAO::create);
      monitor.log("Importing Variations");
      Arrays.stream((Variation[]) g.fromJson(new JsonReader(new InputStreamReader(getClass().getResourceAsStream("/data/variations.json"))), Variation[].class))
          .forEach(variationDAO::create);
      monitor.log("Importing Articles");
      Arrays.stream((Article[]) g.fromJson(new JsonReader(new InputStreamReader(getClass().getResourceAsStream("/data/articles.json"))), Article[].class))
          .forEach(articleDAO::create);
      monitor.log("Importing PriStoDels");
      Arrays.stream((PriStoDel[]) g.fromJson(new JsonReader(new InputStreamReader(getClass().getResourceAsStream("/data/pristodel.json"))), PriStoDel[].class))
          .forEach(priStoDelDAO::create);

      System.out.println(articleDAO.findBySKU("fooHMT3GrL16").getLanguageSpecifics().size());
      monitor.succeed();
    } catch (Exception e) {
      monitor.fail();
      e.printStackTrace();
    }

  }
}
