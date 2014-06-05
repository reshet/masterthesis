package edu.naukma.runnable.single;

import edu.naukma.reshet.configuration.MongoConfiguration;
import edu.naukma.reshet.core.*;
import edu.naukma.reshet.core.algorithm.RelationFinder;
import edu.naukma.reshet.core.algorithm.SnippetsFinder;
import edu.naukma.reshet.model.Snippet;
import edu.naukma.reshet.model.TermInDoc;
import edu.naukma.reshet.model.TermRelation;
import edu.naukma.reshet.repositories.TermInDocRepository;
import edu.naukma.reshet.repositories.TermRelationRepository;
import edu.naukma.reshet.shared.*;
import eu.hlavki.text.lemmagen.LemmatizerFactory;
import eu.hlavki.text.lemmagen.api.Lemmatizer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Configuration
@EnableMongoRepositories
@ComponentScan(basePackages = {"edu.naukma.reshet.core"})
public class SingleDocumentSecondTourAssociation {
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
  public Searcher2 configureSearcher(){
    return new AdvancedTextSearcher("/home/reshet/masterthesis/index2/");
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

//  @Bean
//  public DocumentaryFrequencyCrawler crawler(){
//    return new GoogleCachedCrawler();
//  }

  public static void main(String args[]){
    System.out.println("Single document Second step load terms application");
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
    ctx.register(MongoConfiguration.class);
    ctx.register(SingleDocumentSecondTourAssociation.class);
    ctx.refresh();

    //InitialTerminologyExtract extractor = ctx.getBean(InitialTerminologyExtract.class);
    TermInDocRepository repo = ctx.getBean(TermInDocRepository.class);
    SnippetsFinder finder = ctx.getBean(SnippetsFinder.class);
    RelationFinder relFinder = ctx.getBean(RelationFinder.class);
    Lemmatizer lm = ctx.getBean(Lemmatizer.class);
    Searcher2 searcher = ctx.getBean(Searcher2.class);
    finder.setLm(lm);
    finder.setSearcher(searcher);
    relFinder.setLm(lm);
    List<TermInDoc> terms = repo.findAll();
    List<Snippet> snippets = finder.findSnippets(terms);
    Set<TermRelation> relations = relFinder.getRelations(terms,snippets);
    for(TermRelation relation:relations){
      System.out.println(relation);
    }
    TermRelationRepository repoRel = ctx.getBean(TermRelationRepository.class);
    repoRel.save(relations);
  }

}
