package edu.naukma.reshet.core.algorithm;

import com.carrotsearch.ant.tasks.junit4.dependencies.com.google.common.collect.Lists;
import com.carrotsearch.ant.tasks.junit4.dependencies.com.google.common.collect.Ordering;
import edu.naukma.reshet.model.TermInDoc;
import edu.naukma.reshet.model.Termin;
import edu.naukma.reshet.shared.DocumentaryFrequencyCrawler;
import edu.naukma.reshet.shared.Searcher;
import edu.naukma.reshet.shared.algorithm.InitialTerminologyExtract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class TopTfIdfInitialTerminologyExtract implements InitialTerminologyExtract {

  @Autowired
  Searcher searcher;
  @Autowired
  DocumentaryFrequencyCrawler crawler;

  @Override
  public List<TermInDoc> extractValuableTerms(String repository) {
    List<TermInDoc> terms = Lists.newArrayList();
    Map<String, Integer> map = searcher.getFrequencies();
    for(String term: map.keySet()){
      Integer frequency =  map.get(term);
      Long docFreq = crawler.getDocumentaryFrequency(term);
      Double totalFreq = 1.0*frequency/docFreq;
      terms.add(new TermInDoc(new Termin(term, docFreq),totalFreq));
    }
    return Ordering
            .natural()
            .reverse()
            .immutableSortedCopy(terms);
  }
}
