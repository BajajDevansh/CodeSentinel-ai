package com.codesentinel.backend.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "findings")
public class Finding {
    @Id
    private String id;

    @Indexed
    private String repositoryId;

    @Indexed
    private String analysisJobId;

    private String title;
    private String category; // CODE_SMELL, BUG, VULNERABILITY, MAINTAINABILITY
    private String severity; // CRITICAL, HIGH, MEDIUM, LOW, INFO
    private double confidence; // 0.0 - 1.0
    private boolean toolConfirmed; // true if static tool detected, false if purely LLM hypothesis
    private String detector; // e.g., Semgrep, PMD, SpotBugs, AST-Engine, Bandit
    private String filePath;
    private int startLine;
    private int endLine;
    private String snippet;
    private String explanation;
    private String impact;
    private String recommendedRemediation;
    private List<String> cweOrRuleIds;

    @CreatedDate
    private Instant createdAt;

    public Finding() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getRepositoryId() { return repositoryId; }
    public void setRepositoryId(String repositoryId) { this.repositoryId = repositoryId; }
    public String getAnalysisJobId() { return analysisJobId; }
    public void setAnalysisJobId(String analysisJobId) { this.analysisJobId = analysisJobId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    public double getConfidence() { return confidence; }
    public void setConfidence(double confidence) { this.confidence = confidence; }
    public boolean isToolConfirmed() { return toolConfirmed; }
    public void setToolConfirmed(boolean toolConfirmed) { this.toolConfirmed = toolConfirmed; }
    public String getDetector() { return detector; }
    public void setDetector(String detector) { this.detector = detector; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public int getStartLine() { return startLine; }
    public void setStartLine(int startLine) { this.startLine = startLine; }
    public int getEndLine() { return endLine; }
    public void setEndLine(int endLine) { this.endLine = endLine; }
    public String getSnippet() { return snippet; }
    public void setSnippet(String snippet) { this.snippet = snippet; }
    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }
    public String getImpact() { return impact; }
    public void setImpact(String impact) { this.impact = impact; }
    public String getRecommendedRemediation() { return recommendedRemediation; }
    public void setRecommendedRemediation(String recommendedRemediation) { this.recommendedRemediation = recommendedRemediation; }
    public List<String> getCweOrRuleIds() { return cweOrRuleIds; }
    public void setCweOrRuleIds(List<String> cweOrRuleIds) { this.cweOrRuleIds = cweOrRuleIds; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
