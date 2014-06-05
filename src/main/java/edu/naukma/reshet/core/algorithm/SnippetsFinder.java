package edu.naukma.reshet.core.algorithm;

import com.carrotsearch.ant.tasks.junit4.dependencies.com.google.common.collect.Lists;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import edu.naukma.reshet.model.DocText;
import edu.naukma.reshet.model.Snippet;
import edu.naukma.reshet.model.TermInDoc;
import edu.naukma.reshet.shared.Searcher2;
import eu.hlavki.text.lemmagen.api.Lemmatizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.BreakIterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
public class SnippetsFinder {
  private Lemmatizer lm;
  private Searcher2 searcher;
  private BreakIterator iterator = BreakIterator.getSentenceInstance(new Locale("uk-UA"));
  public List<Snippet> findSnippets(List<TermInDoc> terms){
    //List<Snippet> snippets = Lists.newArrayList();
    Map<Integer,Snippet> uniqueSnipets = Maps.newHashMap();
    for(TermInDoc term:terms){
      System.out.println("Find snippets for term: " + term.getTermin().getText());
      List<DocText> docs = searcher.searchDocs(term);
      proceessSingleSentenceSnippetBuilder(uniqueSnipets, docs);
      //snippets.addAll(lst);
    }
    return ImmutableList.copyOf(uniqueSnipets.values());
  }
  private boolean isSentenceContainTerm(String sentence, String term){
    String [] words = sentence.split("(?=[,.])|\\s+");
    for (String word : words) {
      if (word.trim().length() > 1) {
        String lemma = lm.lemmatize(word.trim()).toString().toLowerCase();
        if(lemma.equals(term)){
          return true;
        }
      }
    }
    return false;
  }
  private void proceessSingleSentenceSnippetBuilder(Map<Integer,Snippet> unSnippets,List<DocText> docs){
    List<Snippet> processedSnippets = Lists.newArrayList();
    for(DocText doc: docs){
      StringBuilder allSnipSentences = new StringBuilder();
      iterator.setText(doc.getText());
      int start = iterator.first();
      for (int end = iterator.next();
           end != BreakIterator.DONE;
           start = end, end = iterator.next()) {
        String sentence = doc.getText().substring(start, end);
        if (isSentenceContainTerm(sentence,doc.getContainsTerm().getTermin().getText())){
          allSnipSentences.append(sentence);
        }
      }
               String snipText = allSnipSentences.toString();
      int code = snipText.hashCode();
      if(!unSnippets.containsKey(snipText.hashCode())){
         unSnippets.put(code, new Snippet(allSnipSentences.toString(),doc.getContainsTerm(),doc.getDocID()));
      }
      //processedSnippets.add();
    }
    //return processedSnippets;
  }

  public void setLm(Lemmatizer lm) {
    this.lm = lm;
  }

  public void setSearcher(Searcher2 searcher) {
    this.searcher = searcher;
  }
}
