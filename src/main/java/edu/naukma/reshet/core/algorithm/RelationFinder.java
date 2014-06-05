package edu.naukma.reshet.core.algorithm;


import com.google.common.base.Function;
import com.google.common.base.Joiner;
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
import edu.naukma.reshet.core.algorithm.lexicography.pattern.IterationElement;
import edu.naukma.reshet.core.algorithm.lexicography.pattern.MatchPattern;
import edu.naukma.reshet.core.algorithm.lexicography.pattern.NounPhraseElement;
import edu.naukma.reshet.model.Snippet;
import edu.naukma.reshet.model.TermInDoc;
import edu.naukma.reshet.model.TermRelation;
import eu.hlavki.text.lemmagen.api.Lemmatizer;
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
    public final static MatchRule [] MATCH_RULES_1_9 = {
            new MatchRule(1, POSTag.ADJ, POSTag.NOUN, POSTag.NOUN, POSTag.NOUN),
            new MatchRule(1, POSTag.ADJ, POSTag.NOUN, POSTag.ADJ, POSTag.NOUN),
            new MatchRule(2, POSTag.ADJ, POSTag.NOUN, POSTag.NOUN),
            new MatchRule(2, POSTag.ADJ, POSTag.ADJ, POSTag.NOUN),
            new MatchRule(0, POSTag.NOUN, POSTag.ADJ, POSTag.NOUN),
            new MatchRule(1, POSTag.ADJ, POSTag.NOUN),
            new MatchRule(1, POSTag.NOUN, POSTag.NOUN),
            new MatchRule(0, POSTag.NOUN)
    };
    public final static MatchRule [] MATCH_RULES_1_4 = {
            new MatchRule(0, POSTag.NOUN, POSTag.ADJ, POSTag.NOUN),
            new MatchRule(1, POSTag.ADJ, POSTag.NOUN),
            new MatchRule(1, POSTag.NOUN, POSTag.NOUN),
            new MatchRule(0, POSTag.NOUN)
    };
    public final static MatchPattern LP1 = new MatchPattern(
            new NounPhraseElement(true, MATCH_RULES_1_9),
            new ExactWordElement("—","це","є","вважається","слід розуміти","являється"),
            new NounPhraseElement(false, MATCH_RULES_1_9)
    );
//    public final static MatchPattern LP2 = new MatchPattern(
//            new ExactWordElement("такий"),
//            new NounPhraseElement(true, MATCH_RULES_1_9),
//            new ExactWordElement(","),new ExactWordElement("як"),
//            new IterationElement(new NounPhraseElement(false, MATCH_RULES_1_9), new ExactWordElement(",")),
//            new ExactWordElement("і","або","й","та"),
//            new NounPhraseElement(false, MATCH_RULES_1_9)
//    );
    public final static MatchPattern LP2_1 = new MatchPattern(
            new ExactWordElement("такий"),
            new NounPhraseElement(true, MATCH_RULES_1_4),
            new ExactWordElement(","),new ExactWordElement("як"),
            new NounPhraseElement(false, MATCH_RULES_1_4),
            new ExactWordElement("і","або","й","та"),
            new NounPhraseElement(false, MATCH_RULES_1_4)
    );
    public final static MatchPattern LP2_2 = new MatchPattern(
            new ExactWordElement("такий"),
            new NounPhraseElement(true, MATCH_RULES_1_4),
            new ExactWordElement(","),new ExactWordElement("як"),
            new NounPhraseElement(false, MATCH_RULES_1_4), new ExactWordElement(","),
            new NounPhraseElement(false, MATCH_RULES_1_4),
            new ExactWordElement("і","або","й","та"),
            new NounPhraseElement(false, MATCH_RULES_1_4)
    );
    public final static MatchPattern LP2_3 = new MatchPattern(
            new ExactWordElement("такий"),
            new NounPhraseElement(true, MATCH_RULES_1_4),
            new ExactWordElement(","),new ExactWordElement("як"),
            new NounPhraseElement(false, MATCH_RULES_1_4), new ExactWordElement(","),
            new NounPhraseElement(false, MATCH_RULES_1_4), new ExactWordElement(","),
            new NounPhraseElement(false, MATCH_RULES_1_4),
            new ExactWordElement("і","або","й","та"),
            new NounPhraseElement(false, MATCH_RULES_1_4)
    );
    public final static MatchPattern LP2_4 = new MatchPattern(
            new ExactWordElement("такий"),
            new NounPhraseElement(true, MATCH_RULES_1_4),
            new ExactWordElement(","),new ExactWordElement("як"),
            new NounPhraseElement(false, MATCH_RULES_1_4), new ExactWordElement(","),
            new NounPhraseElement(false, MATCH_RULES_1_4), new ExactWordElement(","),
            new NounPhraseElement(false, MATCH_RULES_1_4), new ExactWordElement(","),
            new NounPhraseElement(false, MATCH_RULES_1_4),
            new ExactWordElement("і","або","й","та"),
            new NounPhraseElement(false, MATCH_RULES_1_4)
    );
