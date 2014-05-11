package edu.naukma.reshet.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

public class Snippet {
  @Id
  private String id;

  private String text;

  @DBRef
  private TermInDoc term;

  private Long docId;

  public Snippet(String text, TermInDoc term, Long docId) {
    this.text = text;
    this.term = term;
    this.docId = docId;
  }

  public TermInDoc getTerm() {
    return term;
  }

  public void setTerm(TermInDoc term) {
    this.term = term;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Long getDocId() {
    return docId;
  }
}
