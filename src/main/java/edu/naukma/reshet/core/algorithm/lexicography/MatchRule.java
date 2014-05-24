package edu.naukma.reshet.core.algorithm.lexicography;

import com.google.api.client.util.Lists;
import edu.naukma.reshet.model.TermInDoc;
import edu.naukma.reshet.model.Termin;
import org.languagetool.AnalyzedSentence;
import org.languagetool.AnalyzedTokenReadings;
import org.languagetool.JLanguageTool;
import org.languagetool.language.Ukrainian;

import java.io.IOException;
import java.util.List;

public class MatchRule {

    private final POSTag [] tags;
    private JLanguageTool janguageTool;
    public MatchRule(POSTag ... tag) {
        tags = tag;
        try {
           this.janguageTool = new JLanguageTool(new Ukrainian());
           this.janguageTool.activateDefaultPatternRules();
        } catch (IOException e) {
           e.printStackTrace();
        }
    }

    public List<TermInDoc> match(String phrase) {
        List<TermInDoc> matches = Lists.newArrayList();
        try {
            List<AnalyzedSentence> analyzed = janguageTool.analyzeText(phrase);
            AnalyzedSentence sentence = analyzed.get(0);
            for(AnalyzedTokenReadings token: sentence.getTokensWithoutWhitespace()){
                if (token.hasPartialPosTag("noun")){
                    String normalized = token.getAnalyzedToken(0).getLemma();
                    matches.add(new TermInDoc(new Termin(normalized, 1L),1.0));
                }
            }
            } catch (IOException e) {
            e.printStackTrace();
        }

        return matches;
    }
}
