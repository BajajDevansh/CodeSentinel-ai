package com.codesentinel.backend.dto;

import java.time.Instant;
import java.util.List;

public class AnalysisDTOs {

    public record AnalysisTriggerRequest(
            String analysisType, // FULL_SCAN, SECURITY_ONLY, TEST_GAP_ONLY, IMPACT_ONLY
            String commitSha
    ) {}

    public record AnalysisJobResponse(
            String id,
            String repositoryId,
            String type,
            String status,
            int progressPercentage,
            String currentStage,
            String commitSha,
            String errorMessage,
            Instant createdAt,
            Instant updatedAt
    ) {}

    public record FindingsSummaryResponse(
            String repositoryId,
            int totalFindings,
            int criticalCount,
            int highCount,
            int mediumCount,
            int lowCount,
            List<Object> findings
    ) {}
}
