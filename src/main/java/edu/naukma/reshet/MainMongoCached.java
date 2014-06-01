package edu.naukma.reshet;

//import edu.naukma.reshet.configuration.IntegrationConfig;
import edu.naukma.reshet.configuration.MongoConfiguration;
import edu.naukma.reshet.core.help.GoogleCachedCrawler;
import edu.naukma.reshet.core.help.GoogleCrawler;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: reshet
 * Date: 3/4/14
 * Time: 11:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class MainMongoCached {
  public static void main(String args[]){
    AnnotationConfigApplicationContext ctx =
            new AnnotationConfigApplicationContext();

    ctx.register(MongoConfiguration.class);
    //ctx.register(IntegrationConfig.class);
    ctx.refresh();

    GoogleCrawler crawler = ctx.getBean(GoogleCachedCrawler.class);
    Double docFreq = crawler.getDocumentaryFrequency("генеологія");
    System.out.println(docFreq);
    System.out.println("Master Thesis Ihor Reshetnev work 888");
  }
}
