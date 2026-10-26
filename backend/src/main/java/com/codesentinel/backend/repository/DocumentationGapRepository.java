package com.codesentinel.backend.repository;

import com.codesentinel.backend.model.DocumentationGap;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentationGapRepository extends MongoRepository<DocumentationGap, String> {
    List<DocumentationGap> findByRepositoryId(String repositoryId);
    void deleteByRepositoryId(String repositoryId);
}
