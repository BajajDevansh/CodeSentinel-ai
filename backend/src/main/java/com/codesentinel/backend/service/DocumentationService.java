package com.codesentinel.backend.service;

import com.codesentinel.backend.model.DocumentationGap;
import com.codesentinel.backend.repository.DocumentationGapRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DocumentationService {

    private static final Logger log = LoggerFactory.getLogger(DocumentationService.class);
    private final DocumentationGapRepository docGapRepo;
    private final PythonAiServiceClient pythonClient;

    public DocumentationService(DocumentationGapRepository docGapRepo, PythonAiServiceClient pythonClient) {
        this.docGapRepo = docGapRepo;
        this.pythonClient = pythonClient;
    }

    @SuppressWarnings("unchecked")
    public List<DocumentationGap> getDocumentationGaps(String repoId) {
        List<DocumentationGap> gaps = docGapRepo.findByRepositoryId(repoId);

        // If not already in DB, fetch from Python AI Service
        if (gaps.isEmpty()) {
            try {
                Map<String, Object> aiResult = pythonClient.detectDocumentationGaps(repoId);
                if (aiResult.containsKey("gaps") && aiResult.get("gaps") instanceof List) {
                    List<Map<String, Object>> rawList = (List<Map<String, Object>>) aiResult.get("gaps");
                    List<DocumentationGap> toSave = new ArrayList<>();

                    for (Map<String, Object> item : rawList) {
                        DocumentationGap dg = new DocumentationGap();
                        dg.setRepositoryId(repoId);
                        dg.setSymbolName((String) item.getOrDefault("symbol_name", "UnknownSymbol"));
                        dg.setSymbolType((String) item.getOrDefault("symbol_type", "METHOD"));
                        dg.setFilePath((String) item.getOrDefault("file_path", "unknown"));
                        dg.setLineNumber(((Number) item.getOrDefault("line_number", 1)).intValue());
                        dg.setPriority((String) item.getOrDefault("priority", "MEDIUM"));
                        dg.setReason((String) item.getOrDefault("reason", "MISSING_DOC"));
                        dg.setProposedDocumentation((String) item.getOrDefault("proposed_doc", "/** No documentation generated */"));
                        dg.setStatus("PENDING");
                        toSave.add(dg);
                    }
                    return docGapRepo.saveAll(toSave);
                }
            } catch (Exception e) {
                log.error("Failed to fetch documentation gaps from AI service: {}", e.getMessage());
            }
        }
        return gaps;
    }

    public String generateOverviewMarkdown(String repoId) {
        return "# CodeSentinel Architecture & Repository Overview\n\n" +
                "## Repository ID: " + repoId + "\n" +
                "- **Authentication**: Stateless JWT security filter.\n" +
                "- **Knowledge Graph**: AST-derived call graph traversal.\n" +
                "- **Test Gaps**: Coverage-guided mutation analysis.\n";
    }
}