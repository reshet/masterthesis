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

@Component("ukrlemindexer")
public class UkrLemmatizedIndexer implements Indexer {

  @Value("${indexpath}")
  private String directory;
  public UkrLemmatizedIndexer(){
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
    return "Indexed 212 documents";
  }

  @Override
  public Integer indexDocument(Document doc) {
    IndexWriter writer = null;
    try {
      writer = getIndexWriter(false);
      writer.addDocument(doc);
      closeIndexWriter();
    } catch (IOException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
    return writer.maxDoc();
  }

  @Override
  public Integer docCount() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  private IndexWriter indexWriter;
  private IndexWriter getIndexWriter(boolean create) throws IOException {
    if (indexWriter == null) {
      indexWriter = new IndexWriter(
              new SimpleFSDirectory(
                      new File(directory)),
                      new IndexWriterConfig(Version.LUCENE_47,
                              getUkrLemAnalyzer())
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
}
