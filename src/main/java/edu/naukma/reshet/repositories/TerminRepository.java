package edu.naukma.reshet.repositories;


import edu.naukma.reshet.model.Termin;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TerminRepository extends MongoRepository<Termin, String> {
  public Termin findByText(String text);
}

