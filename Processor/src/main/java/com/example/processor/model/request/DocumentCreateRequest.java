package com.example.processor.model.request;

import lombok.Data;

@Data
public class DocumentCreateRequest {

    private String text;
    private Long versionId;
    private Long externalDocumentId;
}
