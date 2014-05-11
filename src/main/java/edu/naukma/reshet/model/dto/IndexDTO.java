package edu.naukma.reshet.model.dto;


import org.springframework.hateoas.ResourceSupport;

public class IndexDTO extends ResourceSupport{
  private final Long docCount;
  private final String name;
  public IndexDTO(Long docCount, String name){
    this.docCount = docCount;
    this.name = name;
  }
  public Long getDocCount(){
    return docCount;
  }

  public String getName() {
    return name;
  }
}
