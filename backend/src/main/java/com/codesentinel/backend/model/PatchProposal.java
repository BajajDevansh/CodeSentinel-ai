package com.codesentinel.backend.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "patch_proposals")
public class PatchProposal {
    @Id
    private String id;

    @Indexed
    private String repositoryId;

    private String findingId;
    private String title;
    private String targetFilePath;
    private String originalSnippet;
    private String proposedSnippet;
    private String diffUnified;
    private String explanation;
    private String status = "PENDING"; // PENDING, VALIDATED, APPROVED, REJECTED, APPLIED
    private boolean testValidationPassed;
    private String validationOutput;

    @CreatedDate
    private Instant createdAt;

    public PatchProposal() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getRepositoryId() { return repositoryId; }
    public void setRepositoryId(String repositoryId) { this.repositoryId = repositoryId; }
    public String getFindingId() { return findingId; }
    public void setFindingId(String findingId) { this.findingId = findingId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getTargetFilePath() { return targetFilePath; }
    public void setTargetFilePath(String targetFilePath) { this.targetFilePath = targetFilePath; }
    public String getOriginalSnippet() { return originalSnippet; }
    public void setOriginalSnippet(String originalSnippet) { this.originalSnippet = originalSnippet; }
    public String getProposedSnippet() { return proposedSnippet; }
    public void setProposedSnippet(String proposedSnippet) { this.proposedSnippet = proposedSnippet; }
    public String getDiffUnified() { return diffUnified; }
    public void setDiffUnified(String diffUnified) { this.diffUnified = diffUnified; }
    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public boolean isTestValidationPassed() { return testValidationPassed; }
    public void setTestValidationPassed(boolean testValidationPassed) { this.testValidationPassed = testValidationPassed; }
    public String getValidationOutput() { return validationOutput; }
    public void setValidationOutput(String validationOutput) { this.validationOutput = validationOutput; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
