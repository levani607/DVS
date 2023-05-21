package com.example.core.model.request;

import lombok.Data;

@Data
public class DocumentUnshareRequest {
    private Long userId;
    private Long documentId;
}
