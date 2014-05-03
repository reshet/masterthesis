package edu.naukma.reshet;

import edu.naukma.reshet.core.*;
import edu.naukma.reshet.shared.Searcher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: reshet
 * Date: 3/4/14
 * Time: 11:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class MainUkrLemmatizer {
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
  public static void main(String args[]){
    AnnotationConfigApplicationContext ctx =
            new AnnotationConfigApplicationContext();

    ctx.register(MongoConfiguration.class);
    ctx.register(IntegrationConfig.class);
    ctx.refresh();

    //GoogleCrawler crawler = ctx.getBean(GoogleCachedCrawler.class);
    //Long docFreq = crawler.getDocumentaryFrequency("генеологія");
    //System.out.println(docFreq);

    //indexSampleDoc();

    SimpleTextSearcher searcher = new SimpleTextSearcher();
    searcher.setIndexDir("/home/reshet/masterthesis/index2/");
    Map<String, Integer> map = searcher.getFrequencies();
    System.out.println(map);
    System.out.println("Master Thesis Ihor Reshetnev work 888");
  }
}
