package edu.naukma.reshet.core.lexicography;

import com.google.common.collect.Lists;
import edu.naukma.reshet.core.algorithm.lexicography.MatchRule;
import edu.naukma.reshet.core.algorithm.lexicography.POSTag;
import edu.naukma.reshet.model.TermInDoc;
import edu.naukma.reshet.model.Termin;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@Configuration
@ContextConfiguration(classes = NounPhraseExtractTest.class)
public class NounPhraseExtractTest {
    @Test
    public void simple_noun_match_test(){
       String phrase = "Соціологія — наука про умови та процеси у суспільстві, а також їх дослідження";
        MatchRule rule = new MatchRule(POSTag.NOUN);
        List<TermInDoc> nouns = rule.match(phrase);
        List<TermInDoc> expected = Lists.newArrayList(
            new TermInDoc(new Termin("cоціологія",1L),1.0),
            new TermInDoc(new Termin("наука",1L),1.0),
            new TermInDoc(new Termin("умова",1L),1.0),
            new TermInDoc(new Termin("процес",1L),1.0),
            new TermInDoc(new Termin("суспільство",1L),1.0),
            new TermInDoc(new Termin("дослідження",1L),1.0)
        );

        assertEquals(nouns, expected);
    }
}
