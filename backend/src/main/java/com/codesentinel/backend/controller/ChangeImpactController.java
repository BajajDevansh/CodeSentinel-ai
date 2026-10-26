package com.codesentinel.backend.controller;

import com.codesentinel.backend.dto.ImpactDTOs.*;
import com.codesentinel.backend.model.ChangeImpactReport;
import com.codesentinel.backend.service.ChangeImpactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/repositories/{repositoryId}/impact")
@Tag(name = "Change Impact", description = "Regression risk analysis and affected component graph")
public class ChangeImpactController {

    private final ChangeImpactService impactService;

    public ChangeImpactController(ChangeImpactService impactService) {
        this.impactService = impactService;
    }

    @PostMapping("/analyze")
    @Operation(summary = "Analyze commit diff impact on codebase and calculate risk score")
    public ResponseEntity<ChangeImpactReport> analyzeImpact(
            @PathVariable String repositoryId,
            @Valid @RequestBody ImpactAnalysisRequest request) {
        return ResponseEntity.ok(impactService.analyzeImpact(repositoryId, request));
    }

    @GetMapping("/latest")
    @Operation(summary = "Get latest change impact report")
    public ResponseEntity<ChangeImpactReport> getLatestReport(@PathVariable String repositoryId) {
        return ResponseEntity.ok(impactService.getLatestReport(repositoryId));
    }
}
