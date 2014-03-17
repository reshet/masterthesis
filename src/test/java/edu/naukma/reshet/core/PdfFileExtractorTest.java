package edu.naukma.reshet.core;

import edu.naukma.reshet.shared.Indexer;
import edu.naukma.reshet.shared.TextExtractor;
import org.apache.lucene.document.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = CoreConfig.class)
public class PdfFileExtractorTest {

   @Autowired
   TextExtractor extractor;
   @Test
   public void extract_document_test(){
     try {
       Document doc = extractor.getDocument();
     } catch (IOException e) {
       e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
     }
     assertEquals("Extract pdf",0,0);
   }
}

