package edu.naukma.reshet.orchestration;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import edu.naukma.reshet.core.IndexerFactory;
import edu.naukma.reshet.core.SimpleIndexer;
import edu.naukma.reshet.shared.IndexManager;
import edu.naukma.reshet.shared.Indexer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;

@Component
public class IndexFacade {
  @Autowired
  IndexManager indexManager;
  @Autowired
  IndexerFactory indexerFactory;

  private final LoadingCache<String, SimpleIndexer> luceneIndexers;
  public IndexFacade(){
    luceneIndexers = CacheBuilder
            .newBuilder()
            .build(new CacheLoader<String, SimpleIndexer>() {
              @Override
              public SimpleIndexer load(String indexerName) {
                return new SimpleIndexer(indexerFactory.getIndexWriter(indexerName));
              }
            });
  }
  public Indexer createTermIndex(String indexName){
    indexManager.createIndex(indexName);
    return luceneIndexers.getUnchecked(indexName);
  }
  public Indexer getTermIndex(String indexName){
    return luceneIndexers.getUnchecked(indexName);
  }
  public boolean deleteTermIndex(String indexName){
    luceneIndexers.asMap().remove(indexName);
    return indexManager.deleteIndex(indexName);
  }
  public boolean addDocument(String indexName, String fileName, byte[] bytes){
     return indexManager.addDocument(indexName,fileName,bytes);
  }
  @PreDestroy
  private void releaseIndexers(){
    try {
      for(String key:luceneIndexers.asMap().keySet()){
        luceneIndexers.getUnchecked(key).getIndexWriter().close();
    }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
