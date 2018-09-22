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
package de.websplatter.muchor.protocol.smb;

import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.msfscc.FileAttributes;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2CreateOptions;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.SmbConfig;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import com.hierynomus.smbj.share.File;
import de.websplatter.muchor.MuchorProtocol;
import de.websplatter.muchor.annotation.Protocol;
import java.io.IOException;
import java.io.OutputStream;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@RequestScoped
@Protocol("smb")
public class ProtocolSMB extends MuchorProtocol {

  private static final String SEP = "/";

  private SMBClient smbClient;

  private Connection con;

  private Session session;

  private DiskShare share;

  private String rootPath = "";

  @Override
  protected void connect(Map<String, String> parameters) throws IOException {
    SmbConfig smbConfig = SmbConfig.builder()
        .withSoTimeout(30, TimeUnit.SECONDS)
        .build();

    smbClient = new SMBClient(smbConfig);
    con = smbClient.connect(parameters.get("host"));

    AuthenticationContext auth = new AuthenticationContext(parameters.get("user"), parameters.get("password").toCharArray(), parameters.getOrDefault("domain", ""));
    session = con.authenticate(auth);
    this.share = (DiskShare) session.connectShare(parameters.get("share"));

    rootPath = parameters.getOrDefault("directory", "");
    if (!rootPath.endsWith(SEP)) {
      rootPath += SEP;
    }
  }

  @Override
  public List<String> ls(String path) throws IOException {
    return share.list(rootPath + path).stream().map(fdibi -> fdibi.getFileName()).collect(Collectors.toList());
  }

  @Override
  public void save(String path, String fileName, byte[] bytes) throws IOException {
    if (!share.folderExists(rootPath + path)) {
      String cPath = rootPath;
      if (!share.folderExists(cPath)) {
        share.mkdir(cPath);
      }
      for (String d : path.split(SEP)) {
        cPath += SEP + d;
        if (!share.folderExists(cPath)) {
          share.mkdir(cPath);
        }
      }
    }

    path = rootPath + path;
    try (File file = share.openFile(path + (path.endsWith(SEP) ? "" : SEP) + fileName,
        EnumSet.of(AccessMask.FILE_WRITE_DATA),
        EnumSet.of(FileAttributes.FILE_ATTRIBUTE_NORMAL),
        EnumSet.of(SMB2ShareAccess.FILE_SHARE_WRITE),
        SMB2CreateDisposition.FILE_CREATE,
        EnumSet.of(SMB2CreateOptions.FILE_RANDOM_ACCESS)
    );
        OutputStream os = file.getOutputStream()) {
      os.write(bytes);
      os.flush();
    }
  }

  @Override
  public void rename(String path, String oldFileName, String newFileName) throws IOException {
    path = rootPath + path;
    try (File file = share.openFile(path + (path.endsWith(SEP) ? "" : SEP) + oldFileName,
        EnumSet.of(AccessMask.FILE_WRITE_ATTRIBUTES),
        EnumSet.of(FileAttributes.FILE_ATTRIBUTE_NORMAL),
        EnumSet.of(SMB2ShareAccess.FILE_SHARE_WRITE),
        SMB2CreateDisposition.FILE_OPEN,
        EnumSet.of(SMB2CreateOptions.FILE_RANDOM_ACCESS)
    )) {
      file.rename(newFileName);
    }
  }

  @Override
  public void remove(String path, String fileName) throws IOException {
    path = rootPath + path;
    String file = path + (path.endsWith(SEP) ? "" : SEP) + fileName;
    if (share.fileExists(file)) {
      share.rm(file);
    }
  }

  @Override
  public void close() throws IOException {
    if (share != null) {
      share.close();
      share = null;
    }

    if (session != null) {
      session.close();
      session = null;
    }

    if (con != null) {
      con.close();
      con = null;
    }

  }
}
