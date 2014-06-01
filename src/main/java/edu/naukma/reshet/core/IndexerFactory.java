package edu.naukma.reshet.core;

import edu.naukma.reshet.shared.Indexer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.lemmagen.LemmagenFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

@Component
public class IndexerFactory {

  @Value("${indexpath}")
  private String indexPath;
  public IndexerFactory(){
  }

  public IndexWriter getIndexWriter(String indexName){
    try {
      return new IndexWriter(
              new SimpleFSDirectory(
                      new File(indexPath + indexName + "/lucene")),
              new IndexWriterConfig(Version.LUCENE_47,
                      getUkrLemAnalyzer())
      );
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private Analyzer getUkrLemAnalyzer() {
    return new Analyzer() {

      @Override
      protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
        StandardTokenizer source = new StandardTokenizer(Version.LUCENE_47, reader);
        LemmagenFilter filter = new LemmagenFilter(source, "mlteast-uk", Version.LUCENE_47);
        return new TokenStreamComponents(source, filter);
      }
    };
  }

    public void setIndexPath(String indexPath) {
        this.indexPath = indexPath;
    }
}
