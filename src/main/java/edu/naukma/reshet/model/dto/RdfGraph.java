package edu.naukma.reshet.model.dto;


import com.carrotsearch.ant.tasks.junit4.dependencies.com.google.common.collect.Lists;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.List;

public class RdfGraph extends JsonLDThesaurus{
  private String content;

  public RdfGraph(String content) {
    this.content = content;
  }


  private final Graph graph = new Graph();
  @JsonProperty(value = "@graph")
  public Graph getContent() {
    return graph;
  }

  class Graph{
     public final Thesaurus thesaurus = new Thesaurus();
  }
  class Thesaurus{
     @JsonProperty("concept")
     public List<Concept> concepts = Lists.newArrayList();
     @JsonProperty("relation")
     public List<HierarchicalRelationship> relations = Lists.newArrayList();

    public Thesaurus(){
       Concept c1 = new Concept("C1","люди");
       Concept c2 = new Concept("C2","діти");
       Concept c3 = new Concept("C3", "студенти");
       concepts.add(c1);
       concepts.add(c2);
       concepts.add(c3);
       relations.add(new HierarchicalRelationship("C1","C2",Relation.BT));
       relations.add(new HierarchicalRelationship("C2","C3",Relation.NT));
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
