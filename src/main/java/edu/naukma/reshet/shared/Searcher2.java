package edu.naukma.reshet.shared;

import edu.naukma.reshet.model.DocText;
import edu.naukma.reshet.model.TermInDoc;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: reshet
 * Date: 5/10/14
 * Time: 7:15 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Searcher2 {
  List<DocText> searchDocs(TermInDoc term);
}
