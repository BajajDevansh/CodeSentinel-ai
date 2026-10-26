package com.codesentinel.backend.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.Map;

public class RepositoryDTOs {

    public record RepositoryCreateRequest(
            @NotBlank String name,
            @NotBlank String gitUrl,
            String defaultBranch,
            String githubToken
    ) {}

    public record RepositoryResponse(
            String id,
            String name,
            String gitUrl,
            String defaultBranch,
            String currentCommitSha,
            String status,
            int fileCount,
            int linesOfCode,
            Map<String, Integer> languageBreakdown,
            Instant lastAnalyzedAt,
            Instant createdAt
    ) {}
}
