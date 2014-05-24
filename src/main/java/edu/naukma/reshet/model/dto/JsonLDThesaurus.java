package edu.naukma.reshet.model.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonLDThesaurus {

  private final Context context = new Context();
  @JsonProperty("@context")
  public Object getContext(){
    return context;
  }

  class Context{
     public final String iso25964 = "http://www.niso.org/schemas/iso25964/iso25964-1_v1.4.xsd#";
     public final String thesaurus = "iso25964:Thesaurus";
     public final String concept = "iso25964:ThesaurusConcept";
     public final String relation = "iso25964:HierarchicalRelationship";
     public final String role = "iso25964:role";
     public final String baseConcept = "iso25964:isHierRelConcept";
     public final String depConcept = "iso25964:hasHierRelConcept";
     public final String lexicalValue = "iso25964:lexicalValue";

  }
}
