package edu.naukma.reshet.core;

import edu.naukma.reshet.shared.Indexer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;

import java.io.IOException;

public class SimpleIndexer implements Indexer {

  private final IndexWriter indexWriter;

  public SimpleIndexer(IndexWriter indexWriter){
    this.indexWriter = indexWriter;
  }

  @Override
  public String  getStepName(){
    return "Index step";
  }

  @Override
  public String indexDocuments() {
    return "Indexed 212 documents";
  }
  @Override
  public Integer indexDocument(Document doc) {
    try {
      indexWriter.addDocument(doc);
      indexWriter.commit();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return indexWriter.maxDoc() - 1;
  }

  @Override
  public Integer docCount() {
    return indexWriter.maxDoc();
  }

  public IndexWriter getIndexWriter() {
    return this.indexWriter;
  }

  private void closeIndexWriter() throws IOException {
    if (indexWriter != null) {
      indexWriter.close();
    }
  }
}
