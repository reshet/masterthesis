package edu.naukma.reshet.model;

/**
 * Created with IntelliJ IDEA.
 * User: reshet
 * Date: 5/10/14
 * Time: 7:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocText {
  private String text;
  private Long docID;
  private TermInDoc containsTerm;

  public DocText(String text, Long docID, TermInDoc containsTerm) {
    this.text = text;
    this.docID = docID;
    this.containsTerm = containsTerm;
  }

  public String getText() {
    return text;
  }

  public Long getDocID() {
    return docID;
  }

  public TermInDoc getContainsTerm() {
    return containsTerm;
  }
}
