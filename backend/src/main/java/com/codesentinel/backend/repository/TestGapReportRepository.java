package com.codesentinel.backend.repository;

import com.codesentinel.backend.model.TestGapReport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestGapReportRepository extends MongoRepository<TestGapReport, String> {
    Optional<TestGapReport> findTopByRepositoryIdOrderByCreatedAtDesc(String repositoryId);
}
