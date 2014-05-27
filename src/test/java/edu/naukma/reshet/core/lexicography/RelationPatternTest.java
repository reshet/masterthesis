package edu.naukma.reshet.core.lexicography;

import edu.naukma.reshet.core.algorithm.lexicography.MatchRule;
import edu.naukma.reshet.core.algorithm.lexicography.NounPhraseMatch;
import edu.naukma.reshet.core.algorithm.lexicography.POSTag;
import edu.naukma.reshet.core.algorithm.lexicography.pattern.ExactWordElement;
import edu.naukma.reshet.core.algorithm.lexicography.pattern.MatchPattern;
import edu.naukma.reshet.core.algorithm.lexicography.pattern.NounPhraseElement;
import edu.naukma.reshet.model.TermRelation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static junit.framework.Assert.assertEquals;
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
           new NounPhraseElement(false, new MatchRule(0,POSTag.NOUN))
        );
        assertTrue(pattern.mathes(sentence));
    }
    @Test
    public void dash_definition_first_match_test(){
        String sentence = "Соціологічне дослідження — різновид дослідження.";
        MatchPattern pattern = new MatchPattern(
                new NounPhraseElement(true, new MatchRule(1, POSTag.ADJ, POSTag.NOUN), new MatchRule(0, POSTag.NOUN)),
                new ExactWordElement("—"),
                new NounPhraseElement(false, new MatchRule(0,POSTag.NOUN))
        );
        assertTrue(pattern.mathes(sentence));
        List<NounPhraseMatch> match = pattern.matchFirst(sentence);
        assertEquals(match.get(0).getPhrase().getText(), "соціологічне дослідження");
        assertEquals(match.get(2).getPhrase().getText(), "різновид");

    }
    @Test
    public void dash_definition_first_match_nn_test(){
        String sentence = "Соціологічне дослідження — різновид дослідження.";
        MatchPattern pattern = new MatchPattern(
                new NounPhraseElement(true, new MatchRule(1, POSTag.ADJ, POSTag.NOUN), new MatchRule(0, POSTag.NOUN)),
                new ExactWordElement("—"),
                new NounPhraseElement(false, new MatchRule(0,POSTag.NOUN,POSTag.NOUN))
        );
        assertTrue(pattern.mathes(sentence));
        List<NounPhraseMatch> match = pattern.matchFirst(sentence);
        assertEquals(match.get(0).getPhrase().getText(), "соціологічне дослідження");
        assertEquals(match.get(2).getPhrase().getText(), "різновид дослідження");
    }
    @Test
    public void dash_definition_first_match_relations_test(){
        String sentence = "Соціологічне дослідження — різновид дослідження.";
        MatchPattern pattern = new MatchPattern(
                new NounPhraseElement(true, new MatchRule(1, POSTag.ADJ, POSTag.NOUN), new MatchRule(0, POSTag.NOUN)),
                new ExactWordElement("—"),
                new NounPhraseElement(false, new MatchRule(0,POSTag.NOUN,POSTag.NOUN))
        );
        assertTrue(pattern.mathes(sentence));
        List<NounPhraseMatch> match = pattern.matchFirst(sentence);
        List<TermRelation> relations = pattern.getRelations(match);
        System.out.println(relations);
        //assertEquals(match.get(0).getPhrase().getText(), "соціологічне дослідження");
        //assertEquals(match.get(2).getPhrase().getText(), "різновид дослідження");
    }
}
