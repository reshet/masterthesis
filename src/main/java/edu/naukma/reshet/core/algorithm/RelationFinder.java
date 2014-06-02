package edu.naukma.reshet.core.algorithm;


import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import edu.naukma.reshet.core.algorithm.lexicography.MatchRule;
import edu.naukma.reshet.core.algorithm.lexicography.NounPhraseMatch;
import edu.naukma.reshet.core.algorithm.lexicography.POSTag;
import edu.naukma.reshet.core.algorithm.lexicography.pattern.ExactWordElement;
import edu.naukma.reshet.core.algorithm.lexicography.pattern.MatchPattern;
import edu.naukma.reshet.core.algorithm.lexicography.pattern.NounPhraseElement;
import edu.naukma.reshet.model.Snippet;
import edu.naukma.reshet.model.TermInDoc;
import edu.naukma.reshet.model.TermRelation;
import eu.hlavki.text.lemmagen.api.Lemmatizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.text.BreakIterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Component
public class RelationFinder {
   private Lemmatizer lm;
    private BreakIterator iterator = BreakIterator.getSentenceInstance(new Locale("uk-UA"));
    private final MatchRule [] nounPhrase = {
            new MatchRule(1, POSTag.ADJ, POSTag.NOUN, POSTag.NOUN, POSTag.NOUN),
            new MatchRule(1, POSTag.ADJ, POSTag.NOUN, POSTag.ADJ, POSTag.NOUN),
            new MatchRule(2, POSTag.ADJ, POSTag.NOUN, POSTag.NOUN),
            new MatchRule(2, POSTag.ADJ, POSTag.ADJ, POSTag.NOUN),
            new MatchRule(0, POSTag.NOUN, POSTag.ADJ, POSTag.NOUN),
            new MatchRule(1, POSTag.ADJ, POSTag.NOUN),
            new MatchRule(1, POSTag.NOUN, POSTag.NOUN),
            new MatchRule(0, POSTag.NOUN)
    };
    private final List<MatchPattern> isAPatterns = new ImmutableList.Builder<MatchPattern>()
            .add(
                new MatchPattern(
                        new NounPhraseElement(true, nounPhrase),
                        new ExactWordElement("—","це","є","вважається","слід розуміти","являється"),
                        new NounPhraseElement(false, nounPhrase)
                )
            ).build();
    public Set<TermRelation> getRelations(List<TermInDoc> terms, List<Snippet> snippets){
     Set<TermRelation> termRelations = Sets.newHashSet();
     //this.addAssociations(termRelations, terms, snippets);
     this.addIsA(termRelations, terms, snippets);
     this.addIsPartOf(termRelations, terms, snippets);
     return termRelations;
   }
   private void addAssociations(Set<TermRelation> relations, List<TermInDoc> terms, List<Snippet> snippets){
     for(Snippet snip:snippets){
       List<TermInDoc> relatedTerms = getTermsListFromSentence(snip.getText(), terms);
       for(TermInDoc term:relatedTerms){
         if(!term.getTermin().getText().equals(snip.getTerm().getTermin().getText())){
           relations.add(new TermRelation(snip.getTerm(),term,"association"));
           relations.add(new TermRelation(term,snip.getTerm(),"association"));
         }
       }
     }
   }
  private void addIsA(Set<TermRelation> relations, List<TermInDoc> terms, List<Snippet> snippets){
    System.out.println("Snippets to process: " + snippets.size());
    int snipCounter = 0;
    for(Snippet snip:snippets){
        iterator.setText(snip.getText());
        int start = iterator.first();
        for (int end = iterator.next();
             end != BreakIterator.DONE;
             start = end, end = iterator.next()) {
            String sentence = snip.getText().substring(start, end);
            relations.addAll(getRelationsFromSentence(sentence, terms));
        }
        snipCounter++;
        if(snipCounter % 50 == 0){
            System.out.println("Snippets processed: " + snipCounter + "/" + snippets.size());
        }
    }
  }
  private void addIsPartOf(Set<TermRelation> relations, List<TermInDoc> terms, List<Snippet> snippets){
   /* for(Snippet snip:snippets){
      List<TermInDoc> relatedTerms = getTermsListFromSentence(snip.getText(), terms);
      *//*for(TermInDoc term:relatedTerms){
        if(!term.getTermin().getText().equals(snip.getTerm().getTermin().getText())){
          relations.add(new TermRelation(snip.getTerm(),term,"association"));
          relations.add(new TermRelation(term,snip.getTerm(),"association"));
        }
      }*//*
    }*/
  }
   private List<TermRelation> getRelationsFromSentence(String sentence, List<TermInDoc> terms){
     List<TermRelation> relations = Lists.newLinkedList();
     for(MatchPattern pattern: isAPatterns){
         List<NounPhraseMatch> match = pattern.matchFirst(sentence);
         List<TermRelation> pRelations = pattern.getRelations(match);
         relations.addAll(pRelations);
     }
     return relations;
   }
   private List<TermInDoc> getTermsListFromSentence(String sentence, List<TermInDoc> allTerms){
       List<TermInDoc> terms = Lists.newArrayList();
       String [] words = sentence.split("(?=[,.])|\\s+");
       for (String word : words) {
         if (word.trim().length() > 1) {
           final String lemma = lm.lemmatize(word.trim()).toString().toLowerCase();
           Optional<TermInDoc> foundTerm = Optional.fromNullable(Iterables.find(allTerms, new Predicate<TermInDoc>() {
             @Override
             public boolean apply(@Nullable TermInDoc termInDoc) {
               return termInDoc.getTermin().getText().equals(lemma);
             }
           }, null));
           if(foundTerm.isPresent()){
             terms.add(foundTerm.get());
           }
         }
       }
     return terms;
   }

  public void setLm(Lemmatizer lm) {
    this.lm = lm;
  }
}
