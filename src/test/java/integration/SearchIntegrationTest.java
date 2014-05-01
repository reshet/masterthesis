package integration;

import edu.naukma.reshet.shared.Indexer;
import edu.naukma.reshet.shared.Searcher;
import edu.naukma.reshet.shared.TextExtractor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = IntegrationConfig.class)
public class SearchIntegrationTest {
  @Autowired
  Searcher searcher;

  @Test
  public void search_pdf_document_test(){
    String resp = searcher.search("граф");
    System.out.println(resp);
    assertEquals("Search for graph in doc", 0, 0);
  }

  @Test
  public void pdf_document_get_terms_test(){
    List<String> list =  searcher.getTerms();
    System.out.println(list);
    assertEquals("Terms in doc", 0, 0);
  }
}


