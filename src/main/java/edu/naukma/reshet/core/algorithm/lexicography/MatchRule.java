package edu.naukma.reshet.core.algorithm.lexicography;

import com.google.api.client.util.Lists;
import edu.naukma.reshet.model.TermInDoc;
import edu.naukma.reshet.model.Termin;
import org.apache.commons.lang.StringUtils;
import org.languagetool.AnalyzedSentence;
import org.languagetool.AnalyzedToken;
import org.languagetool.AnalyzedTokenReadings;
import org.languagetool.JLanguageTool;
import org.languagetool.language.Ukrainian;
import org.languagetool.synthesis.uk.UkrainianSynthesizer;

import java.io.IOException;
import java.util.List;

import static edu.naukma.reshet.core.algorithm.lexicography.POSTag.*;

public class MatchRule {

    private final POSTag [] tags;
    private JLanguageTool janguageTool;
    private int baseWord = 0;
    public MatchRule(int baseWord, POSTag ... tag) {
        tags = tag;
        this.baseWord = baseWord;
        try {
           this.janguageTool = new JLanguageTool(new Ukrainian());
           this.janguageTool.activateDefaultPatternRules();
        } catch (IOException e) {
           e.printStackTrace();
        }
    }

    private boolean matchPOS(POSTag tag, AnalyzedTokenReadings token){
       switch(tag){
           case NOUN: return token.hasPartialPosTag("noun");
           case ADJ: return token.hasPartialPosTag("adj");
           default: return false;
       }
    }

    private POSTag getPOS(AnalyzedTokenReadings token){
        if(token.hasPartialPosTag("noun")){
            return POSTag.NOUN;
        }
        if(token.hasPartialPosTag("adj")){
            return POSTag.ADJ;
        }

        return POSTag.NOUN;
    }

    private String adjustNounPhrase(AnalyzedTokenReadings token, AnalyzedToken baseToken){
        UkrainianSynthesizer synt = new UkrainianSynthesizer();
        if (token.getAnalyzedToken(0).equals(baseToken)) {
            return baseToken.getLemma();
        }
        try {
            POSTag tag = getPOS(token);
            switch (tag){
                case NOUN: return token.getAnalyzedToken(0).getToken();
                case ADJ:
                    String [] tagsBase = baseToken.getPOSTag().split(":");
                    String [] tagsToken = token.getAnalyzedToken(0).getPOSTag().split(":");
                    // Тільки рід. Відмінок називний
                    tagsToken[1] = tagsBase[1];
                    //tagsToken[2] = tagsBase[2];
                    String resultTag = StringUtils.join(tagsToken,":");
                    String [] syntArr = synt.synthesize(token.getAnalyzedToken(0), resultTag);
                    return syntArr[0];
                default: return token.getAnalyzedToken(0).getToken();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private boolean matchPOSSequence(int start, AnalyzedTokenReadings [] tokens){
       int hasMatched = 0;
       for(POSTag tag: tags){
           if((start + hasMatched > tokens.length - 1) || !matchPOS(tag, tokens[start+hasMatched])){
               return false;
           }
           hasMatched++;
       }
       return true;
    }

    public List<NounPhrase> match(String phrase) {
        List<NounPhrase> matches = Lists.newArrayList();
        try {
            List<AnalyzedSentence> analyzed = janguageTool.analyzeText(phrase);
            AnalyzedSentence sentence = analyzed.get(0);
            AnalyzedTokenReadings [] tokens = sentence.getTokensWithoutWhitespace();
            for(int i = 0; i < tokens.length; i++){
                if (matchPOSSequence(i, tokens)){
                    List<String> normalized = Lists.newArrayList();
                    for(int j = 0; j < tags.length; j++){
                        String adjusted = adjustNounPhrase(tokens[i + j], tokens[i + baseWord].getAnalyzedToken(0));
                        normalized.add(adjusted);
                    }
                    matches.add(new NounPhrase(StringUtils.join(normalized, " ")));
                    i += (tags.length - 1);
                }
            }
            } catch (IOException e) {
            e.printStackTrace();
        }

        return matches;
    }
    public List<NounPhraseMatch> matchWithPositions(String phrase) {
        List<NounPhraseMatch> matches = Lists.newArrayList();
        try {
            List<AnalyzedSentence> analyzed = janguageTool.analyzeText(phrase);
            AnalyzedSentence sentence = analyzed.get(0);
            AnalyzedTokenReadings [] tokens = sentence.getTokensWithoutWhitespace();
            for(int i = 0; i < tokens.length; i++){
                if (matchPOSSequence(i, tokens)){
                    List<String> normalized = Lists.newArrayList();
                    for(int j = 0; j < tags.length; j++){
                        String adjusted = adjustNounPhrase(tokens[i + j], tokens[i + baseWord].getAnalyzedToken(0));
                        normalized.add(adjusted);
                    }
                    matches.add(new NounPhraseMatch(new NounPhrase(StringUtils.join(normalized, " ")), i));
                    i += (tags.length - 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return matches;
    }
}
