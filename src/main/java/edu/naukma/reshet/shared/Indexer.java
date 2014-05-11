package edu.naukma.reshet.shared;

import org.apache.lucene.document.Document;

/**
 * Created with IntelliJ IDEA.
 * User: reshet
 * Date: 3/9/14
 * Time: 7:21 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Indexer {
  public String  getStepName();
  public String indexDocuments();
  public Integer indexDocument(Document doc);
  public Integer docCount();
}
