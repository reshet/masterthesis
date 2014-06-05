package edu.naukma.reshet.core.algorithm.lexicography.pattern;


import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import edu.naukma.reshet.core.algorithm.lexicography.NounPhraseCompoundMatch;
import edu.naukma.reshet.core.algorithm.lexicography.NounPhraseMatch;
import edu.naukma.reshet.model.TermInDoc;
import edu.naukma.reshet.model.TermRelation;
import edu.naukma.reshet.model.Termin;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class MatchPattern {
    private final PatternElement [] elemets;

    public MatchPattern(PatternElement... elemets) {
        this.elemets = elemets;
    }
    private List<TermRelation> meetTwoNP(NounPhraseElement noun1, NounPhraseElement noun2, String text1,String text2){
        List<TermRelation> relations = Lists.newArrayList();
        TermInDoc term1 =  new TermInDoc(new Termin(text1,1D),1.0,"science");
        TermInDoc term2 =  new TermInDoc(new Termin(text2,1D),1.0,"science");

        if(noun1.isMainPhrase() && !noun2.isMainPhrase()){
            relations.add(new TermRelation(term1, term2, "NT", "science"));
            relations.add(new TermRelation(term2, term1, "BT", "science"));
        } else
        if(! noun1.isMainPhrase() && noun2.isMainPhrase()){
            relations.add(new TermRelation(term1, term2, "BT", "science"));
            relations.add(new TermRelation(term2, term1, "NT", "science"));
        } else
        {
            relations.add(new TermRelation(term1, term2, "RT", "science"));
            relations.add(new TermRelation(term2, term1, "RT", "science"));
        }
        return relations;
    }
    private List<TermRelation> allIterationRelations(IterationElement iter, NounPhraseCompoundMatch cm){
        List<TermRelation> relations = Lists.newArrayList();

        return relations;
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
                        relations.addAll(
                               meetTwoNP(
                                       (NounPhraseElement)elem1,
                                       (NounPhraseElement)elem2,
                                       match.get(i).getPhrase().getText(),
                                       match.get(j).getPhrase().getText()
                               )
                       );
                   }
                   if(elem1 instanceof IterationElement && elem2 instanceof NounPhraseElement){
                       IterationElement iter1 = (IterationElement)elem1;
                       NounPhraseCompoundMatch cm1 = (NounPhraseCompoundMatch)match.get(i);
                       int mult = cm1.getChildren().size() / iter1.getRepeatedElementSequence().length;
                       for(PatternElement pe: iter1.getRepeatedElementSequence()){
                           int i1 = 0;
                           if(pe instanceof NounPhraseElement){
                               for(int j1 = 0; j1 < mult; j1++){
                                   relations.addAll(
                                       meetTwoNP(
                                           (NounPhraseElement)pe,
                                           (NounPhraseElement) elem2,
                                           cm1.getChildren().get(i1 + iter1.getRepeatedElementSequence().length * j1).getPhrase().getText(),
                                           match.get(j).getPhrase().getText())
                                   );
                               }
                           }
                           i1++;
                       }
                   }
                   if(elem1 instanceof NounPhraseElement && elem2 instanceof IterationElement){
                       IterationElement iter2 = (IterationElement)elem2;
                       NounPhraseCompoundMatch cm2 = (NounPhraseCompoundMatch)match.get(j);
                       int mult = cm2.getChildren().size() / iter2.getRepeatedElementSequence().length;
                       for(PatternElement pe: iter2.getRepeatedElementSequence()){
                           int i2 = 0;
                           if(pe instanceof NounPhraseElement){
                               for(int j2 = 0; j2 < mult; j2++){
                                   relations.addAll(
                                       meetTwoNP(
                                           (NounPhraseElement)elem1,
                                           (NounPhraseElement)pe,
                                           match.get(i).getPhrase().getText(),
                                           cm2.getChildren().get(i2 + iter2.getRepeatedElementSequence().length * j2).getPhrase().getText()
                                       ));
                               }
                           }
                           i2++;
                       }
                   }
                   if(elem1 instanceof IterationElement && elem2 instanceof IterationElement){
                       IterationElement iter1 = (IterationElement)elem1;
                       IterationElement iter2 = (IterationElement)elem2;
                       NounPhraseCompoundMatch cm1 = (NounPhraseCompoundMatch)match.get(i);
                       NounPhraseCompoundMatch cm2 = (NounPhraseCompoundMatch)match.get(j);
                       int mult1 = cm1.getChildren().size() / iter1.getRepeatedElementSequence().length;
                       int mult2 = cm2.getChildren().size() / iter2.getRepeatedElementSequence().length;

                       for(PatternElement pe1: iter1.getRepeatedElementSequence()){
                           int i1 = 0;
                           if(pe1 instanceof NounPhraseElement){
                               for(int j1 = 0; j1 < mult1; j1++){

                                   for(PatternElement pe2: iter2.getRepeatedElementSequence()){
                                       int i2 = 0;
                                       if(pe2 instanceof NounPhraseElement){
                                           for(int j2 = 0; j2 < mult2; j2++){
                                               relations.addAll(
                                                   meetTwoNP(
                                                       (NounPhraseElement)pe1,
                                                       (NounPhraseElement) pe2,
                                                       cm1.getChildren().get(i1 + iter1.getRepeatedElementSequence().length * j1).getPhrase().getText(),
                                                       cm2.getChildren().get(i2 + iter2.getRepeatedElementSequence().length * j2).getPhrase().getText()
                                                       )
                                               );
                                           }
                                       }
                                       i2++;
                                   }
                               }
                           }
                           i1++;
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
    private List<List<NounPhraseMatch>> extractElementsMatches(String phrase){
        List<List<NounPhraseMatch>> allMatches = Lists.newLinkedList();
        for(PatternElement element: elemets){
            List<NounPhraseMatch> matches = element.apply(phrase);
            //Should be at least one match to each match element.
            if(matches.isEmpty()){
                return Collections.emptyList();
            }
            allMatches.add(matches);
        }
        return allMatches;
    }
    public List<NounPhraseMatch> matchFirst(String phrase){
        List<NounPhraseMatch>  match = Lists.newArrayList();
        //System.out.println(allMatches);
        return findBestEffortMatch(match, extractElementsMatches(phrase));
    }
    public List<List<NounPhraseMatch>> matchAll(String phrase){
        List<NounPhraseMatch>  match = Lists.newArrayList();
        List<List<NounPhraseMatch>> allCompleteMatches = Lists.newArrayList();
        findAllMatches(match, extractElementsMatches(phrase), allCompleteMatches);
        return allCompleteMatches;
    }
    public List<NounPhraseMatch> findBestEffortMatch(List<NounPhraseMatch> currentMatch, List<List<NounPhraseMatch>> lastingMatches){
        if(lastingMatches.size() > 0){
            List<NounPhraseMatch> stepMatches = lastingMatches.get(0);
            for(NounPhraseMatch stepMatch: stepMatches){
                List<NounPhraseMatch> currentStep = Lists.newArrayList(currentMatch);
                if(currentStep.isEmpty()){
                    currentStep.add(stepMatch);
                    List<NounPhraseMatch> match = findBestEffortMatch(currentStep, FluentIterable.from(lastingMatches).skip(1).toList());
                    if(match.size() == elemets.length){
                        return match;
                    }
               //not strict match - this allows * between matches.
               // } else if (currentStep.get(currentStep.size()-1).getPosition() < stepMatch.getPosition()){
               //strict match: it should follow immediatly
                } else if (currentStep.get(currentStep.size()-1).getEndPosition() + 1 == stepMatch.getStartPosition()){
                    currentStep.add(stepMatch);
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
    private void addMatch(List<NounPhraseMatch> currentStep, NounPhraseMatch stepMatch){
        if(stepMatch instanceof NounPhraseCompoundMatch){
            NounPhraseCompoundMatch compoundMatch  = (NounPhraseCompoundMatch)stepMatch;
            //currentStep.addAll(compoundMatch.getChildren());
            currentStep.add(compoundMatch);
        } else {
            currentStep.add(stepMatch);
        }
    }
    public void findAllMatches(List<NounPhraseMatch> currentMatch, List<List<NounPhraseMatch>> lastingMatches, List<List<NounPhraseMatch>> completeMatches){
        if(lastingMatches.size() > 0){
            List<NounPhraseMatch> stepMatches = lastingMatches.get(0);
            for(NounPhraseMatch stepMatch: stepMatches){
                List<NounPhraseMatch> currentStep = Lists.newArrayList(currentMatch);
                if(currentStep.isEmpty()){
                    addMatch(currentStep,stepMatch);
                    findAllMatches(currentStep, FluentIterable.from(lastingMatches).skip(1).toList(), completeMatches);
                } else if (currentStep.get(currentStep.size()-1).getEndPosition() + 1 == stepMatch.getStartPosition()){
                    addMatch(currentStep,stepMatch);
                    findAllMatches(currentStep, FluentIterable.from(lastingMatches).skip(1).toList(), completeMatches);
                }
            }
        } else {
            if(currentMatch.size() >= elemets.length){
                completeMatches.add(currentMatch);
            }
        }
    }
    class ListToMapFunc implements Function<List<NounPhraseMatch>, Integer>{
        private int counter = 0;
        public ListToMapFunc reset(){
            counter = 0;
            return this;
        }
        @Nullable
        @Override
        public Integer apply(@Nullable List<NounPhraseMatch> element) {
            return counter++;
        }
    }
    private ListToMapFunc listToMap = new ListToMapFunc();
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
                if (currentMatch.get(currentMatch.size()-1).getEndPosition() < stepMatch.getStartPosition()){
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
