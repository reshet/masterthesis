package edu.naukma.reshet.core;

import com.carrotsearch.ant.tasks.junit4.dependencies.com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import edu.naukma.reshet.shared.Searcher;
import org.apache.lucene.analysis.ru.RussianAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SimpleTextSearcher implements Searcher {
  private String indexDir;
  private IndexSearcher searcher = null;
  private IndexReader reader = null;
  private QueryParser parser = null;
  public SimpleTextSearcher(){}
  public SimpleTextSearcher(String indexDir){
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
  @Override
  public List<String> search(String query) {
      TopDocs top = performSearch(query);
      ScoreDoc[] scores = top.scoreDocs;
      List<String> snippets = Lists.newArrayList();
      for(ScoreDoc score: scores){
        try {
          Document doc = reader.document(score.doc);
          IndexableField field = doc.getField("content");
          if (field != null){
            String str = field.stringValue();
            snippets.add(str);
          }
        } catch (IOException e) {
          e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
      }
      return snippets;
  }

  @Override
  public List<String> getTerms() {
    List<String> terms_str = new ArrayList<String>();
    File indexDirFile = new File(this.getIndexDir());
    Directory dir = null;
    try {
      dir = FSDirectory.open(indexDirFile);
      IndexReader indexReader  = DirectoryReader.open(dir);

      Terms terms = indexReader.getTermVector(0, "content");
      if(terms==null){
         return terms_str;
      }
      //terms.iterator("s");
      TermsEnum termsEnum = null;
      termsEnum = terms.iterator(TermsEnum.EMPTY);
      Map<String, Integer> frequencies = Maps.newHashMap();
      BytesRef text = null;
      while ((text = termsEnum.next()) != null) {
        String term = text.utf8ToString();
        int freq = (int) termsEnum.totalTermFreq();
        frequencies.put(term, freq);
        terms_str.add(term);
      }
    } catch (IOException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
    return terms_str;
  }

  @Override
  public Map<String, Integer> getFrequencies() {
    Map<String, Integer> frequencies = Maps.newHashMap();
    File indexDirFile = new File(this.getIndexDir());
    Directory dir = null;
    try {
      dir = FSDirectory.open(indexDirFile);
      IndexReader indexReader  = DirectoryReader.open(dir);
      Terms terms = indexReader.getTermVector(0,"content");
      if(terms==null){
        return frequencies;
      }
      //terms.iterator("s");
      TermsEnum termsEnum = null;
      termsEnum = terms.iterator(TermsEnum.EMPTY);
      BytesRef text = null;
      while ((text = termsEnum.next()) != null) {
        String term = text.utf8ToString();
        int freq = (int) termsEnum.totalTermFreq();
        frequencies.put(term, freq);
      }
    } catch (IOException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
    return frequencies;
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
}
