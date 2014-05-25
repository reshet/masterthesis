package edu.naukma.reshet.core.lexicography;

import com.google.common.collect.Lists;
import edu.naukma.reshet.core.algorithm.lexicography.MatchRule;
import edu.naukma.reshet.core.algorithm.lexicography.NounPhrase;
import edu.naukma.reshet.core.algorithm.lexicography.POSTag;
import edu.naukma.reshet.model.TermInDoc;
import edu.naukma.reshet.model.Termin;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;


@RunWith(SpringJUnit4ClassRunner.class)
@Configuration
@ContextConfiguration(classes = NounPhraseExtractTest.class)
public class NounPhraseExtractTest {
    @Test
    public void simple_noun_match_test(){
       String phrase = "Соціологія — наука про умови та процеси у суспільстві, а також їх дослідження";
        MatchRule rule = new MatchRule(0, POSTag.NOUN);
        List<NounPhrase> nouns = rule.match(phrase);
        List<NounPhrase> expected = Lists.newArrayList(
            new NounPhrase("соціологія"),
            new NounPhrase("наука"),
            new NounPhrase("умова"),
            new NounPhrase("процес"),
            new NounPhrase("суспільство"),
            new NounPhrase("дослідження")
        );
        //Assert.assertArrayEquals(nouns.toArray(), expected.toArray());
        assertEquals("Found all needed phrases",nouns.size(), expected.size());
        for(int i = 0; i < nouns.size();i++){
            assertEquals(nouns.get(i).getText(), expected.get(i).getText());
        }
    }

    @Test
    public void noun_adj_match_test(){
        String phrase = "Поведінкова соціологія — наука про умови та процеси у суспільстві, а також їх дослідження";
        MatchRule rule = new MatchRule(1, POSTag.ADJ, POSTag.NOUN);
        List<NounPhrase> nouns = rule.match(phrase);
        List<NounPhrase> expected = Lists.newArrayList(
                new NounPhrase("поведінкова соціологія")
        );
        //Assert.assertArrayEquals(nouns.toArray(), expected.toArray());

        assertEquals("Found all needed phrases",nouns.size(), expected.size());
        for(int i = 0; i < nouns.size();i++){
            assertEquals(nouns.get(i).getText(), expected.get(i).getText());
        }
    }
    @Test
    public void noun_adj_undirect_match_test(){
        String phrase = "Поведінкові соціології — наука про умови та процеси у суспільстві, а також їх дослідження";
        MatchRule rule = new MatchRule(1, POSTag.ADJ, POSTag.NOUN);
        List<NounPhrase> nouns = rule.match(phrase);
        List<NounPhrase> expected = Lists.newArrayList(
                new NounPhrase("поведінкова соціологія")
        );
        assertEquals("Found all needed phrases",nouns.size(), expected.size());
        for(int i = 0; i < nouns.size();i++){
            assertEquals(nouns.get(i).getText(), expected.get(i).getText());
        }
    }
    @Test
    public void noun_adj_undirect_multiple_match_test(){
        String phrase = "Поведінкові соціології — наука про умови та процеси у суспільстві, а також їх дослідження, a також кількісні дослідження";
        MatchRule rule = new MatchRule(1, POSTag.ADJ, POSTag.NOUN);
        List<NounPhrase> nouns = rule.match(phrase);
        List<NounPhrase> expected = Lists.newArrayList(
                new NounPhrase("поведінкова соціологія"),
                new NounPhrase("кількісне дослідження")
        );
        assertEquals("Found all needed phrases",nouns.size(), expected.size());
        for(int i = 0; i < nouns.size();i++){
            assertEquals(nouns.get(i).getText(), expected.get(i).getText());
        }
    }
}
