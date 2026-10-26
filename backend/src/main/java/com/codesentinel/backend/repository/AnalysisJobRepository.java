package com.codesentinel.backend.repository;

import com.codesentinel.backend.model.AnalysisJob;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnalysisJobRepository extends MongoRepository<AnalysisJob, String> {
    List<AnalysisJob> findByRepositoryIdOrderByCreatedAtDesc(String repositoryId);
    Optional<AnalysisJob> findTopByRepositoryIdOrderByCreatedAtDesc(String repositoryId);
}
