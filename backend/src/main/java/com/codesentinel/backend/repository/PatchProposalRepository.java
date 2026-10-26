package com.codesentinel.backend.repository;

import com.codesentinel.backend.model.PatchProposal;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatchProposalRepository extends MongoRepository<PatchProposal, String> {
    List<PatchProposal> findByRepositoryId(String repositoryId);
}
