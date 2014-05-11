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
  public String indexDocument(Document doc) {
    try {
      indexWriter.addDocument(doc);
      closeIndexWriter();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "OK";
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
