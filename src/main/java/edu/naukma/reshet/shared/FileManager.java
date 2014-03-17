package edu.naukma.reshet.shared;

import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: reshet
 * Date: 3/9/14
 * Time: 9:17 PM
 * To change this template use File | Settings | File Templates.
 */
public interface FileManager {
  public void writeToFile(InputStream uploadedInputStream,
                   String uploadedFileLocation);
}