//    public final static MatchPattern LP3 = new MatchPattern(
//            new NounPhraseElement(false, MATCH_RULES_1_9),
//            new IterationElement(new ExactWordElement(","), new NounPhraseElement(false, MATCH_RULES_1_9)),
//            new ExactWordElement("і","або","й","та"),new ExactWordElement("інший"),
//            new NounPhraseElement(true, MATCH_RULES_1_9)
//    );
    public final static MatchPattern LP3_1 = new MatchPattern(
            new NounPhraseElement(false, MATCH_RULES_1_4),
            new ExactWordElement(","), new NounPhraseElement(false, MATCH_RULES_1_4),
            new ExactWordElement("і","або","й","та"),new ExactWordElement("інший"),
            new NounPhraseElement(true, MATCH_RULES_1_4)
    );
    public final static MatchPattern LP3_2 = new MatchPattern(
            new NounPhraseElement(false, MATCH_RULES_1_4),
            new ExactWordElement(","), new NounPhraseElement(false, MATCH_RULES_1_4),
            new ExactWordElement(","), new NounPhraseElement(false, MATCH_RULES_1_4),
            new ExactWordElement("і","або","й","та"),new ExactWordElement("інший"),
            new NounPhraseElement(true, MATCH_RULES_1_4)
    );
    public final static MatchPattern LP3_3 = new MatchPattern(
            new NounPhraseElement(false, MATCH_RULES_1_4),
            new ExactWordElement(","), new NounPhraseElement(false, MATCH_RULES_1_4),
            new ExactWordElement(","), new NounPhraseElement(false, MATCH_RULES_1_4),
            new ExactWordElement(","), new NounPhraseElement(false, MATCH_RULES_1_4),
            new ExactWordElement("і","або","й","та"),new ExactWordElement("інший"),
            new NounPhraseElement(true, MATCH_RULES_1_4)
    );
    public final static MatchPattern LP3_4 = new MatchPattern(
            new NounPhraseElement(false, MATCH_RULES_1_4),
            new ExactWordElement(","), new NounPhraseElement(false, MATCH_RULES_1_4),
            new ExactWordElement(","), new NounPhraseElement(false, MATCH_RULES_1_4),
            new ExactWordElement(","), new NounPhraseElement(false, MATCH_RULES_1_4),
            new ExactWordElement(","), new NounPhraseElement(false, MATCH_RULES_1_4),
            new ExactWordElement("і","або","й","та"),new ExactWordElement("інший"),
            new NounPhraseElement(true, MATCH_RULES_1_4)
    );
    public  final static MatchPattern LP4 = new MatchPattern(
            new NounPhraseElement(true, MATCH_RULES_1_9),
            new ExactWordElement(","),new ExactWordElement("включаючи","зокрема","особливо"),
            new IterationElement(new NounPhraseElement(false, MATCH_RULES_1_9), new ExactWordElement(",")),
            new ExactWordElement("і","або","й","та"),
            new NounPhraseElement(false, MATCH_RULES_1_9)
    );
    public  final static MatchPattern LP4_1 = new MatchPattern(
            new NounPhraseElement(true, MATCH_RULES_1_9),
            new ExactWordElement(","),new ExactWordElement("включаючи","зокрема","особливо"),
            new NounPhraseElement(false, MATCH_RULES_1_9),
            new ExactWordElement("і","або","й","та"),
            new NounPhraseElement(false, MATCH_RULES_1_9)
    );
    public  final static MatchPattern LP4_2 = new MatchPattern(
            new NounPhraseElement(true, MATCH_RULES_1_9),
            new ExactWordElement(","),new ExactWordElement("включаючи","зокрема","особливо"),
            new NounPhraseElement(false, MATCH_RULES_1_9), new ExactWordElement(","),
            new NounPhraseElement(false, MATCH_RULES_1_9),
            new ExactWordElement("і","або","й","та"),
            new NounPhraseElement(false, MATCH_RULES_1_9)
    );
    public  final static MatchPattern LP4_3 = new MatchPattern(
            new NounPhraseElement(true, MATCH_RULES_1_9),
            new ExactWordElement(","),new ExactWordElement("включаючи","зокрема","особливо"),
            new NounPhraseElement(false, MATCH_RULES_1_9), new ExactWordElement(","),
            new NounPhraseElement(false, MATCH_RULES_1_9), new ExactWordElement(","),
            new NounPhraseElement(false, MATCH_RULES_1_9),
            new ExactWordElement("і","або","й","та"),
            new NounPhraseElement(false, MATCH_RULES_1_9)
    );
    public  final static MatchPattern LP4_4 = new MatchPattern(
            new NounPhraseElement(true, MATCH_RULES_1_9),
            new ExactWordElement(","),new ExactWordElement("включаючи","зокрема","особливо"),
            new NounPhraseElement(false, MATCH_RULES_1_9), new ExactWordElement(","),
            new NounPhraseElement(false, MATCH_RULES_1_9), new ExactWordElement(","),
            new NounPhraseElement(false, MATCH_RULES_1_9), new ExactWordElement(","),
            new NounPhraseElement(false, MATCH_RULES_1_9),
            new ExactWordElement("і","або","й","та"),
            new NounPhraseElement(false, MATCH_RULES_1_9)
    );
