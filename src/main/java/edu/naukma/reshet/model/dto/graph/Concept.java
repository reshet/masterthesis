package edu.naukma.reshet.model.dto.graph;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Concept{
    @JsonProperty("@id")
    public final String id;
    @JsonProperty("lexicalValue")
    public final String text;
    public Concept(String id, String text){
        this.id = id;
        this.text = text;
    }
}