package com.codesentinel.backend.controller;

import com.codesentinel.backend.dto.TestIntelligenceDTOs.*;
import com.codesentinel.backend.model.TestGapReport;
import com.codesentinel.backend.service.TestIntelligenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/repositories/{repositoryId}")
@Tag(name = "Testing Intelligence", description = "Coverage-guided test gap analyzer and sandboxed execution")
public class TestIntelligenceController {

    private final TestIntelligenceService testService;

    public TestIntelligenceController(TestIntelligenceService testService) {
        this.testService = testService;
    }

    @GetMapping("/tests/gaps")
    @Operation(summary = "Get test gaps, uncovered targets, and generated test cases")
    public ResponseEntity<TestGapReport> getTestGaps(@PathVariable String repositoryId) {
        return ResponseEntity.ok(testService.getOrCreateReport(repositoryId));
    }

    @PostMapping("/tests/execute")
    @Operation(summary = "Execute generated tests in isolated sandbox and verify coverage delta")
    public ResponseEntity<CoverageComparisonResponse> executeTests(
            @PathVariable String repositoryId,
            @RequestBody(required = false) TestExecuteRequest request) {
        TestExecuteRequest req = request != null ? request : new TestExecuteRequest("all", true);
        return ResponseEntity.ok(testService.executeTests(repositoryId, req));
    }
}
