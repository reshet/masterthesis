package edu.naukma.reshet.core.algorithm;

import com.carrotsearch.ant.tasks.junit4.dependencies.com.google.common.collect.Lists;
import com.carrotsearch.ant.tasks.junit4.dependencies.com.google.common.collect.Ordering;
import edu.naukma.reshet.model.TermInDoc;
import edu.naukma.reshet.model.Termin;
import edu.naukma.reshet.repositories.TerminRepository;
import edu.naukma.reshet.shared.DocumentaryFrequencyCrawler;
import edu.naukma.reshet.shared.Searcher;
import edu.naukma.reshet.shared.algorithm.InitialTerminologyExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class TopTfIdfInitialTerminologyExtractor implements InitialTerminologyExtractor {

  @Autowired
  DocumentaryFrequencyCrawler crawler;

  @Autowired
  TerminRepository terminRepo;

  @Override
  public List<TermInDoc> extractValuableTerms(Searcher searcher, Integer docId) {
    List<TermInDoc> terms = Lists.newArrayList();
    Map<String, Integer> map = searcher.getFrequencies(docId);
    for(String term: map.keySet()){
      Integer frequency =  map.get(term);
      Long docFreq = crawler.getDocumentaryFrequency(term);
      Double totalFreq = 1.0*frequency/docFreq;
      terms.add(new TermInDoc(terminRepo.findByText(term),totalFreq));
    }
    return Ordering
            .natural()
            .reverse()
            .immutableSortedCopy(terms);
  }
}
