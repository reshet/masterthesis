package edu.naukma.reshet.core.algorithm.lexicography.pattern;

import com.carrotsearch.ant.tasks.junit4.dependencies.com.google.common.collect.Lists;
import edu.naukma.reshet.core.algorithm.lexicography.MatchRule;
import edu.naukma.reshet.core.algorithm.lexicography.NounPhrase;
import edu.naukma.reshet.core.algorithm.lexicography.NounPhraseMatch;

import java.util.List;

/**
 * Created by user on 5/25/14.
 */
public class NounPhraseElement implements PatternElement{
    private final MatchRule [] orderedRuleSet;
    private final boolean mainPhrase;

    public NounPhraseElement(boolean mainPhrase, MatchRule... orderedRuleSet) {
        this.orderedRuleSet = orderedRuleSet;
        this.mainPhrase = mainPhrase;
    }
    //Як секвентація ДНК: Розбили на всі можливі, потім встановлюємо порядок.
    @Override
    public List<NounPhraseMatch> apply(String subphrase) {
        List<NounPhraseMatch> allMatches = Lists.newLinkedList();
        for(MatchRule rule: orderedRuleSet){
            List<NounPhraseMatch> phrases = rule.matchWithPositions(subphrase);
            allMatches.addAll(phrases);
        }
        return allMatches;
    }

    public boolean isMainPhrase() {
        return mainPhrase;
    }
}
