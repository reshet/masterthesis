package edu.naukma.reshet.shared.algorithm;

import edu.naukma.reshet.model.TerminInDoc;

import java.util.List;

public interface InitialTerminologyExtract {
  List<TerminInDoc> extractValuableTerms(String repository);
}
