package com.codesentinel.backend.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class ImpactDTOs {

    public record ImpactAnalysisRequest(
            @NotBlank String baseCommit,
            @NotBlank String targetCommit
    ) {}

    public record ImpactSummaryDTO(
            String id,
            String repositoryId,
            String baseCommit,
            String targetCommit,
            double regressionRiskScore,
            String riskCategory,
            int directlyAffectedCount,
            int transitivelyAffectedCount,
            List<String> affectedTests,
            List<String> missingTests
    ) {}
}
