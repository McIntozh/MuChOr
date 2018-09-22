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
package de.websplatter.muchor.protocol.file;

import de.websplatter.muchor.MuchorProtocol;
import de.websplatter.muchor.annotation.Protocol;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@RequestScoped
@Protocol("file")
public class ProtocolFile extends MuchorProtocol {

  private static final String SEP = File.separator;

  private String rootPath = "";

  @Override
  protected void connect(Map<String, String> parameters) throws IOException {
    rootPath = parameters.get("directory");
    if (!rootPath.endsWith(SEP)) {
      rootPath += SEP;
    }
  }

  @Override
  public List<String> ls(String path) throws IOException {
    return Arrays.asList(new File(rootPath + path).list());
  }

  @Override
  public void save(String path, String fileName, byte[] bytes) throws IOException {
    File folder = new File(rootPath + path);
    if (!folder.exists()) {
      folder.mkdirs();
    }

    try (FileOutputStream fos = new FileOutputStream(new File(folder, fileName))) {
      fos.write(bytes);
      fos.flush();
    }
  }

  @Override
  public void rename(String path, String oldFileName, String newFileName) throws IOException {
    path = rootPath + path;
    File file = new File(path + (path.endsWith(SEP) ? "" : SEP) + oldFileName);
    File newFile = new File(path + (path.endsWith(SEP) ? "" : SEP) + newFileName);
    if (file.exists()) {
      file.renameTo(newFile);
    }
  }

  @Override
  public void remove(String path, String fileName) throws IOException {
    path = rootPath + path;
    File file = new File(path + (path.endsWith(SEP) ? "" : SEP) + fileName);
    if (file.exists()) {
      file.delete();
    }
  }

  @Override
  public void close() throws IOException {

  }
}
