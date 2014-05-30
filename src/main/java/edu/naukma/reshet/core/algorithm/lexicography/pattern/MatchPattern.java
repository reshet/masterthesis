package edu.naukma.reshet.core.algorithm.lexicography.pattern;


import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import edu.naukma.reshet.core.algorithm.lexicography.NounPhraseMatch;
import edu.naukma.reshet.model.TermInDoc;
import edu.naukma.reshet.model.TermRelation;
import edu.naukma.reshet.model.Termin;

import java.util.Collections;
import java.util.List;

public class MatchPattern {
    private final PatternElement [] elemets;

    public MatchPattern(PatternElement... elemets) {
        this.elemets = elemets;
    }
    public List<TermRelation> getRelations(List<NounPhraseMatch> match){
        List<TermRelation> relations = Lists.newArrayList();
        if(match.isEmpty()) return  relations;
        for(int i = 0; i < elemets.length; i++){
            for(int j = i; j < elemets.length; j++){
               if(i != j){
                   PatternElement elem1 = elemets[i];
                   PatternElement elem2 = elemets[j];
                   if(elem1 instanceof NounPhraseElement && elem2 instanceof NounPhraseElement){
                       TermInDoc term1 =  new TermInDoc(new Termin(match.get(i).getPhrase().getText(),1L),1.0);
                       TermInDoc term2 =  new TermInDoc(new Termin(match.get(j).getPhrase().getText(),1L),1.0);

                       NounPhraseElement noun1 = (NounPhraseElement)elem1;
                       NounPhraseElement noun2 = (NounPhraseElement)elem2;
                       if(noun1.isMainPhrase() && !noun2.isMainPhrase()){
                           relations.add(new TermRelation(term1, term2, "NT"));
                           relations.add(new TermRelation(term2, term1, "BT"));
                       } else
                       if(! noun1.isMainPhrase() && noun2.isMainPhrase()){
                           relations.add(new TermRelation(term1, term2, "BT"));
                           relations.add(new TermRelation(term2, term1, "NT"));
                       } else
                       {
                           relations.add(new TermRelation(term1, term2, "RT"));
                           relations.add(new TermRelation(term2, term1, "RT"));
                       }
                   }
               }
            }
        }
        return relations;
    }
    // With backtracking inside
    public boolean mathes(String phrase){
        List<List<NounPhraseMatch>> allMatches = Lists.newArrayList();
        for(PatternElement element: elemets){
            List<NounPhraseMatch> matches = element.apply(phrase);
            allMatches.add(matches);
        }
        return hasBestEffortMatch(Lists.<NounPhraseMatch>newArrayList(), allMatches);
    }
    public List<NounPhraseMatch> matchFirst(String phrase){
        List<List<NounPhraseMatch>> allMatches = Lists.newArrayList();
        for(PatternElement element: elemets){
            List<NounPhraseMatch> matches = element.apply(phrase);
            //Should be at least one match to each match element.
            if(matches.isEmpty()){
                return Collections.emptyList();
            }
            allMatches.add(matches);
        }
        List<NounPhraseMatch>  match = Lists.newArrayList();
        return findBestEffortMatch(match, allMatches);
    }
    public List<NounPhraseMatch> findBestEffortMatch(List<NounPhraseMatch> currentMatch, List<List<NounPhraseMatch>> lastingMatches){

        if(lastingMatches.size() > 0){
            List<NounPhraseMatch> stepMatches = lastingMatches.get(0);
            for(NounPhraseMatch stepMatch: stepMatches){
                List<NounPhraseMatch> currentStep = Lists.newArrayList(currentMatch);
                if(currentStep.isEmpty()){
                    currentStep.add(stepMatch);
                    System.out.println(currentStep);
                    List<NounPhraseMatch> match = findBestEffortMatch(currentStep, FluentIterable.from(lastingMatches).skip(1).toList());
                    if(match.size() == elemets.length){
                        return match;
                    }
                } else if (currentStep.get(currentStep.size()-1).getPosition() < stepMatch.getPosition()){
                    currentStep.add(stepMatch);
                    System.out.println(currentStep);
                    List<NounPhraseMatch> match = findBestEffortMatch(currentStep,FluentIterable.from(lastingMatches).skip(1).toList());
                    if(match.size() == elemets.length){
                        return match;
                    }
                }
            }
            return currentMatch;
        }
        return currentMatch;
    }
    public boolean hasBestEffortMatch(List<NounPhraseMatch> currentMatch, List<List<NounPhraseMatch>> lastingMatches){
        if(lastingMatches.size() > 0){
            List<NounPhraseMatch> stepMatches = lastingMatches.get(0);
            for(NounPhraseMatch stepMatch: stepMatches){
                if(currentMatch.isEmpty()){
                    //currentMatch = Lists.newArrayList();
                    currentMatch.add(stepMatch);
                  boolean found = hasBestEffortMatch(currentMatch, FluentIterable.from(lastingMatches).skip(1).toList());
                  if(found){
                      return true;
                  }
                }
                if (currentMatch.get(currentMatch.size()-1).getPosition() < stepMatch.getPosition()){
                    boolean found = hasBestEffortMatch(currentMatch, FluentIterable.from(lastingMatches).skip(1).toList());
                    if (found){
                        return true;
                    }
                }
            }
            return false;
        }
        return true;
    } 
    
}
