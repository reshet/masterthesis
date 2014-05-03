package edu.naukma.reshet.core;

import edu.naukma.reshet.shared.Searcher;
import jersey.repackaged.com.google.common.collect.Maps;
import org.apache.lucene.analysis.ru.RussianAnalyzer;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
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

import static com.google.common.base.Preconditions.checkNotNull;

public class SimpleTextSearcher implements Searcher {
  private String indexDir;
  private IndexSearcher searcher = null;
  private QueryParser parser = null;
  public SimpleTextSearcher(){}
  public SimpleTextSearcher(String indexDir){
    this.setIndexDir(indexDir);
    File indexDirFile = new File(this.getIndexDir());
    Directory dir = null;
    try {
      dir = FSDirectory.open(indexDirFile);
      IndexReader indexReader  = DirectoryReader.open(dir);
      this.searcher = new IndexSearcher(indexReader);
      this.parser = new QueryParser(Version.LUCENE_47,"content", new RussianAnalyzer(Version.LUCENE_47));
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
  @Override
  public String search(String query) {
    try {
      TopDocs top = performSearch(query);
      ScoreDoc[] scores = top.scoreDocs;
      for(ScoreDoc score: scores){

      }
      return scores.toString();
    } catch (IOException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    } catch (ParseException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
    return null;
  }

  @Override
  public List<String> getTerms() {
    List<String> terms_str = new ArrayList<String>();
    File indexDirFile = new File(this.getIndexDir());
    Directory dir = null;
    try {
      dir = FSDirectory.open(indexDirFile);
      IndexReader indexReader  = DirectoryReader.open(dir);
      Terms terms = indexReader.getTermVector(0,"content");
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

  public TopDocs performSearch(String queryString)
          throws IOException, ParseException {
    BooleanQuery query = new BooleanQuery();
    query.add(new TermQuery(new Term("content", queryString)), BooleanClause.Occur.MUST);
    TopDocs hits = searcher.search(query,100);
    return hits;
  }

  public String getIndexDir() {
    return indexDir;
  }

  public void setIndexDir(String indexDir) {
    this.indexDir = indexDir;
  }
}
