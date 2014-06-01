package edu.naukma.reshet;

//import edu.naukma.reshet.configuration.IntegrationConfig;
import edu.naukma.reshet.configuration.MongoConfiguration;
import edu.naukma.reshet.core.help.GoogleCachedCrawler;
import edu.naukma.reshet.core.help.GoogleCrawler;
import edu.naukma.reshet.core.SimpleTextSearcher;
import edu.naukma.reshet.shared.Searcher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: reshet
 * Date: 3/4/14
 * Time: 11:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class CalcTextDocFreqMain {
  public static void main(String args[]){
    AnnotationConfigApplicationContext ctx =
            new AnnotationConfigApplicationContext();

    ctx.register(MongoConfiguration.class);
    //ctx.register(IntegrationConfig.class);
    ctx.refresh();


    GoogleCrawler crawler = ctx.getBean(GoogleCachedCrawler.class);
    Searcher searcher = ctx.getBean(SimpleTextSearcher.class);
    Map<String, Integer> freq = searcher.getFrequencies();
    Random rd = new Random();
    for(String term: freq.keySet()){
      Integer frequency =  freq.get(term);
      System.out.println(term + ":" +frequency);
      Integer docFreq = rd.nextInt(20000000);
      Double totalFreq = 1.0*frequency/docFreq;
        System.out.println(totalFreq);
    }
    //Long docFreq = crawler.getDocumentaryFrequency("генеологія");
    //System.out.println(docFreq);
    System.out.println("Master Thesis Ihor Reshetnev work 888");
  }
}
