package edu.naukma.reshet.core;

import edu.naukma.reshet.shared.FileManager;
import edu.naukma.reshet.shared.Indexer;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: reshet
 * Date: 3/9/14
 * Time: 6:41 PM
 * To change this template use File | Settings | File Templates.
 */
@Component("filemanager")
public class ConcreteFileManager implements FileManager {

  @Override
  public void writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) {
    try {

      int read = 0;
      byte[] bytes = new byte[1024];
      OutputStream out = new FileOutputStream(new File(uploadedFileLocation));
      while ((read = uploadedInputStream.read(bytes)) != -1) {
        out.write(bytes, 0, read);
      }
      out.flush();
      out.close();
    } catch (IOException e) {

      e.printStackTrace();
    }
  }
}
