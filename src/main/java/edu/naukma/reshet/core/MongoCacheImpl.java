package edu.naukma.reshet.core;

import edu.naukma.reshet.model.Termin;
import edu.naukma.reshet.repositories.TerminRepository;
import edu.naukma.reshet.shared.MongoCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: reshet
 * Date: 5/2/14
 * Time: 12:46 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class MongoCacheImpl implements MongoCache{
  @Autowired
  TerminRepository repository;

  @Override
  public void saveTerm(Termin term) {
    repository.save(term);
  }

  @Override
  public Termin findTerm(String term) {
    return repository.findByText(term);
  }

}
