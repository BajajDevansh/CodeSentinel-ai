package com.codesentinel.backend.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;

public class ChatDTOs {

    public record ChatRequest(
            @NotBlank String question,
            String conversationId,
            String commitSha
    ) {}

    public record CitationDTO(
            String filePath,
            int startLine,
            int endLine,
            String symbolName,
            String snippet,
            String retrievalMethod // KEYWORD, SEMANTIC, GRAPH, HYBRID
    ) {}

    public record ChatResponse(
            String id,
            String conversationId,
            String question,
            String answer,
            boolean groundedInEvidence,
            List<CitationDTO> citations,
            Instant timestamp
    ) {}
}
