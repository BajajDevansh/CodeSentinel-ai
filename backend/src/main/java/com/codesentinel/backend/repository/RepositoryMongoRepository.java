package com.codesentinel.backend.repository;

import com.codesentinel.backend.model.RepositoryEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepositoryMongoRepository extends MongoRepository<RepositoryEntity, String> {
    List<RepositoryEntity> findByUserId(String userId);
    Optional<RepositoryEntity> findByIdAndUserId(String id, String userId);
}
