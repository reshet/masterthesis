package edu.naukma.reshet.model.dto.graph;

import edu.naukma.reshet.model.TermInDoc;
import edu.naukma.reshet.model.TermRelation;

import java.util.List;

/**
 * Created by user on 5/19/14.
 */
public class Graph {
    public final Thesaurus thesaurus;

    public Graph(List<TermInDoc> terms, List<TermRelation> relations, Thesaurus.Type type) {
        if(type == Thesaurus.Type.BASIC){
            thesaurus = new Thesaurus(terms, relations);
        } else
        if(type == Thesaurus.Type.COMPACT){
            thesaurus = new ThesaurusCompact(terms, relations);
        }else
        if(type == Thesaurus.Type.HUMAN){
            thesaurus = new ThesaurusHuman(terms, relations);
        }else{
            thesaurus = new Thesaurus(terms, relations);
        }
    }
}
