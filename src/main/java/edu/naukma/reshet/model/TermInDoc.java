package edu.naukma.reshet.model;

import com.google.common.collect.Ordering;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.annotation.Nullable;

public class TermInDoc implements Comparable<TermInDoc>{
  @Id
  private String id;

  @DBRef
  private Termin termin;

  private Double tfidf;

  public TermInDoc() {}

  public TermInDoc(Termin termin, Double tfidf) {
    this.termin = termin;
    this.tfidf = tfidf;
  }

  @Override
  public String toString() {
    return String.format(
            "TermInDoc[id=%s, termin='%s', tfidf='%.12f']",
            id, getTermin().getText(), getTfidf());
  }



  public Termin getTermin() {
    return termin;
  }

  public void setTermin(Termin termin) {
    this.termin = termin;
  }

  public Double getTfidf() {
    return tfidf;
  }

  public void setTfidf(Double tfidf) {
    this.tfidf = tfidf;
  }

  @Override
  public int compareTo(TermInDoc other) {
    return ORDERING_BY_TFIDF.compare(this, other);
  }

  public static Ordering<TermInDoc> ORDERING_BY_TFIDF = new Ordering<TermInDoc>(){

    @Override
    public int compare(@Nullable TermInDoc termInDoc, @Nullable TermInDoc termInDoc2) {
      double value = termInDoc.tfidf - termInDoc2.tfidf;
      if(value == 0D) {
        return 0;
      }
      return value < 0 ? -1 : 1;
    }
  };
}
