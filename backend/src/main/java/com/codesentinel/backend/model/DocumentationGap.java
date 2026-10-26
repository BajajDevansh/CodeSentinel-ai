package com.codesentinel.backend.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "documentation_gaps")
public class DocumentationGap {
    @Id
    private String id;

    @Indexed
    private String repositoryId;

    private String symbolName;
    private String symbolType; // CLASS, METHOD, API_ENDPOINT, CONFIG_KEY
    private String filePath;
    private int lineNumber;
    private String priority; // HIGH, MEDIUM, LOW
    private String reason; // MISSING_DOC, STALE_DOC, INCOMPLETE_PARAM_SPEC
    private String proposedDocumentation;
    private String status = "PENDING"; // PENDING, ACCEPTED, REJECTED, EDITED

    @CreatedDate
    private Instant createdAt;

    public DocumentationGap() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getRepositoryId() { return repositoryId; }
    public void setRepositoryId(String repositoryId) { this.repositoryId = repositoryId; }
    public String getSymbolName() { return symbolName; }
    public void setSymbolName(String symbolName) { this.symbolName = symbolName; }
    public String getSymbolType() { return symbolType; }
    public void setSymbolType(String symbolType) { this.symbolType = symbolType; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public int getLineNumber() { return lineNumber; }
    public void setLineNumber(int lineNumber) { this.lineNumber = lineNumber; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getProposedDocumentation() { return proposedDocumentation; }
    public void setProposedDocumentation(String proposedDocumentation) { this.proposedDocumentation = proposedDocumentation; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
