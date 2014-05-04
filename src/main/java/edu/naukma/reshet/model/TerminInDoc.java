package edu.naukma.reshet.model;

import com.google.common.collect.Ordering;
import org.springframework.data.annotation.Id;

import javax.annotation.Nullable;

public class TerminInDoc implements Comparable<TerminInDoc>{
  @Id
  private String id;

  private Termin termin;
  private Double tfidf;

  public TerminInDoc() {}

  public TerminInDoc(Termin termin, Double tfidf) {
    this.termin = termin;
    this.tfidf = tfidf;
  }

  @Override
  public String toString() {
    return String.format(
            "TerminInDoc[id=%s, termin='%s', tfidf='%.12f']",
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
  public int compareTo(TerminInDoc other) {
    return ORDERING_BY_TFIDF.compare(this, other);
  }

  public static Ordering<TerminInDoc> ORDERING_BY_TFIDF = new Ordering<TerminInDoc>(){

    @Override
    public int compare(@Nullable TerminInDoc terminInDoc, @Nullable TerminInDoc terminInDoc2) {
      double value = terminInDoc.tfidf - terminInDoc2.tfidf;
      if(value == 0D) {
        return 0;
      }
      return value < 0 ? -1 : 1;
    }
  };
}
