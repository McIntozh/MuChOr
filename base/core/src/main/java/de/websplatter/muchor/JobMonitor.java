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

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@RequestScoped
public class JobMonitor {

  private String jobDescription;

  public void begin(String jobDescription) {
    this.jobDescription = jobDescription;
    Logger.getLogger(JobMonitor.class.getName()).log(Level.INFO, "{0} started", jobDescription);
  }

  public void log(String message) {
    Logger.getLogger(JobMonitor.class.getName()).log(Level.INFO, "{0}: {1}", new Object[]{jobDescription, message});
  }

  public void succeed() {
    Logger.getLogger(JobMonitor.class.getName()).log(Level.INFO, "{0} succeeded", jobDescription);
  }

  public void fail() {
    Logger.getLogger(JobMonitor.class.getName()).log(Level.WARNING, "{0} failed", jobDescription);
  }
}
