package com.codesentinel.backend.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "change_impact_reports")
public class ChangeImpactReport {
    @Id
    private String id;

    @Indexed
    private String repositoryId;

    private String baseCommit;
    private String targetCommit;
    private double regressionRiskScore; // 0 - 100
    private String riskCategory; // LOW, MEDIUM, HIGH, CRITICAL
    private int directlyAffectedCount;
    private int transitivelyAffectedCount;

    private List<RiskFactor> riskFactors = new ArrayList<>();
    private List<AffectedComponent> affectedComponents = new ArrayList<>();
    private List<String> affectedTests = new ArrayList<>();
    private List<String> missingTests = new ArrayList<>();
    private List<String> validationChecklist = new ArrayList<>();

    @CreatedDate
    private Instant createdAt;

    public ChangeImpactReport() {}

    public static class RiskFactor {
        private String name;
        private double weight;
        private String explanation;

        public RiskFactor() {}
        public RiskFactor(String name, double weight, String explanation) {
            this.name = name;
            this.weight = weight;
            this.explanation = explanation;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public double getWeight() { return weight; }
        public void setWeight(double weight) { this.weight = weight; }
        public String getExplanation() { return explanation; }
        public void setExplanation(String explanation) { this.explanation = explanation; }
    }

    public static class AffectedComponent {
        private String name;
        private String type; // CLASS, METHOD, API, CONFIG
        private String filePath;
        private String impactType; // DIRECT, TRANSITIVE
        private int dependencyDepth;

        public AffectedComponent() {}
        public AffectedComponent(String name, String type, String filePath, String impactType, int dependencyDepth) {
            this.name = name;
            this.type = type;
            this.filePath = filePath;
            this.impactType = impactType;
            this.dependencyDepth = dependencyDepth;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getFilePath() { return filePath; }
        public void setFilePath(String filePath) { this.filePath = filePath; }
        public String getImpactType() { return impactType; }
        public void setImpactType(String impactType) { this.impactType = impactType; }
        public int getDependencyDepth() { return dependencyDepth; }
        public void setDependencyDepth(int dependencyDepth) { this.dependencyDepth = dependencyDepth; }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getRepositoryId() { return repositoryId; }
    public void setRepositoryId(String repositoryId) { this.repositoryId = repositoryId; }
    public String getBaseCommit() { return baseCommit; }
    public void setBaseCommit(String baseCommit) { this.baseCommit = baseCommit; }
    public String getTargetCommit() { return targetCommit; }
    public void setTargetCommit(String targetCommit) { this.targetCommit = targetCommit; }
    public double getRegressionRiskScore() { return regressionRiskScore; }
    public void setRegressionRiskScore(double regressionRiskScore) { this.regressionRiskScore = regressionRiskScore; }
    public String getRiskCategory() { return riskCategory; }
    public void setRiskCategory(String riskCategory) { this.riskCategory = riskCategory; }
    public int getDirectlyAffectedCount() { return directlyAffectedCount; }
    public void setDirectlyAffectedCount(int directlyAffectedCount) { this.directlyAffectedCount = directlyAffectedCount; }
    public int getTransitivelyAffectedCount() { return transitivelyAffectedCount; }
    public void setTransitivelyAffectedCount(int transitivelyAffectedCount) { this.transitivelyAffectedCount = transitivelyAffectedCount; }
    public List<RiskFactor> getRiskFactors() { return riskFactors; }
    public void setRiskFactors(List<RiskFactor> riskFactors) { this.riskFactors = riskFactors; }
    public List<AffectedComponent> getAffectedComponents() { return affectedComponents; }
    public void setAffectedComponents(List<AffectedComponent> affectedComponents) { this.affectedComponents = affectedComponents; }
    public List<String> getAffectedTests() { return affectedTests; }
    public void setAffectedTests(List<String> affectedTests) { this.affectedTests = affectedTests; }
    public List<String> getMissingTests() { return missingTests; }
    public void setMissingTests(List<String> missingTests) { this.missingTests = missingTests; }
    public List<String> getValidationChecklist() { return validationChecklist; }
    public void setValidationChecklist(List<String> validationChecklist) { this.validationChecklist = validationChecklist; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
