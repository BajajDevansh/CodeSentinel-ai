package com.codesentinel.backend.controller;

import com.codesentinel.backend.dto.PatchDTOs.*;
import com.codesentinel.backend.model.PatchProposal;
import com.codesentinel.backend.service.PatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Patch Proposals", description = "Review, validate and approve refactoring patches")
public class PatchController {

    private final PatchService patchService;

    public PatchController(PatchService patchService) {
        this.patchService = patchService;
    }

    @PostMapping("/repositories/{repositoryId}/patches")
    @Operation(summary = "Propose patch for a detected finding")
    public ResponseEntity<PatchProposal> createPatch(
            @PathVariable String repositoryId,
            @Valid @RequestBody PatchCreateRequest request) {
        return ResponseEntity.ok(patchService.createPatchProposal(repoId(repositoryId), request));
    }

    @GetMapping("/repositories/{repositoryId}/patches")
    @Operation(summary = "List patch proposals for repository")
    public ResponseEntity<List<PatchProposal>> getPatches(@PathVariable String repositoryId) {
        return ResponseEntity.ok(patchService.getPatches(repoId(repositoryId)));
    }

    @PostMapping("/patches/{patchId}/approve")
    @Operation(summary = "Approve or reject patch proposal")
    public ResponseEntity<PatchProposal> approvePatch(
            @PathVariable String patchId,
            @Valid @RequestBody PatchApprovalRequest request) {
        return ResponseEntity.ok(patchService.approvePatch(patchId, request));
    }

    private String repoId(String r) { return r; }
}
