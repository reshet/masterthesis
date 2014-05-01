package edu.naukma.reshet.core;

import edu.naukma.reshet.shared.Indexer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.ru.RussianAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component("indexer")
public class ConcreteIndexer implements Indexer {
  private String directory;
  public ConcreteIndexer(){
  }
  public void setDirectory(String directory){
     this.directory = directory;
  }
  @Override
  public String  getStepName(){
    return "Index step";
  }

  @Override
  public String indexDocuments() {
    return "Indexed 334 documents";
  }

  @Override
  public String indexDocument(Document doc) {
    IndexWriter writer = null;
    try {
      writer = getIndexWriter(false);
      writer.addDocument(doc);
      closeIndexWriter();
    } catch (IOException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
    return "OK";
  }
  private IndexWriter indexWriter;
  private IndexWriter getIndexWriter(boolean create) throws IOException {
    if (indexWriter == null) {
      indexWriter = new IndexWriter(
              new SimpleFSDirectory(new File(directory)), new IndexWriterConfig(Version.LUCENE_47, new RussianAnalyzer(Version.LUCENE_47))
      );
    }
    //new RussianAnalyzer(Version.LUCENE_47), create
    return indexWriter;
  }

  private void closeIndexWriter() throws IOException {
    if (indexWriter != null) {
      indexWriter.close();
    }
  }
}
