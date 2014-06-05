package edu.naukma.reshet.model.dto.graph;

import com.carrotsearch.ant.tasks.junit4.dependencies.com.google.common.collect.Lists;
import com.carrotsearch.ant.tasks.junit4.dependencies.com.google.common.collect.Maps;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.naukma.reshet.model.TermInDoc;
import edu.naukma.reshet.model.TermRelation;

import java.util.List;
import java.util.Map;

public class ThesaurusHuman extends Thesaurus {
    @JsonProperty("concept")
    public List<Concept> concepts = Lists.newArrayList();
    @JsonProperty("relation")
    public List<HierarchicalRelationship> relations = Lists.newArrayList();

    public ThesaurusHuman(List<TermInDoc> terms, List<TermRelation> termRelations){
        Map<String, String> termsMap = Maps.newHashMap();
        Long tCount = 1L;
//        for(TermInDoc term: terms){
//            Concept c = new Concept("C"+tCount, term.getTermin().getText());
//            termsMap.put(term.getId(), "C"+tCount);
//            concepts.add(c);
//            tCount++;
//        }
        for (TermRelation rel: termRelations){
            String term1id = rel.getTerm1().getId();
            String term2id = rel.getTerm2().getId();
            Relation rela = Relation.RT;
            if(rel.getRelationType().equals("BT")){
                rela = Relation.BT;
            }
            if(rel.getRelationType().equals("NT")){
                rela = Relation.NT;
            }
            Concept c = new Concept(rel.getTerm1().getTermin().getText(), rel.getTerm1().getTermin().getText());
            termsMap.put(term1id, rel.getTerm1().getTermin().getText());
            concepts.add(c);
            tCount++;
            Concept c2 = new Concept(rel.getTerm2().getTermin().getText(), rel.getTerm2().getTermin().getText());
            termsMap.put(term2id, rel.getTerm2().getTermin().getText());
            concepts.add(c2);
            tCount++;
            relations.add(
                    new HierarchicalRelationship(termsMap.get(term1id),termsMap.get(term2id),rela)
            );
        }
        //relations.add(new HierarchicalRelationship("C1","C2",Relation.BT));
        //relations.add(new HierarchicalRelationship("C2","C3",Relation.NT));
    }
}
