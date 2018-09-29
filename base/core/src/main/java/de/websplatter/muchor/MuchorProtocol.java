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

import de.websplatter.muchor.annotation.Protocol;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.util.AnnotationLiteral;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
public abstract class MuchorProtocol implements AutoCloseable {

  protected abstract void connect(Map<String, String> parameters) throws IOException;

  public abstract List<String> ls(String path) throws IOException;

  public abstract void save(String path, String fileName, byte[] bytes) throws IOException;

  public abstract OutputStream openStream(String path, String fileName) throws IOException;

  public abstract void rename(String path, String oldFileName, String newFileName) throws IOException;

  public abstract void remove(String path, String fileName) throws IOException;

  public static final MuchorProtocol get(Map<String, String> parameters) throws IOException {

    Instance<MuchorProtocol> i = CDI.current().select(MuchorProtocol.class, new ProtocolQualifier(parameters.get("protocol")) {
    });
    if (i.isUnsatisfied()) {
      throw new RuntimeException("No protocol found for type '" + parameters.get("protocol") + "'");
    }
    MuchorProtocol mp = i.get();
    mp.connect(parameters);
    return mp;
  }

  private static class ProtocolQualifier extends AnnotationLiteral<Protocol> implements Protocol {

    private final String value;

    public ProtocolQualifier(String value) {
      this.value = value;
    }

    @Override
    public String value() {
      return value;
    }
  }
}
