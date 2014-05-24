package integration;

import edu.naukma.reshet.shared.Indexer;
import edu.naukma.reshet.shared.Searcher;
import edu.naukma.reshet.shared.TextExtractor;
import org.junit.Ignore;
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
@ContextConfiguration(classes = IntegrationConfig.class)
@Ignore
public class ConcreteIndexerIntegrationTest {
   @Autowired
   Indexer indexer;
   @Autowired
   TextExtractor extractor;
   @Autowired
   Searcher searcher;

   @Test
   public void index_pdf_document_test(){
     try {
       indexer.indexDocument(extractor.getDocument());
     } catch (IOException e) {
       e.printStackTrace();
     }
     assertEquals("Indexed documents real", 0, 0);
   }
    @Test
    public void search_pdf_document_test(){
      System.out.println(searcher.search("граф"));
      assertEquals("Search for graph in doc", 0, 0);
    }

  @Test
  public void pdf_document_get_terms_test(){
    List<String> list =  searcher.getTerms();
    System.out.println(list);
    assertEquals("Terms in doc", 0, 0);
  }
}


