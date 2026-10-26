package com.codesentinel.backend.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "test_gap_reports")
public class TestGapReport {
    @Id
    private String id;

    @Indexed
    private String repositoryId;

    private String commitSha;
    private double baselineLineCoverage;
    private double baselineBranchCoverage;
    private double postGenerationLineCoverage;
    private double postGenerationBranchCoverage;
    private int uncoveredMethodsCount;
    private int generatedTestCount;
    private int testsPassed;
    private int testsFailed;
    private String executionStatus; // READY, EXECUTING, PASSED, FAILED

    private List<UncoveredTarget> uncoveredTargets = new ArrayList<>();
    private List<GeneratedTestCase> generatedTestCases = new ArrayList<>();

    @CreatedDate
    private Instant createdAt;

    public TestGapReport() {}

    public static class UncoveredTarget {
        private String className;
        private String methodName;
        private String filePath;
        private int startLine;
        private int endLine;
        private String riskLevel; // HIGH, MEDIUM, LOW

        public UncoveredTarget() {}
        public UncoveredTarget(String className, String methodName, String filePath, int startLine, int endLine, String riskLevel) {
            this.className = className;
            this.methodName = methodName;
            this.filePath = filePath;
            this.startLine = startLine;
            this.endLine = endLine;
            this.riskLevel = riskLevel;
        }

        public String getClassName() { return className; }
        public void setClassName(String className) { this.className = className; }
        public String getMethodName() { return methodName; }
        public void setMethodName(String methodName) { this.methodName = methodName; }
        public String getFilePath() { return filePath; }
        public void setFilePath(String filePath) { this.filePath = filePath; }
        public int getStartLine() { return startLine; }
        public void setStartLine(int startLine) { this.startLine = startLine; }
        public int getEndLine() { return endLine; }
        public void setEndLine(int endLine) { this.endLine = endLine; }
        public String getRiskLevel() { return riskLevel; }
        public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    }

    public static class GeneratedTestCase {
        private String testName;
        private String targetMethod;
        private String targetFile;
        private String testCode;
        private boolean executed;
        private boolean passed;
        private String outputLog;

        public GeneratedTestCase() {}
        public GeneratedTestCase(String testName, String targetMethod, String targetFile, String testCode) {
            this.testName = testName;
            this.targetMethod = targetMethod;
            this.targetFile = targetFile;
            this.testCode = testCode;
        }

        public String getTestName() { return testName; }
        public void setTestName(String testName) { this.testName = testName; }
        public String getTargetMethod() { return targetMethod; }
        public void setTargetMethod(String targetMethod) { this.targetMethod = targetMethod; }
        public String getTargetFile() { return targetFile; }
        public void setTargetFile(String targetFile) { this.targetFile = targetFile; }
        public String getTestCode() { return testCode; }
        public void setTestCode(String testCode) { this.testCode = testCode; }
        public boolean isExecuted() { return executed; }
        public void setExecuted(boolean executed) { this.executed = executed; }
        public boolean isPassed() { return passed; }
        public void setPassed(boolean passed) { this.passed = passed; }
        public String getOutputLog() { return outputLog; }
        public void setOutputLog(String outputLog) { this.outputLog = outputLog; }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getRepositoryId() { return repositoryId; }
    public void setRepositoryId(String repositoryId) { this.repositoryId = repositoryId; }
    public String getCommitSha() { return commitSha; }
    public void setCommitSha(String commitSha) { this.commitSha = commitSha; }
    public double getBaselineLineCoverage() { return baselineLineCoverage; }
    public void setBaselineLineCoverage(double baselineLineCoverage) { this.baselineLineCoverage = baselineLineCoverage; }
    public double getBaselineBranchCoverage() { return baselineBranchCoverage; }
    public void setBaselineBranchCoverage(double baselineBranchCoverage) { this.baselineBranchCoverage = baselineBranchCoverage; }
    public double getPostGenerationLineCoverage() { return postGenerationLineCoverage; }
    public void setPostGenerationLineCoverage(double postGenerationLineCoverage) { this.postGenerationLineCoverage = postGenerationLineCoverage; }
    public double getPostGenerationBranchCoverage() { return postGenerationBranchCoverage; }
    public void setPostGenerationBranchCoverage(double postGenerationBranchCoverage) { this.postGenerationBranchCoverage = postGenerationBranchCoverage; }
    public int getUncoveredMethodsCount() { return uncoveredMethodsCount; }
    public void setUncoveredMethodsCount(int uncoveredMethodsCount) { this.uncoveredMethodsCount = uncoveredMethodsCount; }
    public int getGeneratedTestCount() { return generatedTestCount; }
    public void setGeneratedTestCount(int generatedTestCount) { this.generatedTestCount = generatedTestCount; }
    public int getTestsPassed() { return testsPassed; }
    public void setTestsPassed(int testsPassed) { this.testsPassed = testsPassed; }
    public int getTestsFailed() { return testsFailed; }
    public void setTestsFailed(int testsFailed) { this.testsFailed = testsFailed; }
    public String getExecutionStatus() { return executionStatus; }
    public void setExecutionStatus(String executionStatus) { this.executionStatus = executionStatus; }
    public List<UncoveredTarget> getUncoveredTargets() { return uncoveredTargets; }
    public void setUncoveredTargets(List<UncoveredTarget> uncoveredTargets) { this.uncoveredTargets = uncoveredTargets; }
    public List<GeneratedTestCase> getGeneratedTestCases() { return generatedTestCases; }
    public void setGeneratedTestCases(List<GeneratedTestCase> generatedTestCases) { this.generatedTestCases = generatedTestCases; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
