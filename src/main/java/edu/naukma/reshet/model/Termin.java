package edu.naukma.reshet.model;

import org.springframework.data.annotation.Id;

public class Termin {
  @Id
  private String id;

  private String text;
  private Long docFrequency;

  public Termin() {}

  public Termin(String text, Long docFrequency) {
    this.text = text;
    this.docFrequency = docFrequency;
  }

  @Override
  public String toString() {
    return String.format(
            "Termin[id=%s, text='%s', docFreq='%s']",
            id, text, docFrequency);
  }
}
