package integration;

import edu.naukma.reshet.core.GoogleCrawler;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MongoConfiguration.class,IntegrationConfig.class}, loader = AnnotationConfigContextLoader.class)
@Ignore
public class CachedCrawlerTest {
  @Autowired
  @Qualifier(value = "cached")
  GoogleCrawler crawler;

  @Test
  public void search_pdf_document_test(){
    crawler.getDocumentaryFrequency("генеологія");
    assertEquals("Saved term to cache", 0, 0);
  }
}
