package edu.naukma.reshet.configuration;

import com.mongodb.Mongo;
import com.mongodb.MongoURI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@Profile("default")
@EnableMongoRepositories(basePackages = "edu.naukma.reshet.repositories")
public class MongoConfiguration extends AbstractMongoConfiguration {
//  @Value("${mongourl}")
//  private String url;

  @Value("${mongodb}")
  private String databaseName;

  @Override
  protected String getDatabaseName() {
    return databaseName;
  }

  @Override
  public Mongo mongo() throws Exception {
    return new Mongo();
  }

  @Override
  protected String getMappingBasePackage() {
    return "edu.naukma.reshet.model";
  }
}