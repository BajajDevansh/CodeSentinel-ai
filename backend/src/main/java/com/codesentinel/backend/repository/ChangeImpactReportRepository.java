package com.codesentinel.backend.repository;

import com.codesentinel.backend.model.ChangeImpactReport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChangeImpactReportRepository extends MongoRepository<ChangeImpactReport, String> {
    Optional<ChangeImpactReport> findTopByRepositoryIdOrderByCreatedAtDesc(String repositoryId);
}
