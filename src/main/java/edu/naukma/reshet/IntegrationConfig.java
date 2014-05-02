package edu.naukma.reshet;

import edu.naukma.reshet.core.ConcreteIndexer;
import edu.naukma.reshet.core.MongoCacheImpl;
import edu.naukma.reshet.core.PdfFileExtractor;
import edu.naukma.reshet.core.SimpleTextSearcher;
import edu.naukma.reshet.shared.Indexer;
import edu.naukma.reshet.shared.MongoCache;
import edu.naukma.reshet.shared.Searcher;
import edu.naukma.reshet.shared.TextExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

/**
 * Created with IntelliJ IDEA.
 * User: reshet
 * Date: 3/16/14
 * Time: 9:02 PM
 * To change this template use File | Settings | File Templates.
 */
@Configuration
@ContextConfiguration
class IntegrationConfig {
  @Bean
  public Indexer configureIndexer() {
    ConcreteIndexer indexer = new ConcreteIndexer();
    indexer.setDirectory("/home/reshet/masterthesis/index/");
    return indexer;
  }
  @Bean
  public TextExtractor configureExtractor(){
    return new PdfFileExtractor("/home/reshet/masterthesis/src/test/resources/sample.pdf");
  }
  @Bean
  public Searcher configureSearcher(){
    return new SimpleTextSearcher("/home/reshet/masterthesis/index/");
  }

  @Bean
  public MongoCache mongoCacher(){
    return new MongoCacheImpl();
  }

}
