package edu.naukma.reshet.core.algorithm.lexicography.pattern;

import com.carrotsearch.ant.tasks.junit4.dependencies.com.google.common.collect.Lists;
import edu.naukma.reshet.core.algorithm.lexicography.NounPhrase;
import edu.naukma.reshet.core.algorithm.lexicography.NounPhraseMatch;
import org.languagetool.AnalyzedSentence;
import org.languagetool.AnalyzedTokenReadings;
import org.languagetool.JLanguageTool;
import org.languagetool.language.Ukrainian;

import java.io.IOException;
import java.util.List;

public class ExactWordElement implements PatternElement{
    private final String [] synonyms;
    private JLanguageTool jLanguageTool;
    public ExactWordElement(String... synonyms) {
        this.synonyms = synonyms;
        try {
            this.jLanguageTool = new JLanguageTool(new Ukrainian());
            this.jLanguageTool.activateDefaultPatternRules();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<NounPhraseMatch> apply(String subphrase) {
        List<NounPhraseMatch> list = Lists.newArrayList();
        try {
            AnalyzedSentence sentence = jLanguageTool.getAnalyzedSentence(subphrase);
            AnalyzedTokenReadings [] tokens = sentence.getTokensWithoutWhitespace();
            int index = 0;
            for(AnalyzedTokenReadings token:tokens){
                for(String syn: synonyms){
                    if((token.getAnalyzedToken(0).getLemma() == null
                            && token.getAnalyzedToken(0).getToken().equals(syn))
                        || (token.getAnalyzedToken(0).getLemma() != null
                            && token.getAnalyzedToken(0).getLemma().equals(syn))){
                        list.add(new NounPhraseMatch(new NounPhrase(syn), index));
                    }
                }
                index++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
