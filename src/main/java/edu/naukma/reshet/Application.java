package edu.naukma.reshet;

import edu.naukma.reshet.configuration.MongoConfiguration;
import eu.hlavki.text.lemmagen.LemmatizerFactory;
import eu.hlavki.text.lemmagen.api.Lemmatizer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.MultiPartConfigFactory;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

import javax.servlet.MultipartConfigElement;
import java.io.IOException;

@Configuration
@PropertySource(value = "classpath:prod.properties")
@Import({MongoConfiguration.class,RepositoryRestMvcConfiguration.class})
@ComponentScan
@EnableAutoConfiguration
public class Application {
  @Bean
  public static PropertySourcesPlaceholderConfigurer getConfigurator(){
    return new PropertySourcesPlaceholderConfigurer();
  }
  @Bean
  MultipartConfigElement multipartConfigElement() {
    MultiPartConfigFactory factory = new MultiPartConfigFactory();
    factory.setMaxFileSize("24MB");
    factory.setMaxRequestSize("24MB");
    return factory.createMultipartConfig();
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
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}