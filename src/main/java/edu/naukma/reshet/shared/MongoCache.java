package edu.naukma.reshet.shared;

import edu.naukma.reshet.model.Termin;


/**
 * Created with IntelliJ IDEA.
 * User: reshet
 * Date: 5/2/14
 * Time: 12:45 PM
 * To change this template use File | Settings | File Templates.
 */
public interface MongoCache {
  public void saveTerm(Termin term);
  public Termin findTerm(String term);
}
