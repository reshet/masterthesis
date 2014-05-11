package edu.naukma.reshet.repositories;

import edu.naukma.reshet.model.TermInDoc;
import edu.naukma.reshet.model.TermRelation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TermRelationRepository extends MongoRepository<TermRelation, String> {
  public TermRelation findByTerm1AndTerm2AndRelationType(@Param(value = "term1") TermInDoc term1,@Param(value = "term2") TermInDoc term2, @Param(value = "relType") String relationType);
}

