package edu.naukma.reshet.core.algorithm;

import com.carrotsearch.ant.tasks.junit4.dependencies.com.google.common.collect.Lists;
import com.carrotsearch.ant.tasks.junit4.dependencies.com.google.common.collect.Ordering;
import edu.naukma.reshet.model.TermInDoc;
import edu.naukma.reshet.repositories.TerminRepository;
import edu.naukma.reshet.shared.DocumentaryFrequencyCrawler;
import edu.naukma.reshet.shared.Searcher;
import edu.naukma.reshet.shared.algorithm.InitialTerminologyExtractor;
import org.languagetool.AnalyzedSentence;
import org.languagetool.AnalyzedTokenReadings;
import org.languagetool.JLanguageTool;
import org.languagetool.language.Ukrainian;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component("noun")
public class TopTfIdfInitialTerminologyNounExtractor implements InitialTerminologyExtractor {

  @Autowired
  @Qualifier("cached")
  private
  DocumentaryFrequencyCrawler crawler;

  @Autowired
  private
  TerminRepository terminRepo;

  private JLanguageTool langTool;

  public TopTfIdfInitialTerminologyNounExtractor(){
      try {
          langTool = new JLanguageTool(new Ukrainian());
      } catch (IOException e) {
          e.printStackTrace();
      }
  }
  @Override
  public List<TermInDoc> extractValuableTerms(Searcher searcher, Integer docId) {
    List<TermInDoc> terms = Lists.newArrayList();
    Map<String, Integer> map = searcher.getFrequencies(docId);
    for(String term: map.keySet()){
        try {
            List<AnalyzedSentence> analyzed = langTool.analyzeText(term);
            AnalyzedSentence sentence = analyzed.get(0);
            boolean hasNoun = sentence.getTokensWithoutWhitespace()[1].hasPartialPosTag("noun");

            AnalyzedTokenReadings[] tokens = analyzed.get(0).getTokensWithoutWhitespace();
            System.out.print(tokens);

            if (hasNoun) {
                Integer frequency =  map.get(term);
                Double docFreq = crawler.getDocumentaryFrequency(term);
                Double totalFreq = 1.0*frequency/docFreq;
                terms.add(new TermInDoc(terminRepo.findByText(term),totalFreq));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    return Ordering
            .natural()
            .reverse()
            .immutableSortedCopy(terms);
  }

    public void setCrawler(DocumentaryFrequencyCrawler crawler) {
        this.crawler = crawler;
    }

    public void setTerminRepo(TerminRepository terminRepo) {
        this.terminRepo = terminRepo;
    }
}
