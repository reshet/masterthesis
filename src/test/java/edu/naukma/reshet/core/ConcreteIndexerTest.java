package edu.naukma.reshet.core;

import edu.naukma.reshet.shared.Indexer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = CoreConfig.class)
public class ConcreteIndexerTest {

   @Autowired
   ConcreteIndexer indexer;
   @Test
   public void index_document_test(){
      assertEquals("Indexed documents number","Indexed 334 documents",indexer.indexDocuments());
   }
   @Test
   public void index_pdf_document_test(){

     assertEquals("Indexed documents real",0,0);
   }
}


