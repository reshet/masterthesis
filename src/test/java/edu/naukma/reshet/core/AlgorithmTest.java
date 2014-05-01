package edu.naukma.reshet.core;

import edu.naukma.reshet.shared.Indexer;
import edu.naukma.reshet.shared.TextExtractor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = CoreConfig.class)
public class AlgorithmTest {

   @Autowired
   Indexer indexer;
   @Autowired
   TextExtractor extractor;

   @Test
   public void algorithm_builder_test(){
      Algorithm alg = Algorithm.builder()
              .withIndexer(indexer)
              .withTextExtractor(extractor)
              .build();
      alg.run();
      assertEquals("Algorithm created",0,0);
   }
}


