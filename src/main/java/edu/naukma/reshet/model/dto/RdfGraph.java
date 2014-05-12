package edu.naukma.reshet.model.dto;

import org.springframework.hateoas.ResourceSupport;

/**
 * Created with IntelliJ IDEA.
 * User: reshet
 * Date: 5/11/14
 * Time: 11:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class RdfGraph extends ResourceSupport{
  private String content;

  public RdfGraph(String content) {
    this.content = content;
  }

  public String getContent() {
    return content;
  }
}
