package edu.naukma.reshet.repositories;

import edu.naukma.reshet.model.TermRelation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermRelationRepository extends MongoRepository<TermRelation, String> {
}

