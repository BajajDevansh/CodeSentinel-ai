package com.codesentinel.backend.repository;

import com.codesentinel.backend.model.Finding;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FindingRepository extends MongoRepository<Finding, String> {
    List<Finding> findByRepositoryId(String repositoryId);
    List<Finding> findByRepositoryIdAndSeverity(String repositoryId, String severity);
    List<Finding> findByRepositoryIdAndCategory(String repositoryId, String category);
    void deleteByRepositoryId(String repositoryId);
}
