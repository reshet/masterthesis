package edu.naukma.reshet.shared.algorithm;

import edu.naukma.reshet.model.TermInDoc;

import java.util.List;

public interface InitialTerminologyExtract {
  List<TermInDoc> extractValuableTerms(String repository);
}
