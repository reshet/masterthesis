package edu.naukma.reshet.core.algorithm.lexicography.pattern;

import com.carrotsearch.ant.tasks.junit4.dependencies.com.google.common.collect.Lists;
import edu.naukma.reshet.core.algorithm.lexicography.MatchRule;
import edu.naukma.reshet.core.algorithm.lexicography.NounPhraseMatch;

import java.util.List;

public class IterationElement implements PatternElement{
    private final PatternElement [] repeatedElementSequence;

    public IterationElement(PatternElement... repeatedElementSequence) {
        this.repeatedElementSequence = repeatedElementSequence;
    }
    @Override
    public List<NounPhraseMatch> apply(String subphrase) {
        List<List<NounPhraseMatch>> allMatches = Lists.newLinkedList();
        MatchPattern pattern = new MatchPattern(repeatedElementSequence);
        List<NounPhraseMatch> currentLongestMatches = pattern.matchFirst(subphrase);
        if(currentLongestMatches.size()>0){
            allMatches.add(currentLongestMatches);
            int mult = 2;
            while(currentLongestMatches.size()>0){
                PatternElement [] iteration = expandIteration(mult, repeatedElementSequence);
                MatchPattern patternIter = new MatchPattern(iteration);
                currentLongestMatches = patternIter.matchFirst(subphrase);
                mult++;
            }
        }
        return mergeToLongestMatches(allMatches);
    }

    private PatternElement [] expandIteration(int mult, PatternElement [] elems){
        int iterLen = elems.length;
        PatternElement [] iteration = new PatternElement[mult*iterLen];
        for(int j = 0; j < mult * iterLen; j+=iterLen){
            for(int i = 0; i < iterLen; i++){
                iteration[j+i] = elems[i];
            }
        }
        return iteration;
    }
    private List<NounPhraseMatch> mergeToLongestMatches(List<List<NounPhraseMatch>> allMatches){

    }
}
