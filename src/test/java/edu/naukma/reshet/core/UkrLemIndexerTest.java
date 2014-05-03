package edu.naukma.reshet.core;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CoreConfig.class)
public class UkrLemIndexerTest {
   @Test
   public void index_document_test(){
      UkrLemmatizedIndexer indexer = new UkrLemmatizedIndexer();
      indexer.setDirectory("/home/reshet/masterthesis/index2/");
      assertEquals("Indexed documents number","Indexed 212 documents",indexer.indexDocuments());
   }
}


