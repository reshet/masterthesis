package edu.naukma.reshet.core.algorithm;

import com.carrotsearch.ant.tasks.junit4.dependencies.com.google.common.collect.Lists;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import edu.naukma.reshet.model.TermInDoc;
import edu.naukma.reshet.repositories.TerminRepository;
import edu.naukma.reshet.shared.DocumentaryFrequencyCrawler;
import edu.naukma.reshet.shared.Searcher;
import edu.naukma.reshet.shared.algorithm.InitialTerminologyExtractor;
import org.languagetool.AnalyzedSentence;
import org.languagetool.JLanguageTool;
import org.languagetool.language.Ukrainian;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component("noun")
public class TopTfIdfInitialTerminologyNounExtractor implements InitialTerminologyExtractor {

  @Autowired
  @Qualifier("local")
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
    Map<String, Integer> map = filterTerms(searcher.getFrequencies(docId));

    for(String term: map.keySet()){
        try {
            List<AnalyzedSentence> analyzed = langTool.analyzeText(term);
            if(analyzed.isEmpty()) continue;
            AnalyzedSentence sentence = analyzed.get(0);
            boolean hasNoun = sentence.getTokensWithoutWhitespace()[1].hasPartialPosTag("noun");
            if (hasNoun) {
                Integer frequency =  map.get(term);
                Double docFreq = crawler.getDocumentaryFrequency(term);
                Double totalFreq = 1.0*frequency*Math.log(1.0/docFreq);
                terms.add(new TermInDoc(terminRepo.findByText(term),totalFreq, searcher.getIndexName()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    return FluentIterable.from(
            Ordering
                .natural()
                .reverse()
                .immutableSortedCopy(terms)
            ).limit((int)Math.round(terms.size()*0.2)).toList();
  }
    private static Map<String, Integer> filterTerms(Map<String, Integer> map){
        return ImmutableMap.copyOf(Maps.filterEntries(map, new Predicate<Map.Entry<String, Integer>>() {
            @Override
            public boolean apply(@Nullable Map.Entry<String, Integer> entry) {
                if (entry.getKey().length() <= 2) return false;
                if (!entry.getKey().matches("^[А-ЯІа-яі\u0027-]+$")) return false;
                return true;
            }
        }));
    }

    public void setCrawler(DocumentaryFrequencyCrawler crawler) {
        this.crawler = crawler;
    }

    public void setTerminRepo(TerminRepository terminRepo) {
        this.terminRepo = terminRepo;
    }
}
