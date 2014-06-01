package integration;

import edu.naukma.reshet.configuration.BaseTestConfig;
import edu.naukma.reshet.core.algorithm.TopTfIdfInitialTerminologyNounExtractor;
import edu.naukma.reshet.model.TermInDoc;
import edu.naukma.reshet.model.Termin;
import edu.naukma.reshet.repositories.TerminRepository;
import edu.naukma.reshet.shared.Searcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@Configuration
@Import(BaseTestConfig.class)
@ContextConfiguration(classes = NounExtractorIntegrationTest.class)
public class NounExtractorIntegrationTest {

    @Autowired
    TerminRepository terminRepo;

    @Autowired
    @Qualifier("noun")
    TopTfIdfInitialTerminologyNounExtractor extractor = new TopTfIdfInitialTerminologyNounExtractor();

    @Test
    public void noun_extractor_integration_test(){
        Searcher searcher = mock(Searcher.class);
        Map<String, Integer> termFrequencies = new LinkedHashMap<>();
        termFrequencies.put("і",1);
        termFrequencies.put("або",1);
        termFrequencies.put("термін",1);
        termFrequencies.put("соціологія",1);
        termFrequencies.put("яскравий",1);

        when(searcher.getFrequencies(anyInt())).thenReturn(termFrequencies);
        List<TermInDoc> terms = extractor.extractValuableTerms(searcher, 0);
        assertEquals("Should be 2 noun terms in list", terms.size(), 2);
        assertEquals("First noun term", terms.get(0).getTermin().getText(), "термін");
        assertEquals("Second noun term", terms.get(1).getTermin().getText(), "соціологія");
    }
    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        initTermsDatabase();
    }

    private void initTermsDatabase(){
       terminRepo.save(new Termin("термін",100000D));
       terminRepo.save(new Termin("соціологія",100000D));
    }
}


