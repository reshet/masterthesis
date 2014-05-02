package edu.naukma.reshet.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName(value = "searchInformation")
public class GoogleSearchResult {

  private String totalResults;

  public String getTotalResults() {
    return totalResults;
  }


}