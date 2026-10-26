package com.codesentinel.backend.dto;

import java.util.List;

public class TestIntelligenceDTOs {

    public record TestGenerateRequest(
            String targetFilePath,
            String targetMethodName,
            String framework // JUNIT5, PYTEST
    ) {}

    public record TestExecuteRequest(
            String testCaseId,
            boolean runInSandbox
    ) {}

    public record CoverageComparisonResponse(
            String repositoryId,
            double baselineLineCoverage,
            double baselineBranchCoverage,
            double postGenerationLineCoverage,
            double postGenerationBranchCoverage,
            double lineCoverageDelta,
            double branchCoverageDelta,
            int generatedCount,
            int passedCount,
            int failedCount,
            String executionLog
    ) {}
}
