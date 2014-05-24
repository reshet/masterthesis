package edu.naukma.reshet.orchestration;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import edu.naukma.reshet.core.IndexerFactory;
import edu.naukma.reshet.core.PdfTextExtractor;
import edu.naukma.reshet.core.SimpleIndexer;
import edu.naukma.reshet.core.algorithm.RelationFinder;
import edu.naukma.reshet.core.algorithm.SnippetsFinder;
import edu.naukma.reshet.model.DocText;
import edu.naukma.reshet.model.Snippet;
import edu.naukma.reshet.model.TermInDoc;
import edu.naukma.reshet.model.TermRelation;
import edu.naukma.reshet.repositories.TermInDocRepository;
import edu.naukma.reshet.repositories.TermRelationRepository;
import edu.naukma.reshet.shared.IndexManager;
import edu.naukma.reshet.shared.Indexer;
import edu.naukma.reshet.shared.Searcher;
import edu.naukma.reshet.shared.Searcher2;
import edu.naukma.reshet.shared.algorithm.InitialTerminologyExtractor;
import eu.hlavki.text.lemmagen.LemmatizerFactory;
import eu.hlavki.text.lemmagen.api.Lemmatizer;
import org.apache.lucene.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@Component
public class IndexFacade {
  @Autowired
  IndexManager indexManager;
  @Autowired
  IndexerFactory indexerFactory;

  @Autowired
  @Qualifier("default")
  InitialTerminologyExtractor
  extractor;

  @Autowired
  TermInDocRepository repo;

  @Autowired
  TermRelationRepository relationRepo;

  @Autowired
  SnippetsFinder snipFinder;

  @Autowired
  RelationFinder relationFinder;

  private final Lemmatizer lemmatizer;

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

    Lemmatizer lm = null;
    try {
      lm = LemmatizerFactory.getPrebuilt("mlteast-uk");
    } catch (IOException e) {
      e.printStackTrace();
    }
    lemmatizer = lm;

  }
  public Indexer createTermIndex(String indexName){
    indexManager.createIndex(indexName);
    try {
      new SimpleIndexer(indexerFactory.getIndexWriter(indexName)).getIndexWriter().close();
    } catch (IOException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
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
     indexManager.addDocument(indexName,fileName,bytes);
     File docFile = indexManager.getDocument(indexName, fileName);
     Document document = new PdfTextExtractor(docFile).getDocument();
     Integer docID = this.getTermIndex(indexName).indexDocument(document);
     Searcher searcher = indexManager.getSearcher(indexName);
     List<TermInDoc> terms = extractor.extractValuableTerms(searcher, docID);
     for(TermInDoc term:terms){
       TermInDoc foundTerm = repo.findByTermin(term.getTermin());
       if(foundTerm == null){
          repo.save(term);
        }
     }
     Searcher2 searcher2 = indexManager.getSearcher2(indexName);
     snipFinder.setLm(lemmatizer);
     snipFinder.setSearcher(searcher2);

     List<TermInDoc> termsAll = repo.findAll();
     List<Snippet> snippets = snipFinder.findSnippets(termsAll);
     relationFinder.setLm(lemmatizer);
     Set<TermRelation> relations = relationFinder.getRelations(termsAll,snippets);
     relationRepo.save(relations);
//     for(TermRelation relation: relations){
//       if(relationRepo.findByTerm1AndTerm2AndRelationType(relation.getTerm1(), relation.getTerm2(), relation.getRelationType()) == null){
//         relationRepo.save(relation);
//       }
//     }
     return true;
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
