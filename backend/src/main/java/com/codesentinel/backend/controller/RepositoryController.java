package com.codesentinel.backend.controller;

import com.codesentinel.backend.dto.RepositoryDTOs.*;
import com.codesentinel.backend.service.RepositoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/repositories")
@Tag(name = "Repositories", description = "Connect, list, inspect, and delete GitHub repositories")
public class RepositoryController {

    private final RepositoryService repoService;

    public RepositoryController(RepositoryService repoService) {
        this.repoService = repoService;
    }

    @PostMapping
    @Operation(summary = "Connect a new repository")
    public ResponseEntity<RepositoryResponse> connectRepository(
            Authentication authentication,
            @Valid @RequestBody RepositoryCreateRequest request) {
        String userId = (String) authentication.getPrincipal();
        return ResponseEntity.ok(repoService.createRepository(userId, request));
    }

    @GetMapping
    @Operation(summary = "List user repositories")
    public ResponseEntity<List<RepositoryResponse>> listRepositories(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        return ResponseEntity.ok(repoService.getUserRepositories(userId));
    }

    @GetMapping("/{repositoryId}")
    @Operation(summary = "Get repository details")
    public ResponseEntity<RepositoryResponse> getRepository(
            Authentication authentication,
            @PathVariable String repositoryId) {
        String userId = (String) authentication.getPrincipal();
        return ResponseEntity.ok(repoService.getRepository(repositoryId, userId));
    }

    @DeleteMapping("/{repositoryId}")
    @Operation(summary = "Delete connected repository")
    public ResponseEntity<Void> deleteRepository(
            Authentication authentication,
            @PathVariable String repositoryId) {
        String userId = (String) authentication.getPrincipal();
        repoService.deleteRepository(repositoryId, userId);
        return ResponseEntity.noContent().build();
    }
}
