package edu.naukma.reshet.core.algorithm.lexicography.pattern;

import edu.naukma.reshet.core.algorithm.lexicography.NounPhrase;
import edu.naukma.reshet.core.algorithm.lexicography.NounPhraseMatch;

import java.util.List;

public interface PatternElement {
   List<NounPhraseMatch> apply(String subphrase);
}
