package com.codesentinel.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class PatchDTOs {

    public record PatchCreateRequest(
            @NotBlank String findingId,
            String customizedInstruction
    ) {}

    public record PatchApprovalRequest(
            boolean approved,
            boolean createPullRequest,
            String targetBranch
    ) {}
}
