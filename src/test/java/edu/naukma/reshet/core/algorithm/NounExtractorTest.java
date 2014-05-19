package edu.naukma.reshet.core.algorithm;

import edu.naukma.reshet.model.TermInDoc;
import edu.naukma.reshet.model.Termin;
import edu.naukma.reshet.repositories.TerminRepository;
import edu.naukma.reshet.shared.DocumentaryFrequencyCrawler;
import edu.naukma.reshet.shared.Searcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class, loader = SpringApplicationContextLoader.class)
public class NounExtractorTest {
    @Mock
    DocumentaryFrequencyCrawler crawler;

    @Mock
    TerminRepository terminRepo;

    @InjectMocks
    TopTfIdfInitialTerminologyNounExtractor extractor = new TopTfIdfInitialTerminologyNounExtractor();

    @Test
    public void noun_extractor_test(){
        Searcher searcher = mock(Searcher.class);
        Map<String, Integer> termFrequencies = new LinkedHashMap<>();
        termFrequencies.put("і",1);
        termFrequencies.put("або",1);
        termFrequencies.put("термін",1);
        termFrequencies.put("соціологія",1);
        termFrequencies.put("яскравий",1);

        when(searcher.getFrequencies(anyInt())).thenReturn(termFrequencies);
        when(crawler.getDocumentaryFrequency(anyString())).thenReturn(100000L);
        when(terminRepo.findByText(refEq("термін"))).thenReturn(new Termin("термін",1L));
        when(terminRepo.findByText(refEq("соціологія"))).thenReturn(new Termin("соціологія",1L));
        List<TermInDoc> terms = extractor.extractValuableTerms(searcher, 0);
        assertEquals("Should be 2 noun terms in list", terms.size(), 2);
        assertEquals("First noun term", terms.get(0).getTermin().getText(), "термін");
        assertEquals("Second noun term", terms.get(1).getTermin().getText(), "соціологія");
    }
    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }
}

@Configuration
class TestConfig{

}
