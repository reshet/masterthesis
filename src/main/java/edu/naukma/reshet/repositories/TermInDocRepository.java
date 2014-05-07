package edu.naukma.reshet.repositories;


import edu.naukma.reshet.model.TermInDoc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermInDocRepository extends MongoRepository<TermInDoc, String> {
}

