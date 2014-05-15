package edu.naukma.reshet.model.dto;


import com.carrotsearch.ant.tasks.junit4.dependencies.com.google.common.collect.Lists;
import com.carrotsearch.ant.tasks.junit4.dependencies.com.google.common.collect.Maps;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import edu.naukma.reshet.model.TermInDoc;
import edu.naukma.reshet.model.TermRelation;

import java.util.List;
import java.util.Map;

public class RdfGraph extends JsonLDThesaurus{
  private String content;

  public RdfGraph(List<TermInDoc> terms, List<TermRelation> relations) {
    this.graph = new Graph(terms, relations);
  }


  private final Graph graph ;
  @JsonProperty(value = "@graph")
  public Graph getContent() {
    return graph;
  }

  class Graph{
    public final Thesaurus thesaurus;

    Graph(List<TermInDoc> terms, List<TermRelation> relations) {
      thesaurus = new Thesaurus(terms, relations);
    }
  }
  class Thesaurus{
     @JsonProperty("concept")
     public List<Concept> concepts = Lists.newArrayList();
     @JsonProperty("relation")
     public List<HierarchicalRelationship> relations = Lists.newArrayList();

    public Thesaurus(List<TermInDoc> terms, List<TermRelation> termRelations){
       Map<String, String> termsMap = Maps.newHashMap();
       Long tCount = 1L;
       for(TermInDoc term: terms){
         Concept c = new Concept("C"+tCount, term.getTermin().getText());
         termsMap.put(term.getId(), "C"+tCount);
         concepts.add(c);
         tCount++;
       }
       /*Concept c1 = new Concept("C1","люди");
       Concept c2 = new Concept("C2","діти");
       Concept c3 = new Concept("C3", "студенти");
       concepts.add(c1);
       concepts.add(c2);
       concepts.add(c3);*/
       for (TermRelation rel: termRelations){
         String term1id = rel.getTerm1().getId();
         String term2id = rel.getTerm2().getId();
         relations.add(
            new HierarchicalRelationship(termsMap.get(term1id),termsMap.get(term2id),Relation.RT)
         );
       }
       //relations.add(new HierarchicalRelationship("C1","C2",Relation.BT));
       //relations.add(new HierarchicalRelationship("C2","C3",Relation.NT));
   }
  }
  class Concept{
    @JsonProperty("@id")
    public final String id;
    @JsonProperty("lexicalValue")
    public final String text;
    public Concept(String id, String text){
      this.id = id;
      this.text = text;
    }
  }
  class Term{

  }
  class HierarchicalRelationship{
     @JsonProperty("role")
     public final Relation relation;
     public final String baseConcept;
     public final String depConcept;
     public HierarchicalRelationship(String base, String dep, Relation rel){
       this.baseConcept = base;
       this.depConcept = dep;
       relation = rel;
     }
  }
  enum Relation {RT,BT, NT}

}
