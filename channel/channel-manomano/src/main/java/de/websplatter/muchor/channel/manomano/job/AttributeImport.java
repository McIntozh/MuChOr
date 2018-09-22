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
package de.websplatter.muchor.channel.manomano.job;

import de.websplatter.muchor.Job;
import de.websplatter.muchor.JobMonitor;
import de.websplatter.muchor.channel.manomano.ManoManoChannel;
import de.websplatter.muchor.persistence.dao.ChannelAttributeDAO;
import de.websplatter.muchor.persistence.entity.ChannelAttribute;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@RequestScoped
public class AttributeImport extends Job {

  @Inject
  private ManoManoChannel channelConfig;
  @Inject
  private ChannelAttributeDAO channelAttributeDAO;
  @Inject
  private JobMonitor monitor;

  @Override
  public void run() {
    monitor.begin(AttributeImport.class.getSimpleName() + " - " + channelConfig.getKey());
    try {
      monitor.log("Creating channel attributes");

      createChannelAttributes().forEach((ca) -> {
        ChannelAttribute entity = channelAttributeDAO.findByChannelAndCategorySetAndKey(ca.getChannel(), ca.getCategorySet(), ca.getKey());
        if (entity == null) {
          entity = ca;
          channelAttributeDAO.create(entity);
        } else {
          entity.setName(ca.getName());
          entity.setDescription(ca.getDescription());
          entity.setType(ca.getType());
          entity.setMandatory(ca.isMandatory());
          entity.setDataType(ca.getDataType());
          entity.getPossibleValuesKey().clear();
          entity.getPossibleValuesKey().addAll(ca.getPossibleValuesKey());
          entity.getPossibleValuesDescription().clear();
          entity.getPossibleValuesDescription().addAll(ca.getPossibleValuesDescription());
          channelAttributeDAO.update(entity);
        }

      });
      monitor.succeed();
    } catch (IOException e) {
      Logger.getLogger(AttributeImport.class.getName()).log(Level.WARNING, "AttributeImport failed", e);
      monitor.fail();
    }
  }

  private List<ChannelAttribute> createChannelAttributes() throws IOException {
    List<ChannelAttribute> result = new LinkedList<>();
    getFeedFields().forEach(keyXMandatory -> {

      ChannelAttribute attr = CDI.current().select(ChannelAttribute.class).get();
      attr.setKey(keyXMandatory[0]);
      attr.setChannel(channelConfig.getKey());
      attr.setCategorySet(channelConfig.getCategorySets()[0]);

      attr.setMandatory(keyXMandatory.length > 0 && "true".equalsIgnoreCase(keyXMandatory[1]));
      attr.setName(keyXMandatory[0]);
      attr.setDescription("");
      attr.setType(ChannelAttribute.Type.SINGLE);
      attr.setDataType(ChannelAttribute.DataType.STRING);
      result.add(attr);

    });
    return result;
  }

  public static List<String[]> getFeedFields() throws IOException {
    BufferedReader lineReader = new BufferedReader(new InputStreamReader(AttributeImport.class.getResourceAsStream("../FeedFields.txt"), "UTF-8"));
    return lineReader.lines().map(line -> line.split("\t")).collect(Collectors.toList());
  }

}
