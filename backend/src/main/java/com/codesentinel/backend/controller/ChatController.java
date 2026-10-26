package com.codesentinel.backend.controller;

import com.codesentinel.backend.dto.ChatDTOs.*;
import com.codesentinel.backend.service.CodebaseChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/repositories/{repositoryId}/chat")
@Tag(name = "Codebase Q&A", description = "Repository-grounded natural language Q&A with citations")
public class ChatController {

    private final CodebaseChatService chatService;

    public ChatController(CodebaseChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    @Operation(summary = "Ask question grounded in repository codebase")
    public ResponseEntity<ChatResponse> askQuestion(
            @PathVariable String repositoryId,
            @Valid @RequestBody ChatRequest request) {
        return ResponseEntity.ok(chatService.answerQuestion(repositoryId, request));
    }
}
