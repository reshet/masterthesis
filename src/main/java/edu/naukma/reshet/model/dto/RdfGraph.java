package edu.naukma.reshet.model.dto;


import com.carrotsearch.ant.tasks.junit4.dependencies.com.google.common.collect.Lists;
import com.carrotsearch.ant.tasks.junit4.dependencies.com.google.common.collect.Maps;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import edu.naukma.reshet.model.TermInDoc;
import edu.naukma.reshet.model.TermRelation;
import edu.naukma.reshet.model.dto.graph.Graph;

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

}
