package com.example.processor.model.request;

import lombok.Data;

@Data
public class DocumentUpdateRequest {

    private String documentId;
    private String text;
    private Long versionId;
    private Long externalDocumentId;
}
