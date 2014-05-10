package edu.naukma.reshet.repositories;

import edu.naukma.reshet.model.TermRelation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(collectionResourceRel = "termrelations", path = "termrelations")
public interface TermRelationRestRepository extends MongoRepository<TermRelation, String> {
}

