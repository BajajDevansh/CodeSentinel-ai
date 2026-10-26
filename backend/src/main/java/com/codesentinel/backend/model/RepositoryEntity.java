package com.codesentinel.backend.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Document(collection = "repositories")
public class RepositoryEntity {
    @Id
    private String id;

    @Indexed
    private String userId;

    private String name;
    private String gitUrl;
    private String defaultBranch = "main";
    private String currentCommitSha;
    private String status = "PENDING"; // PENDING, CLONING, INDEXED, FAILED
    private int fileCount;
    private int linesOfCode;
    private Map<String, Integer> languageBreakdown = new HashMap<>();
    private String lastIndexedCommitSha;
    private Instant lastAnalyzedAt;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    public RepositoryEntity() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getGitUrl() { return gitUrl; }
    public void setGitUrl(String gitUrl) { this.gitUrl = gitUrl; }
    public String getDefaultBranch() { return defaultBranch; }
    public void setDefaultBranch(String defaultBranch) { this.defaultBranch = defaultBranch; }
    public String getCurrentCommitSha() { return currentCommitSha; }
    public void setCurrentCommitSha(String currentCommitSha) { this.currentCommitSha = currentCommitSha; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getFileCount() { return fileCount; }
    public void setFileCount(int fileCount) { this.fileCount = fileCount; }
    public int getLinesOfCode() { return linesOfCode; }
    public void setLinesOfCode(int linesOfCode) { this.linesOfCode = linesOfCode; }
    public Map<String, Integer> getLanguageBreakdown() { return languageBreakdown; }
    public void setLanguageBreakdown(Map<String, Integer> languageBreakdown) { this.languageBreakdown = languageBreakdown; }
    public String getLastIndexedCommitSha() { return lastIndexedCommitSha; }
    public void setLastIndexedCommitSha(String lastIndexedCommitSha) { this.lastIndexedCommitSha = lastIndexedCommitSha; }
    public Instant getLastAnalyzedAt() { return lastAnalyzedAt; }
    public void setLastAnalyzedAt(Instant lastAnalyzedAt) { this.lastAnalyzedAt = lastAnalyzedAt; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
