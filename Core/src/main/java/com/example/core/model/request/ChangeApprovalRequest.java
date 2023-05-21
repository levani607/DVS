package com.example.core.model.request;

import lombok.Data;

@Data
public class ChangeApprovalRequest {

    private Long documentId;
    private Long versionId;
    private boolean approved;
}
