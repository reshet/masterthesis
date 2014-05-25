package edu.naukma.reshet.core.algorithm.lexicography.pattern;

/**
 * Created by user on 5/25/14.
 */
public class ExactWordElement implements PatternElement{
    private final String [] synonyms;
    public ExactWordElement(String... synonyms) {
        this.synonyms = synonyms;
    }
}
