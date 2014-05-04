package edu.naukma.reshet;

import com.carrotsearch.ant.tasks.junit4.dependencies.com.google.common.collect.FluentIterable;
import com.carrotsearch.ant.tasks.junit4.dependencies.com.google.common.collect.Lists;
import com.carrotsearch.ant.tasks.junit4.dependencies.com.google.common.collect.Ordering;
import edu.naukma.reshet.core.*;
import edu.naukma.reshet.model.Termin;
import edu.naukma.reshet.model.TerminInDoc;
import edu.naukma.reshet.shared.DocumentaryFrequencyCrawler;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.util.*;


public class MainUkrLemmatizerFreqs {
  public static void indexSampleDoc(){
    UkrLemmatizedIndexer indexer = new UkrLemmatizedIndexer();
    indexer.setDirectory("/home/reshet/masterthesis/index2/");
    PdfFileExtractor extractor = new PdfFileExtractor();
    extractor.setFileName("/home/reshet/masterthesis/src/test/resources/sample2.pdf");
    try {
      indexer.indexDocument(extractor.getDocument());

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  public static Map<String, Integer> findTermFrequenciesOfFirstDocInIndex(){
      SimpleTextSearcher searcher = new SimpleTextSearcher();
      searcher.setIndexDir("/home/reshet/masterthesis/index2/");
      Map<String, Integer> map = searcher.getFrequencies();
      return map;
  }
  public static List<TerminInDoc> getSortedTermsList(Map<String, Integer> map, DocumentaryFrequencyCrawler crawler){
    List<TerminInDoc> termins = Lists.newArrayList();
    for(String term: map.keySet()){
      Integer frequency =  map.get(term);
      Long docFreq = crawler.getDocumentaryFrequency(term);
      Double totalFreq = 1.0*frequency/docFreq;
      termins.add(new TerminInDoc(new Termin(term, docFreq),totalFreq));
    }
    return Ordering
            .natural()
            .reverse()
            .immutableSortedCopy(termins);
  }
  public static void main(String args[]){
    AnnotationConfigApplicationContext ctx =
            new AnnotationConfigApplicationContext();

    ctx.register(MongoConfiguration.class);
    ctx.register(IntegrationConfig.class);
    ctx.refresh();

    GoogleCrawler crawler = ctx.getBean(GoogleCachedCrawler.class);
    Map <String, Integer> map = findTermFrequenciesOfFirstDocInIndex();
    List<TerminInDoc> sortedList = getSortedTermsList(map, crawler);
    for(TerminInDoc term:sortedList){
      System.out.println(term);
    }

    System.out.println("Master Thesis Ihor Reshetnev work 888");
  }
}
