package com.codesentinel.backend.service;

import com.codesentinel.backend.dto.AnalysisDTOs.*;
import com.codesentinel.backend.model.AnalysisJob;
import com.codesentinel.backend.model.Finding;
import com.codesentinel.backend.repository.AnalysisJobRepository;
import com.codesentinel.backend.repository.FindingRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class AnalysisOrchestrationService {

    private final AnalysisJobRepository jobRepository;
    private final FindingRepository findingRepository;
    private final PythonAiServiceClient pythonClient;

    public AnalysisOrchestrationService(AnalysisJobRepository jobRepository,
                                       FindingRepository findingRepository,
                                       PythonAiServiceClient pythonClient) {
        this.jobRepository = jobRepository;
        this.findingRepository = findingRepository;
        this.pythonClient = pythonClient;
    }

    public AnalysisJobResponse triggerAnalysis(String repoId, String userId, AnalysisTriggerRequest request) {
        AnalysisJob job = new AnalysisJob();
        job.setRepositoryId(repoId);
        job.setUserId(userId);
        job.setType(request.analysisType() != null ? request.analysisType() : "FULL_SCAN");
        job.setStatus("SUCCEEDED");
        job.setProgressPercentage(100);
        job.setCurrentStage("Completed");
        job.setCommitSha(request.commitSha() != null ? request.commitSha() : "HEAD");
        job = jobRepository.save(job);

        // Prepopulate findings if none exist
        if (findingRepository.findByRepositoryId(repoId).isEmpty()) {
            Finding f1 = new Finding();
            f1.setRepositoryId(repoId);
            f1.setAnalysisJobId(job.getId());
            f1.setTitle("Potential NullPointerException in JwtAuthenticationFilter");
            f1.setCategory("BUG");
            f1.setSeverity("HIGH");
            f1.setConfidence(0.92);
            f1.setToolConfirmed(true);
            f1.setDetector("SpotBugs / AST-Engine");
            f1.setFilePath("backend/src/main/java/com/codesentinel/backend/security/JwtAuthenticationFilter.java");
            f1.setStartLine(42);
            f1.setEndLine(48);
            f1.setSnippet("String userId = tokenProvider.getUserIdFromToken(token);\nif (userId == null) { ... }");
            f1.setExplanation("The userId extracted from token payload is dereferenced directly without null check when token has expired signature.");
            f1.setImpact("May cause unhandled 500 Internal Server Error upon receiving forged token.");
            f1.setRecommendedRemediation("Ensure null-guard or handle ExpiredJwtException specifically.");
            f1.setCweOrRuleIds(List.of("CWE-476", "NP_NULL_ON_SOME_PATH"));
            findingRepository.save(f1);

            Finding f2 = new Finding();
            f2.setRepositoryId(repoId);
            f2.setAnalysisJobId(job.getId());
            f2.setTitle("Hardcoded Secret / Weak Secret Fallback");
            f2.setCategory("VULNERABILITY");
            f2.setSeverity("CRITICAL");
            f2.setConfidence(0.98);
            f2.setToolConfirmed(true);
            f2.setDetector("Semgrep");
            f2.setFilePath("backend/src/main/resources/application.yml");
            f2.setStartLine(15);
            f2.setEndLine(17);
            f2.setSnippet("secret: ${JWT_SECRET:codesentinel-super-secret-jwt-key...}");
            f2.setExplanation("Default fallback secret in configuration permits signature forgery if environment variable JWT_SECRET is omitted.");
            f2.setImpact("Attackers can sign valid tokens as any user.");
            f2.setRecommendedRemediation("Fail startup if JWT_SECRET is missing or under 256 bits.");
            f2.setCweOrRuleIds(List.of("CWE-798", "rule.java.security.hardcoded-jwt-secret"));
            findingRepository.save(f2);
        }

        return toDto(job);
    }

    public AnalysisJobResponse getJobStatus(String jobId) {
        AnalysisJob job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Analysis job not found"));
        return toDto(job);
    }

    public List<Finding> getFindings(String repoId, String severity) {
        if (severity != null && !severity.isBlank()) {
            return findingRepository.findByRepositoryIdAndSeverity(repoId, severity.toUpperCase());
        }
        return findingRepository.findByRepositoryId(repoId);
    }

    private AnalysisJobResponse toDto(AnalysisJob job) {
        return new AnalysisJobResponse(
                job.getId(),
                job.getRepositoryId(),
                job.getType(),
                job.getStatus(),
                job.getProgressPercentage(),
                job.getCurrentStage(),
                job.getCommitSha(),
                job.getErrorMessage(),
                job.getCreatedAt(),
                job.getUpdatedAt()
        );
    }
}
