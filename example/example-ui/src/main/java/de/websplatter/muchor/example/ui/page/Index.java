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
package de.websplatter.muchor.example.ui.page;

import de.websplatter.muchor.example.ui.page.include.Navigation;
import java.net.URI;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@RequestScoped
public class Index extends HTMLPage {

  private static final Navigation NAVI = new Navigation();

  @Override
  protected String getBody(URI requestURI) {
    StringBuilder sb = new StringBuilder();
    sb.append(NAVI.get("home"));
    sb.append("</div>");
    sb.append("<div>");
    sb.append("<h1>MuChOr - Your <strong>Mu</strong>lti <strong>Ch</strong>annel <strong>Or</strong>ganizer</h1>");
    sb.append("</div>");
    return sb.toString();
  }

}
