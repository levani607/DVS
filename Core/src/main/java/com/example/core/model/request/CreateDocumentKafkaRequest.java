package com.example.core.model.request;

import lombok.Data;

@Data
public class CreateDocumentKafkaRequest {
    private String text;
    private Long versionId;
    private Long externalDocumentId;

    public CreateDocumentKafkaRequest(String text, Long versionId, Long externalDocumentId) {
        this.text = text;
        this.versionId = versionId;
        this.externalDocumentId = externalDocumentId;
    }
}
