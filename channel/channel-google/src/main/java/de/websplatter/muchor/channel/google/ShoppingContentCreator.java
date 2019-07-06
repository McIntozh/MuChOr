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

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.content.ShoppingContent;
import com.google.api.services.content.ShoppingContentScopes;
import de.websplatter.muchor.MuChOr;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.Collections;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@ApplicationScoped
public class ShoppingContentCreator {

  @Inject
  private MuChOr.Config config;
  @Inject
  private GoogleChannel channelDescription;

  private NetHttpTransport httpTransport;
  private JacksonFactory jsonFactory;

  public ShoppingContentCreator() {
    try {
      httpTransport = GoogleNetHttpTransport.newTrustedTransport();
      jsonFactory = JacksonFactory.getDefaultInstance();
    } catch (IOException | GeneralSecurityException ex) {
      throw new RuntimeException(ex);
    }
  }

  private static final char[] KS_PASS = new char[]{
    'n',
    'o',
    't',
    'a',
    's',
    'e',
    'c',
    'r',
    'e',
    't'
  };

  public ShoppingContent createForChannelInstance(String channelInstance) throws GeneralSecurityException, IOException {
    Map<String, Object> gConfig = (Map<String, Object>) config.get("channel." + channelDescription.getKey() + "." + channelInstance);
    KeyStore pkcs12Store = KeyStore.getInstance("PKCS12", "SunJSSE");
    pkcs12Store.load(getClass().getResourceAsStream("/" + gConfig.get("p12File").toString()), null);

    PrivateKey pk = (PrivateKey) pkcs12Store.getKey("privatekey", KS_PASS);

    GoogleCredential credential = new GoogleCredential.Builder().setTransport(httpTransport)
        .setJsonFactory(jsonFactory)
        .setServiceAccountId(gConfig.get("oAuthEmail").toString())
        .setServiceAccountScopes(Collections.singleton(ShoppingContentScopes.CONTENT))
        .setServiceAccountPrivateKey(pk)
        .build();

    return new ShoppingContent.Builder(httpTransport, jsonFactory, credential)
        .setApplicationName("MuChOr")
        .build();
  }

}
