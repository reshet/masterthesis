package edu.naukma.reshet.core.algorithm.lexicography;


public class NounPhraseMatch{
    final private NounPhrase phrase;
    private final int position;

    public NounPhraseMatch(NounPhrase phrase, int position) {
        this.phrase = phrase;
        this.position = position;
    }

    @Override
    public String toString(){
        return this.phrase.getText() + ", "+position;
    }

    public int getPosition() {
        return position;
    }

    public NounPhrase getPhrase() {
        return phrase;
    }
}
