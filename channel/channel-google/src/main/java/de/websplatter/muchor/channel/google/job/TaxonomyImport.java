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
package de.websplatter.muchor.channel.google.job;

import de.websplatter.muchor.Job;
import de.websplatter.muchor.JobMonitor;
import de.websplatter.muchor.Notifier;
import de.websplatter.muchor.channel.google.GoogleChannel;
import de.websplatter.muchor.persistence.dao.CategoryDAO;
import de.websplatter.muchor.persistence.entity.Category;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@RequestScoped
public class TaxonomyImport extends Job {

  private static final String PATH = "http://www.google.com/basepages/producttype/taxonomy-with-ids.%s-%s.txt";
  private static final int CONNECT_TIMEOUT_IN_MILLIS = 9000;
  private static final int READ_TIMEOUT_IN_MILLIS = 1000 * 60 * 5;

  @Inject
  GoogleChannel channelConfig;
  @Inject
  CategoryDAO categoryDAO;
  @Inject
  JobMonitor monitor;

  @Override
  public void run() {
    String url = String.format(PATH, getOrDefaultStringParameter("languageCode", "en").toLowerCase(), getOrDefaultStringParameter("countryCode", "US").toUpperCase());

    monitor.begin(TaxonomyImport.class.getSimpleName());

    monitor.log("Downloading taxonomy from " + url);

    List<String> lines;

    HttpURLConnection connection = null;
    try {
      connection = (HttpURLConnection) new URL(url).openConnection();

      connection.setConnectTimeout(CONNECT_TIMEOUT_IN_MILLIS);
      connection.setReadTimeout(READ_TIMEOUT_IN_MILLIS);
      connection.setDoInput(true);
      connection.setRequestProperty("Accept-Charset", "UTF-8");
      connection.setRequestProperty("Content-Type", "txt/plain");

      if (connection.getResponseCode() != 200) {
        Notifier.builder(Notifier.Severity.WARNING)
            .job(TaxonomyImport.class.getSimpleName())
            .message("Got HTTP error downloading taxonomy: " + connection.getHeaderField(0))
            .publish();
        monitor.fail();
        return;
      }

      lines = readLines(connection.getInputStream());
      if (lines.isEmpty()) {
        Notifier.builder(Notifier.Severity.WARNING)
            .job(TaxonomyImport.class.getSimpleName())
            .message("Taxonomy is empty")
            .publish();
        monitor.fail();
        return;
      }
    } catch (IOException ex) {
      Notifier.builder(Notifier.Severity.WARNING)
          .message("Got error downloading taxonomy")
          .job(TaxonomyImport.class.getSimpleName())
          .exception(ex)
          .publish();
      monitor.fail();
      return;
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }

    try {
      Map<String, Category> loadedCategoriesByKey = lines.stream()
          .filter(line -> (!line.isEmpty() && !line.startsWith("#")))
          .map(line -> buildBean(line.split(" - ", 2)))
          .collect(Collectors.toMap(Category::getKey, Function.identity()));

      monitor.log("Importing " + loadedCategoriesByKey.size() + " found categories");

      List<? extends Category> categoriesInDB = categoryDAO.findByChannelAndCategorySet(channelConfig.getKey(), channelConfig.getCategorySets()[0]);
      categoriesInDB.forEach((dbCat) -> {
        Category lCat = loadedCategoriesByKey.remove(dbCat.getKey());
        if (lCat == null) {
          if (dbCat.getOutdated() == null) {
            dbCat.setOutdated(new Date());
            categoryDAO.save(dbCat);
          }
        } else {
          if (!lCat.getPath().equals(dbCat.getPath())) {
            dbCat.setPath(dbCat.getPath());
            categoryDAO.save(dbCat);
          }
        }
      });

      loadedCategoriesByKey.values().forEach((lCat) -> {
        categoryDAO.save(lCat);
      });
    } catch (RuntimeException ex) {
      Notifier.builder(Notifier.Severity.WARNING)
          .job(TaxonomyImport.class.getSimpleName())
          .message("Got error parsing/saving taxonomy")
          .exception(ex)
          .publish();
      monitor.fail();
      return;
    }
    monitor.succeed();

  }

  private List<String> readLines(InputStream inputStream) throws IOException {
    List<String> lines = new LinkedList<>();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")))) {
      String s;
      while ((s = reader.readLine()) != null) {
        if (s != null) {
          lines.add(s.trim());
        }
      }
    }
    return lines;
  }

  private Category buildBean(String[] idWithCategoryPath) {
    Category c = CDI.current().select(Category.class).get();
    System.out.println(c);

    c.setChannel(channelConfig.getKey());
    c.setCategorySet(channelConfig.getCategorySets()[0]);
    c.setKey(idWithCategoryPath[0]);
    c.setPath(idWithCategoryPath[1].replaceAll(" > ", " | "));
    return c;
  }

}
