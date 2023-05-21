package com.example.core.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDocumentKafkaRequest {
    private String documentId;
    private String text;
    private Long versionId;
    private Long externalDocumentId;
}
