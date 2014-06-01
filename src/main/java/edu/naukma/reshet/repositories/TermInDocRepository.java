package edu.naukma.reshet.repositories;


import edu.naukma.reshet.model.TermInDoc;
import edu.naukma.reshet.model.Termin;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TermInDocRepository extends MongoRepository<TermInDoc, String> {
  public TermInDoc findByTermin(Termin term);
  public List<TermInDoc> findByIndex(String index);

}

