package edu.naukma.reshet.core;

import edu.naukma.reshet.shared.Indexer;
import edu.naukma.reshet.shared.TextExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 * User: reshet
 * Date: 3/16/14
 * Time: 9:02 PM
 * To change this template use File | Settings | File Templates.
 */
@Configuration
@ComponentScan("edu.naukma.reshet.core")
class CoreConfig {
  @Bean
  public Indexer configureIndexer() {
    return new ConcreteIndexer();
  }
  @Bean
  public TextExtractor configureExtractor(){
    return new PdfFileExtractor("sample.pdf");
  }
}
