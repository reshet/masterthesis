package edu.naukma.reshet.integration.single;

import com.carrotsearch.ant.tasks.junit4.dependencies.com.google.common.collect.Lists;
import com.carrotsearch.ant.tasks.junit4.dependencies.com.google.common.collect.Ordering;
import edu.naukma.reshet.configuration.MongoConfiguration;
import edu.naukma.reshet.core.*;
import edu.naukma.reshet.model.TermInDoc;
import edu.naukma.reshet.model.Termin;
import edu.naukma.reshet.shared.DocumentaryFrequencyCrawler;
import edu.naukma.reshet.shared.Searcher;
import eu.hlavki.text.lemmagen.LemmatizerFactory;
import eu.hlavki.text.lemmagen.api.Lemmatizer;
import org.languagetool.AnalyzedSentence;
import org.languagetool.AnalyzedTokenReadings;
import org.languagetool.JLanguageTool;
import org.languagetool.language.Ukrainian;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.text.BreakIterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MainAlgorithmSecondStepPOS {
  public static void indexSampleDoc(){
    UkrLemmatizedIndexer indexer = new UkrLemmatizedIndexer();
    indexer.setDirectory("/home/reshet/masterthesis/index2/");
    PdfFileExtractor extractor = new PdfFileExtractor();
    extractor.setFileName("/home/reshet/masterthesis/src/test/resources/sample2.pdf");
    try {
      indexer.indexDocument(extractor.getDocument());

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  public static Map<String, Integer> findTermFrequenciesOfFirstDocInIndex(Searcher searcher){
      Map<String, Integer> map = searcher.getFrequencies();
      return map;
  }
  public static List<TermInDoc> getSortedTermsList(Map<String, Integer> map, DocumentaryFrequencyCrawler crawler){
    List<TermInDoc> termins = Lists.newArrayList();
    for(String term: map.keySet()){
      Integer frequency =  map.get(term);
      Long docFreq = crawler.getDocumentaryFrequency(term);
      Double totalFreq = 1.0*frequency/docFreq;
      termins.add(new TermInDoc(new Termin(term, docFreq),totalFreq));
    }
    return Ordering
            .natural()
            .reverse()
            .immutableSortedCopy(termins);
  }
  static Lemmatizer lm = null;
  static {
    try {
      lm = LemmatizerFactory.getPrebuilt("mlteast-uk");
    } catch (IOException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
  }
  private static boolean isSentenceContainTerm(String sentence, String term){
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
  private static List<String> proceessSingleSentenceSnippetBuilder(List<String> snippets, String term){
    List<String> processedSnippets = Lists.newArrayList();

    for(String snip: snippets){
      StringBuilder allSnipSentences = new StringBuilder();
      BreakIterator iterator = BreakIterator.getSentenceInstance(new Locale("uk-UA"));
      iterator.setText(snip);
      int start = iterator.first();
      for (int end = iterator.next();
           end != BreakIterator.DONE;
           start = end, end = iterator.next()) {
        String sentence = snip.substring(start,end);
        if (isSentenceContainTerm(sentence,term)){
          allSnipSentences.append(sentence);
        }
      }
      processedSnippets.add(allSnipSentences.toString());
    }
    return processedSnippets;
  }
  public static List<String> findTextSnippetsForTerm(TermInDoc term, Searcher searcher){
    List<String> snippets = searcher.search(term.getTermin().getText());
    return proceessSingleSentenceSnippetBuilder(snippets, term.getTermin().getText());
  }
  public static void posTagSnippet(String snippet){
    JLanguageTool langTool = null;
    try {
      langTool = new JLanguageTool(new Ukrainian());
      langTool.activateDefaultPatternRules();
      List<AnalyzedSentence> analyzed = langTool.analyzeText(snippet);
      for(AnalyzedSentence analyzedSentence:analyzed){
        //System.out.println(analyzedSentence);
        //System.out.println(analyzedSentence.getTokenSet());
        //System.out.println(analyzedSentence.getAnnotations());
        //System.out.println(analyzedSentence.getLemmaSet());
        System.out.println(analyzedSentence.getTokens());
        for(AnalyzedTokenReadings token:analyzedSentence.getTokens()){
          System.out.println(token);
          String tok = token.getToken();
        }



      }
    } catch (IOException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }

  }
  public static void main(String args[]){
    System.out.println("GetTextSnippet subprogram");
    AnnotationConfigApplicationContext ctx =
            new AnnotationConfigApplicationContext();

    ctx.register(MongoConfiguration.class);
    ctx.register(SingleDocumentInitialExtractApplication.class);
    ctx.refresh();

    GoogleCrawler crawler = ctx.getBean(GoogleCachedCrawler.class);
    SimpleTextSearcher searcher = new SimpleTextSearcher("/home/reshet/masterthesis/index2/");

    Map <String, Integer> map = findTermFrequenciesOfFirstDocInIndex(searcher);
    List<TermInDoc> sortedList = getSortedTermsList(map, crawler);
    for(TermInDoc term:sortedList){
      System.out.println(term);
    }
    TermInDoc termin = sortedList.get(5);
    List<String> snippets = findTextSnippetsForTerm(termin, searcher);
    for(String snip:snippets){
      posTagSnippet(snip);
    }
    System.out.println("Term to find: " + termin.getTermin().getText());
    System.out.println(snippets);
  }
}
