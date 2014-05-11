package edu.naukma.reshet.shared;

import java.util.List;

public interface IndexManager {
  public boolean createIndex(String indexName);
  public boolean deleteIndex(String indexName);
  public boolean addDocument(String indexName, String fileName, byte[] bytes);
  public List<String> getAllIndexes();
}
