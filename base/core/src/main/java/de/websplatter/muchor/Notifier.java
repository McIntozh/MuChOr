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

import static de.websplatter.muchor.Notifier.Severity.ERROR;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
public class Notifier {

  private NotificationBuilder nBuilder;
  private ArticleNotificationBuilder aBuilder;

  protected Notifier() {
  }

  protected Notifier build(NotificationBuilder nBuilder) {
    this.nBuilder = nBuilder;
    return this;
  }

  protected Notifier build(ArticleNotificationBuilder aBuilder) {
    this.aBuilder = aBuilder;
    return this;
  }

  public static NotificationBuilder builder(Severity severity) {
    NotificationBuilder nb = new NotificationBuilder();
    nb.severity = severity;
    return nb;
  }

  public static ArticleNotificationBuilder article(String sku) {
    ArticleNotificationBuilder nb = new ArticleNotificationBuilder();
    nb.sku = sku;
    return nb;
  }

  public void publish() {
    if (nBuilder != null) {

      Level ll = Level.INFO;
      switch (nBuilder.severity) {
        case WARNING:
          ll = Level.WARNING;
          break;
        case ERROR:
          ll = Level.SEVERE;
          break;
      }
      StringBuilder msg = new StringBuilder();
      if (nBuilder.job != null) {
        msg.append(nBuilder.job);
        if (nBuilder.channelInstance != null) {
          msg.append(" - ");
          msg.append(nBuilder.channelInstance);
        }
        msg.append(": ");
      } else {
        if (nBuilder.channelInstance != null) {
          msg.append(nBuilder.channelInstance);
          msg.append(": ");
        }
      }
      msg.append(nBuilder.message);
      if (nBuilder.exception != null) {
        Logger.getLogger(Notifier.class.getName()).log(ll, msg.toString(), nBuilder.exception);
      } else {
        Logger.getLogger(Notifier.class.getName()).log(ll, msg.toString());
      }
    }
    if (aBuilder != null) {
      String msg = "Article '" + aBuilder.sku + "' in '" + aBuilder.channelInstance + "' has problem: '" + aBuilder.code + (aBuilder.message != null ? " (" + aBuilder.message + ")" : "") + "'.";
      Logger.getLogger(Notifier.class.getName()).log(Level.WARNING, msg);
    }
  }

  public static class ArticleNotificationBuilder {

    private String sku;
    private String channelInstance;
    private String code;
    private String message;

    private ArticleNotificationBuilder() {
    }

    public ArticleNotificationBuilder channelInstance(String channelInstance) {
      this.channelInstance = channelInstance;
      return this;
    }

    public ArticleNotificationBuilder code(String code) {
      this.code = code;
      return this;
    }

    public ArticleNotificationBuilder message(String message) {
      this.message = message;
      return this;
    }

    public Notifier build() {
      Instance<Notifier> instance = CDI.current().select(Notifier.class);
      Notifier n;
      if (instance.isUnsatisfied()) {
        n = new Notifier();
      } else {
        n = instance.get();
      }
      return n.build(this);
    }

    public void publish() {
      build().publish();
    }
  }

  public static class NotificationBuilder {

    private Severity severity;
    private Exception exception;
    private String message;
    private String channelInstance;
    private String job;

    private NotificationBuilder() {
    }

    public NotificationBuilder exception(Exception exception) {
      this.exception = exception;
      return this;
    }

    public NotificationBuilder message(String message) {
      this.message = message;
      return this;
    }

    public NotificationBuilder channelInstance(String channelInstance) {
      this.channelInstance = channelInstance;
      return this;
    }

    public NotificationBuilder job(String job) {
      this.job = job;
      return this;
    }

    public Notifier build() {
      Instance<Notifier> instance = CDI.current().select(Notifier.class);
      Notifier n;
      if (instance.isUnsatisfied()) {
        n = new Notifier();
      } else {
        n = instance.get();
      }
      return n.build(this);
    }

    public void publish() {
      build().publish();
    }
  }

  public enum Severity {
    INFO, WARNING, ERROR;
  }
}
