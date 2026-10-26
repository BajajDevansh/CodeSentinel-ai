package com.codesentinel.backend.controller;

import com.codesentinel.backend.model.DocumentationGap;
import com.codesentinel.backend.service.DocumentationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/repositories/{repositoryId}/documentation")
@Tag(name = "Documentation", description = "Documentation generation and documentation gap analyzer")
public class DocumentationController {

    private final DocumentationService docService;

    public DocumentationController(DocumentationService docService) {
        this.docService = docService;
    }

    @GetMapping("/gaps")
    @Operation(summary = "Detect documentation gaps and missing symbol docs")
    public ResponseEntity<List<DocumentationGap>> getGaps(@PathVariable String repositoryId) {
        return ResponseEntity.ok(docService.getDocumentationGaps(repositoryId));
    }

    @PostMapping("/generate")
    @Operation(summary = "Generate architecture and module markdown documentation")
    public ResponseEntity<Map<String, String>> generateDocs(@PathVariable String repositoryId) {
        String markdown = docService.generateOverviewMarkdown(repositoryId);
        return ResponseEntity.ok(Map.of("markdown", markdown));
    }
}
