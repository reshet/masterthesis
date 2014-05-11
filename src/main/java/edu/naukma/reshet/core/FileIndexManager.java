package edu.naukma.reshet.core;

import edu.naukma.reshet.shared.IndexManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FileIndexManager implements IndexManager{
  @Value("${indexpath}")
  String indexPath;

  @Override
  public boolean createIndex(String indexName) {
    System.out.println(indexName);
    System.out.println(indexPath);

    return false;
  }

  @Override
  public boolean deleteIndex(String indexName) {
    return false;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public boolean addDocument(String fileName, String indexName) {
    return false;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public List<String> getAllIndexes() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }
}
