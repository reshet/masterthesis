package edu.naukma.reshet.core;

import edu.naukma.reshet.shared.Indexer;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: reshet
 * Date: 3/9/14
 * Time: 6:41 PM
 * To change this template use File | Settings | File Templates.
 */
@Component("indexer")
public class ConcreteIndexer implements Indexer {
  @Override
  public String  getStepName(){
    return "Index step";
  }

  @Override
  public String indexDocuments() {
    return "Indexed 334 documents";
  }
}
