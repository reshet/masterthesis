package edu.naukma.reshet.core.algorithm.lexicography;

/**
 * Created by user on 5/25/14.
 */
public class NounPhrase implements Comparable<NounPhrase>{
    final private String text;

    public NounPhrase(String text) {
        this.text = text;
    }

    @Override
    public int compareTo(NounPhrase o) {
        return this.getText().compareTo(o.getText());
    }

    public String getText() {
        return text;
    }
}
