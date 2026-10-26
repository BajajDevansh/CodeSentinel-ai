package com.codesentinel.backend.controller;

import com.codesentinel.backend.dto.AnalysisDTOs.*;
import com.codesentinel.backend.model.Finding;
import com.codesentinel.backend.service.AnalysisOrchestrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Analysis", description = "Trigger and inspect code quality and security analyses")
public class AnalysisController {

    private final AnalysisOrchestrationService analysisService;

    public AnalysisController(AnalysisOrchestrationService analysisService) {
        this.analysisService = analysisService;
    }

    @PostMapping("/repositories/{repositoryId}/analyses")
    @Operation(summary = "Trigger full repository scan")
    public ResponseEntity<AnalysisJobResponse> triggerAnalysis(
            Authentication authentication,
            @PathVariable String repositoryId,
            @RequestBody(required = false) AnalysisTriggerRequest request) {
        String userId = (String) authentication.getPrincipal();
        AnalysisTriggerRequest req = request != null ? request : new AnalysisTriggerRequest("FULL_SCAN", "HEAD");
        return ResponseEntity.ok(analysisService.triggerAnalysis(repositoryId, userId, req));
    }

    @GetMapping("/analyses/{analysisId}/status")
    @Operation(summary = "Get analysis job status")
    public ResponseEntity<AnalysisJobResponse> getStatus(@PathVariable String analysisId) {
        return ResponseEntity.ok(analysisService.getJobStatus(analysisId));
    }

    @GetMapping("/repositories/{repositoryId}/findings")
    @Operation(summary = "List repository findings")
    public ResponseEntity<List<Finding>> getFindings(
            @PathVariable String repositoryId,
            @RequestParam(required = false) String severity) {
        return ResponseEntity.ok(analysisService.getFindings(repositoryId, severity));
    }
}
