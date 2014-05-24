package edu.naukma.reshet.model.dto.graph;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HierarchicalRelationship{
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
