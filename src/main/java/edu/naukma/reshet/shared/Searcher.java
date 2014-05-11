package edu.naukma.reshet.shared;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: reshet
 * Date: 3/18/14
 * Time: 10:20 AM
 * To change this template use File | Settings | File Templates.
 */
public interface Searcher {
   public List<String> search(String query);
   public List<String> getTerms();
   public Map<String, Integer> getFrequencies();
   public Map<String, Integer> getFrequencies(Integer docId);
}
