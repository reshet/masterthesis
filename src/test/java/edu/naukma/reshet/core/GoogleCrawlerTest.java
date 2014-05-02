package edu.naukma.reshet.core;

import edu.naukma.reshet.shared.DocumentaryFrequencyCrawler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CoreConfig.class)
public class GoogleCrawlerTest {

   @Autowired
   DocumentaryFrequencyCrawler crawler;
   @Test
   public void crawl_doc_freq_test(){
      assertEquals(new Long(22L),crawler.getDocumentaryFrequency("some"));
   }

}


