package edu.naukma.reshet.core.lexicography;

import edu.naukma.reshet.core.algorithm.lexicography.MatchRule;
import edu.naukma.reshet.core.algorithm.lexicography.POSTag;
import edu.naukma.reshet.core.algorithm.lexicography.pattern.AnySubstringElement;
import edu.naukma.reshet.core.algorithm.lexicography.pattern.ExactWordElement;
import edu.naukma.reshet.core.algorithm.lexicography.pattern.MatchPattern;
import edu.naukma.reshet.core.algorithm.lexicography.pattern.NounPhraseElement;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@Configuration
@ContextConfiguration(classes = NounPhraseExtractTest.class)
public class RelationPatternTest {
    @Test
    public void dash_definition_test(){
        String sentence = "Соціологічне дослідження — різновид дослідження.";
        MatchPattern pattern = new MatchPattern(
           new NounPhraseElement(true, new MatchRule(1, POSTag.ADJ, POSTag.NOUN), new MatchRule(0, POSTag.NOUN)),
           new ExactWordElement("—"),
           new AnySubstringElement(),
           new NounPhraseElement(false, new MatchRule(0,POSTag.NOUN))
        );
        assertTrue(pattern.mathes(sentence));
    }
}
