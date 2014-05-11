package edu.naukma.reshet.shared;

import java.io.File;
import java.util.List;

public interface IndexManager {
  public boolean createIndex(String indexName);
  public boolean deleteIndex(String indexName);
  public boolean addDocument(String indexName, String fileName, byte[] bytes);
  public File getDocument(String indexName, String fileName);
  public Searcher getSearcher(String indexName);
  public Searcher2 getSearcher2(String indexName);

  public List<String> getAllIndexes();
}
