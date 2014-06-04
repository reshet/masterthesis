package edu.naukma.reshet.core.algorithm.lexicography;


import java.util.List;

public class NounPhraseCompoundMatch extends NounPhraseMatch{

    final private List<NounPhraseMatch> children;
    public NounPhraseCompoundMatch(List<NounPhraseMatch> children) {
        super(new NounPhrase(buildPhrase(children)), children.get(0).getPosition());
        this.children = children;
    }
    private static String buildPhrase(List<NounPhraseMatch> children){
        StringBuilder text = new StringBuilder("");
        for(NounPhraseMatch npm: children){
            text.append(npm.getPhrase().getText());
        }
        return text.toString();
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

    public List<NounPhraseMatch> getChildren() {
        return children;
    }
}
