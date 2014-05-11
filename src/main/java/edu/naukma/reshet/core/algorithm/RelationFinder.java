package edu.naukma.reshet.core.algorithm;

import com.carrotsearch.ant.tasks.junit4.dependencies.com.google.common.collect.Lists;
import com.carrotsearch.ant.tasks.junit4.dependencies.com.google.common.collect.Sets;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import edu.naukma.reshet.model.Snippet;
import edu.naukma.reshet.model.TermInDoc;
import edu.naukma.reshet.model.TermRelation;
import eu.hlavki.text.lemmagen.api.Lemmatizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

@Component
public class RelationFinder {
   private Lemmatizer lm;
   public Set<TermRelation> getRelations(List<TermInDoc> terms, List<Snippet> snippets){
     Set<TermRelation> termRelations = Sets.newHashSet();
     this.addAssociations(termRelations, terms, snippets);
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
