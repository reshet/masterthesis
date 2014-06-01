package edu.naukma.reshet.model;

import org.springframework.data.annotation.Id;

public class Termin {
  @Id
  private String id;

  private String text;
  private Double docFrequency;

  public Termin() {}

  public Termin(String text, Double docFrequency) {
    this.setText(text);
    this.setDocFrequency(docFrequency);
  }

  @Override
  public String toString() {
    return String.format(
            "Termin[id=%s, text='%s', docFreq='%s']",
            id, getText(), getDocFrequency());
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Double getDocFrequency() {
    return docFrequency;
  }

  public void setDocFrequency(Double docFrequency) {
    this.docFrequency = docFrequency;
  }
}
