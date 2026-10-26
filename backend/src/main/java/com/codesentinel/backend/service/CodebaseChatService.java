package com.codesentinel.backend.service;

import com.codesentinel.backend.dto.ChatDTOs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CodebaseChatService {

    private static final Logger log = LoggerFactory.getLogger(CodebaseChatService.class);
    private final PythonAiServiceClient pythonClient;

    public CodebaseChatService(PythonAiServiceClient pythonClient) {
        this.pythonClient = pythonClient;
    }

    @SuppressWarnings("unchecked")
    public ChatResponse answerQuestion(String repoId, ChatRequest request) {
        String convId = request.conversationId() != null && !request.conversationId().isBlank()
                ? request.conversationId()
                : UUID.randomUUID().toString();

        log.info("Forwarding Q&A to AI Service for repoId: {}, question: {}", repoId, request.question());

        try {
            // 1. Send query to Python AI Engine
            Map<String, Object> aiResult = pythonClient.queryCodebase(
                    repoId,
                    request.question(),
                    request.commitSha() != null ? request.commitSha() : "HEAD"
            );

            String answer = (String) aiResult.getOrDefault("answer", "Unable to analyze repository.");
            boolean grounded = (boolean) aiResult.getOrDefault("grounded_in_evidence", true);

            // 2. Parse citations from AI Service
            List<CitationDTO> citations = new ArrayList<>();
            if (aiResult.containsKey("citations") && aiResult.get("citations") instanceof List) {
                List<Map<String, Object>> rawCitations = (List<Map<String, Object>>) aiResult.get("citations");
                for (Map<String, Object> c : rawCitations) {
                    citations.add(new CitationDTO(
                            (String) c.getOrDefault("file_path", "unknown"),
                            ((Number) c.getOrDefault("start_line", 1)).intValue(),
                            ((Number) c.getOrDefault("end_line", 1)).intValue(),
                            (String) c.getOrDefault("symbol_name", "Symbol"),
                            (String) c.getOrDefault("snippet", ""),
                            (String) c.getOrDefault("retrieval_method", "HYBRID")
                    ));
                }
            }

            return new ChatResponse(
                    UUID.randomUUID().toString(),
                    convId,
                    request.question(),
                    answer,
                    grounded,
                    citations,
                    Instant.now()
            );

        } catch (Exception e) {
            log.error("Error communicating with AI Service for Q&A: {}", e.getMessage());
            return new ChatResponse(
                    UUID.randomUUID().toString(),
                    convId,
                    request.question(),
                    "AI Service communication failed: " + e.getMessage(),
                    false,
                    List.of(),
                    Instant.now()
            );
        }
    }
}