//    private final static MatchPattern LP6 = new MatchPattern(
//            new NounPhraseElement(true, MATCH_RULES_1_9),
//            new ExactWordElement(","),new ExactWordElement("включаючи","зокрема","особливо"),
//            new IterationElement(new NounPhraseElement(false, MATCH_RULES_1_9), new ExactWordElement(",")),
//            new ExactWordElement("і","або","й","та"),
//            new NounPhraseElement(false, MATCH_RULES_1_9)
//    );
    private final List<MatchPattern> isAPatterns = new ImmutableList.Builder<MatchPattern>()
            .add(LP1)
            .add(LP2_4,LP2_3,LP2_2,LP2_1)
            .add(LP3_4,LP3_3,LP3_2,LP3_1)
            .add(LP4_4,LP4_3,LP4_2,LP4_1)
        .build();
    public Set<TermRelation> getRelations(List<TermInDoc> terms, List<Snippet> snippets){
     Set<TermRelation> termRelations = Sets.newHashSet();
     //this.addAssociations(termRelations, terms, snippets);
     this.addIsA(termRelations, terms, snippets);
     //this.addIsPartOf(termRelations, terms, snippets);
     return termRelations;
   }
   private void addAssociations(Set<TermRelation> relations, List<TermInDoc> terms, List<Snippet> snippets){
       Function<TermInDoc,String> termToString = new Function<TermInDoc, String>() {
           @Nullable
           @Override
           public String apply(@Nullable TermInDoc input) {
               return input.getTermin().getText();
           }
       };
       for(Snippet snip:snippets){
       List<TermInDoc> relatedTerms = getTermsListFromSentence(snip.getText(), terms);
       String str = Joiner.on(",").join(Lists.transform(relatedTerms,termToString));
       System.out.println(str);
       for(TermInDoc term:relatedTerms){
         if(!term.getTermin().getText().equals(snip.getTerm().getTermin().getText())){
           relations.add(new TermRelation(snip.getTerm(),term,"association", "science"));
           relations.add(new TermRelation(term,snip.getTerm(),"association", "science"));
         }
       }
     }

   }
  private void addIsA(Set<TermRelation> relations, List<TermInDoc> terms, List<Snippet> snippets){
    System.out.println("Snippets to process: " + snippets.size());
    int snipCounter = 0;
    Set<Integer> processedSentences = Sets.newHashSet();
    for(Snippet snip:snippets){
        iterator.setText(snip.getText());
        int start = iterator.first();
        for (int end = iterator.next();
             end != BreakIterator.DONE;
             start = end, end = iterator.next()) {
            String sentence = snip.getText().substring(start, end);
            if(!processedSentences.contains(sentence.hashCode())){
                relations.addAll(getRelationsFromSentence(sentence, terms));
                processedSentences.add(sentence.hashCode());
            }

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
         List<List<NounPhraseMatch>> matches = pattern.matchAll(sentence);
         for(List<NounPhraseMatch> match: matches){
             relations.addAll(pattern.getRelations(match));
         }
     }
     if(!relations.isEmpty()){
         System.out.println(relations);
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
