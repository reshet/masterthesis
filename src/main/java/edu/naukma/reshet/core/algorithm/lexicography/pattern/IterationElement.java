package edu.naukma.reshet.core.algorithm.lexicography.pattern;


import com.google.common.collect.Lists;
import edu.naukma.reshet.core.algorithm.lexicography.MatchRule;
import edu.naukma.reshet.core.algorithm.lexicography.NounPhrase;
import edu.naukma.reshet.core.algorithm.lexicography.NounPhraseCompoundMatch;
import edu.naukma.reshet.core.algorithm.lexicography.NounPhraseMatch;

import java.util.List;

public class IterationElement implements PatternElement{
    private final PatternElement [] repeatedElementSequence;

    public IterationElement(PatternElement... repeatedElementSequence) {
        this.repeatedElementSequence = repeatedElementSequence;
    }
    @Override
    public List<NounPhraseMatch> apply(String subphrase) {
        List<List<List<NounPhraseMatch>>> allMatches = Lists.newLinkedList();
        List<List<NounPhraseMatch>> currentLongestMatches;
        int mult = 1;
        do{
            PatternElement [] iteration = expandIteration(mult, repeatedElementSequence);
            MatchPattern patternIter = new MatchPattern(iteration);
            currentLongestMatches = patternIter.matchAll(subphrase);
            allMatches.add(currentLongestMatches);
            mult++;
        } while(!currentLongestMatches.isEmpty());

        return mergeToLongestMatches(Lists.reverse(allMatches));
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

    private List<NounPhraseMatch> mergeToLongestMatches(List<List<List<NounPhraseMatch>>> allMatches){
        if(allMatches.isEmpty()) return Lists.newArrayList();
        if(allMatches.size() < 2) return selectLongest(allMatches.get(0));
        for(int i = 1; i < allMatches.size();i++){
           mergeLevel(allMatches.get(i-1), allMatches.get(i));
        }
        return concatenate(allMatches.get(allMatches.size() - 1));
    }
    private List<NounPhraseMatch> concatenate(List<List<NounPhraseMatch>> matches){
       List<NounPhraseMatch> concat = Lists.newLinkedList();
       for(List<NounPhraseMatch> match: matches){
          concat.add(new NounPhraseCompoundMatch(match));
       }
       return concat;
    }
    private List<NounPhraseMatch> selectLongest(List<List<NounPhraseMatch>> matches){
       int currLength = 0;
       int longest = 0;
       if(matches.isEmpty()) return Lists.newArrayList();
       for(int i = 0; i < matches.size();i++){
           if(matches.get(i).size() > currLength){
               currLength = matches.get(i).size();
               longest = i;
           }
       }
       return matches.get(longest);
    }
    private void mergeLevel(List<List<NounPhraseMatch>> from, List<List<NounPhraseMatch>> to){
        // Here assume that on down level matches goes Exactly in the same order as on upper level.
        // Sudden changes of order not taken into account.
        for(List<NounPhraseMatch> fElem: from){
           List<NounPhraseMatch> currentCover = Lists.newLinkedList();
           List<List<NounPhraseMatch>> coveredFully = Lists.newLinkedList();
           int pointer = 0;
           for(List<NounPhraseMatch> tElem: to){
               int totalUsed = 0;
               for(NounPhraseMatch tMatch: tElem){
                  if(fElem.size() > pointer && tMatch.getStartPosition() == fElem.get(pointer).getStartPosition()){
                      currentCover.add(tMatch);
                      pointer++;
                      totalUsed++;
                  }
               }
               if(totalUsed != tElem.size()){
                 pointer = 0;
                 currentCover = Lists.newLinkedList();
               }else{
                 coveredFully.add(tElem);
               }
           }
          if(currentCover.size() == fElem.size()){
             to.removeAll(coveredFully);
          }
        }
        to.addAll(from);
    }

    public PatternElement[] getRepeatedElementSequence() {
        return repeatedElementSequence;
    }
}
