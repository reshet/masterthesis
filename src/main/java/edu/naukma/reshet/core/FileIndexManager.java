package edu.naukma.reshet.core;


import edu.naukma.reshet.shared.IndexManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

@Component
public class FileIndexManager implements IndexManager{
  @Value("${indexpath}")
  String indexPath;

  @Override
  public boolean createIndex(String indexName) {
    return new File(indexPath+indexName+"/files").mkdirs() &&
           new File(indexPath+indexName+"/lucene").mkdirs();
  }

  @Override
  public boolean deleteIndex(String indexName) {
    Path directory = Paths.get(indexPath + indexName);
    try {
      Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
          Files.delete(file);
          return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
          Files.delete(dir);
          return FileVisitResult.CONTINUE;
        }

      });
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  @Override
  public boolean addDocument(String indexName, String fileName, byte[] bytes) {
    String uploadFileName = indexPath + indexName + "/files/" + fileName;
    try{
      BufferedOutputStream stream =
              new BufferedOutputStream(new FileOutputStream(new File(uploadFileName)));
      stream.write(bytes);
      stream.close();
      return true;
    } catch (Exception e){
      return false;
    }
  }

  @Override
  public List<String> getAllIndexes() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }
}
