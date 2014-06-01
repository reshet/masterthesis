package edu.naukma.reshet.integration.single;

import com.github.fakemongo.impl.index.IndexFactory;
import edu.naukma.reshet.configuration.MongoConfiguration;
import edu.naukma.reshet.core.GoogleCachedCrawler;
import edu.naukma.reshet.core.PdfDirectoryIndexer;
import edu.naukma.reshet.core.PdfFileExtractor;
import edu.naukma.reshet.core.SimpleTextSearcher;
import edu.naukma.reshet.core.UkrLemmatizedIndexer;
import edu.naukma.reshet.model.TermInDoc;
import edu.naukma.reshet.repositories.TermInDocRepository;
import edu.naukma.reshet.shared.DocumentaryFrequencyCrawler;
import edu.naukma.reshet.shared.IndexManager;
import edu.naukma.reshet.shared.Indexer;
import edu.naukma.reshet.shared.Searcher;
import edu.naukma.reshet.shared.TextExtractor;
import edu.naukma.reshet.shared.algorithm.InitialTerminologyExtract;
import eu.hlavki.text.lemmagen.LemmatizerFactory;
import eu.hlavki.text.lemmagen.api.Lemmatizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.util.List;

@Configuration
@PropertySource(value = "classpath:init.properties")
@Import(MongoConfiguration.class)
@ContextConfiguration(classes = IndexReferenceCollectionApplication.class)
@ComponentScan(basePackages = {"edu.naukma.reshet.core"})
public class IndexReferenceCollectionApplication {
    static String path = "/Users/user/naukma/";
    @Bean
    public static PropertySourcesPlaceholderConfigurer getConfigurator(){
        return new PropertySourcesPlaceholderConfigurer();
    }
    @Bean
    public static PdfDirectoryIndexer indexer(){
        return new PdfDirectoryIndexer(path + "pdfs/", path + "index/", "science");
    }

    public static void main(String args[]){
      System.out.println("Reference collection documentary frequency builder application");
      PdfDirectoryIndexer indexer = indexer();
      indexer.indexDirectoryWithPdfs();

    }
}
