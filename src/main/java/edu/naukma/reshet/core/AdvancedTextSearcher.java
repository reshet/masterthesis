package edu.naukma.reshet.core;

import com.carrotsearch.ant.tasks.junit4.dependencies.com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import edu.naukma.reshet.model.DocText;
import edu.naukma.reshet.model.TermInDoc;
import edu.naukma.reshet.shared.Searcher;
import edu.naukma.reshet.shared.Searcher2;
import org.apache.lucene.analysis.ru.RussianAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdvancedTextSearcher implements  Searcher2 {
  private String indexDir;
  private IndexSearcher searcher = null;
  private IndexReader reader = null;
  private QueryParser parser = null;
  public AdvancedTextSearcher(){}
  public AdvancedTextSearcher(String indexDir){
    this.setIndexDir(indexDir);
    File indexDirFile = new File(this.getIndexDir());
    Directory dir = null;
    try {
      dir = FSDirectory.open(indexDirFile);
      this.reader  = DirectoryReader.open(dir);
      this.searcher = new IndexSearcher(reader);
      this.parser = new QueryParser(Version.LUCENE_47,"content", new RussianAnalyzer(Version.LUCENE_47));
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
  public TopDocs performSearch(String queryString){
    BooleanQuery query = new BooleanQuery();
    query.add(new TermQuery(new Term("content", queryString)), BooleanClause.Occur.MUST);
    //query.add(new MatchAllDocsQuery(), BooleanClause.Occur.MUST);
    TopDocs hits = null;
    try {
      hits = searcher.search(query,100);
    } catch (IOException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
    return hits;
  }

  public String getIndexDir() {
    return indexDir;
  }

  public void setIndexDir(String indexDir) {
    this.indexDir = indexDir;
  }

  @Override
  public List<DocText> searchDocs(TermInDoc term) {
    TopDocs top = performSearch(term.getTermin().getText());
    ScoreDoc[] scores = top.scoreDocs;
    List<DocText> snippets = Lists.newArrayList();
    for(ScoreDoc score: scores){
      try {
        Document doc = reader.document(score.doc);
        IndexableField field = doc.getField("content");
        if (field != null){
          String str = field.stringValue();
          snippets.add(new DocText(str,new Long(score.doc),term));
        }
      } catch (IOException e) {
        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
      }
    }
    return snippets;
  }
}
