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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
public class CommunicationArchiver {

  private static final SimpleDateFormat FOLDER_TIME_FORMAT = new SimpleDateFormat("yyyy/MM/dd");

  private ArchiveBuilder builder;

  protected CommunicationArchiver() {
  }

  public static ArchiveBuilder builder() {
    return new ArchiveBuilder();
  }

  public CommunicationArchiver build(ArchiveBuilder builder) {
    this.builder = builder;
    return this;
  }

  public OutputStream openStream() throws IOException {
    File folder;
    synchronized (FOLDER_TIME_FORMAT) {
      folder = new File("/tmp/muchor/" + builder.channel + "/" + builder.channelInstance + "/" + builder.fileType + "/" + FOLDER_TIME_FORMAT.format(builder.time.getTime()));
    }
    if (!folder.exists()) {
      folder.mkdirs();
    }
    return new FileOutputStream(folder.getAbsolutePath() + "/" + builder.fileName);
  }

  public static class ArchiveBuilder {

    private String channel;
    private String channelInstance;
    private Date time = new Date();
    private String fileType;
    private String fileName;

    private ArchiveBuilder() {
    }

    public ArchiveBuilder channel(String channel) {
      this.channel = channel;
      return this;
    }

    public ArchiveBuilder channelInstance(String channelInstance) {
      this.channelInstance = channelInstance;
      return this;
    }

    public ArchiveBuilder time(Date time) {
      this.time = time;
      return this;
    }

    public ArchiveBuilder fileType(String fileType) {
      this.fileType = fileType;
      return this;
    }

    public ArchiveBuilder fileName(String fileName) {
      this.fileName = fileName;
      return this;
    }

    public CommunicationArchiver build() {
      Instance<CommunicationArchiver> instance = CDI.current().select(CommunicationArchiver.class);
      CommunicationArchiver ca;
      if (instance.isUnsatisfied()) {
        ca = new CommunicationArchiver();
      } else {
        ca = instance.get();
      }
      return ca.build(this);
    }
  }

}
