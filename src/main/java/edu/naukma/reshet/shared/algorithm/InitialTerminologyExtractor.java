package edu.naukma.reshet.shared.algorithm;

import edu.naukma.reshet.model.TermInDoc;
import edu.naukma.reshet.shared.Searcher;

import java.util.List;

public interface InitialTerminologyExtractor {
  List<TermInDoc> extractValuableTerms(Searcher searcher, Integer docId);
}
