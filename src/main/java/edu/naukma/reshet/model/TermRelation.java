package edu.naukma.reshet.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

public class TermRelation {
  @Id
  private String id;

  @DBRef
  private TermInDoc term1;

  @DBRef
  private TermInDoc term2;

  private String relationType;

  public TermRelation(TermInDoc term1, TermInDoc term2, String relationType) {
    this.term1 = term1;
    this.term2 = term2;
    this.relationType = relationType;
  }


  @Override
  public String toString() {
    return String.format(
            "TermRelation[id=%s, type=%s, term1='%s', term2='%s']",
            id, relationType, this.getTerm1(), this.getTerm2());
  }


  public String getRelationType() {
    return relationType;
  }

  public TermInDoc getTerm1() {
    return term1;
  }

  public TermInDoc getTerm2() {
    return term2;
  }
}
