package edu.naukma.reshet.integration.single;

import edu.naukma.reshet.configuration.MongoConfiguration;
import edu.naukma.reshet.core.*;
import edu.naukma.reshet.core.help.GoogleCachedCrawler;
import edu.naukma.reshet.model.TermInDoc;
import edu.naukma.reshet.shared.*;
import edu.naukma.reshet.shared.algorithm.InitialTerminologyExtract;
import eu.hlavki.text.lemmagen.LemmatizerFactory;
import eu.hlavki.text.lemmagen.api.Lemmatizer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.io.IOException;
import java.util.List;

//@Configuration
@EnableMongoRepositories
@ComponentScan(basePackages = {"edu.naukma.reshet.core"})
public class SingleDocumentInitialExtractApplication {
  @Bean
  public Indexer ukrLemmaIndexer() {
    UkrLemmatizedIndexer indexer = new UkrLemmatizedIndexer();
    indexer.setDirectory("/home/reshet/masterthesis/index2/");
    return indexer;
  }
  @Bean
  public TextExtractor configureExtractor(){
    PdfFileExtractor extractor = new PdfFileExtractor();
    extractor.setFileName("/home/reshet/masterthesis/src/test/resources/sample2.pdf");
    return extractor;
  }
  @Bean
  public Searcher configureSearcher(){
    return new SimpleTextSearcher("/home/reshet/masterthesis/index2/", "science");
  }

  @Bean
  public Lemmatizer ukrLemmatizer(){
    try {
      return LemmatizerFactory.getPrebuilt("mlteast-uk");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Bean
  public DocumentaryFrequencyCrawler crawler(){
    return new GoogleCachedCrawler();
  }

  public static void main(String args[]){
    System.out.println("Single document initial term extract application");
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
    ctx.register(MongoConfiguration.class);
    ctx.register(SingleDocumentInitialExtractApplication.class);
    ctx.refresh();

    InitialTerminologyExtract extractor = ctx.getBean(InitialTerminologyExtract.class);
    List<TermInDoc> sortedList = extractor.extractValuableTerms("");
    for(TermInDoc term:sortedList){
      System.out.println(term);
    }
  }

}
