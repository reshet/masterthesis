package integration;

import edu.naukma.reshet.configuration.BaseTestConfig;
import edu.naukma.reshet.model.TermInDoc;
import edu.naukma.reshet.model.TermRelation;
import edu.naukma.reshet.model.Termin;
import edu.naukma.reshet.model.dto.RdfGraph;
import edu.naukma.reshet.model.dto.graph.Concept;
import edu.naukma.reshet.model.dto.graph.HierarchicalRelationship;
import edu.naukma.reshet.repositories.TermInDocRepository;
import edu.naukma.reshet.repositories.TermRelationRepository;
import edu.naukma.reshet.repositories.TerminRepository;
import edu.naukma.reshet.resource.RDFController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@Configuration
@ComponentScan(basePackages = {"edu.naukma.reshet.resource", "edu.naukma.reshet.orchestration"})
@Import(BaseTestConfig.class)
@ContextConfiguration(classes = RDFControllerIntegrationTest.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RDFControllerIntegrationTest {

    @Autowired
    TerminRepository terminRepo;
    @Autowired
    TermInDocRepository repoTerms;
    @Autowired
    TermRelationRepository repoRelations;

    @Autowired
    RDFController controller;

    @Test
    public void get_empty_rdf_graph_integration_test(){
        HttpEntity<RdfGraph> graphResponse = controller.getRDF("indexname");
        RdfGraph graph = graphResponse.getBody();
        List<Concept> concepts = graph.getContent().thesaurus.concepts;
        List<HierarchicalRelationship> relations = graph.getContent().thesaurus.relations;
        assertEquals("Empty concepts list",concepts.size(),0);
        assertEquals("Empty relations list",relations.size(),0);
    }

    @Test
    public void get_simple_rdf_graph_integration_test(){
        fillTerminsDatabase();
        HttpEntity<RdfGraph> graphResponse = controller.getRDF("indexname");
        RdfGraph graph = graphResponse.getBody();
        List<Concept> concepts = graph.getContent().thesaurus.concepts;
        List<HierarchicalRelationship> relations = graph.getContent().thesaurus.relations;
        assertEquals("Empty concepts list",concepts.size(),2);
        assertEquals("Empty relations list",relations.size(),1);
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

    private void fillTerminsDatabase(){
        Termin t1 = terminRepo.findByText("термін");
        Termin t2 = terminRepo.findByText("соціологія");
        repoTerms.save(new TermInDoc(t1, 0.2, "science"));
        repoTerms.save(new TermInDoc(t2, 0.2, "science"));
        //TermInDoc term1 = repoTerms.findByTermin(t1);
        //TermInDoc term2 = repoTerms.findByTermin(t2);
        List<TermInDoc> terms = repoTerms.findAll();
        TermInDoc term1 = terms.get(0);
        TermInDoc term2 = terms.get(1);

        repoRelations.save(new TermRelation(
                term1,
                term2,
                "association"
            )
        );
        System.out.println(repoRelations.findAll());
    }
}


