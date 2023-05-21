package com.example.core.model.request;

import com.example.core.model.enums.ProcessingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KafkaDocumentVersionRequest {
    private String documentId;
    private Long externalDocumentId;
    private Long versionId;
    private ProcessingStatus status;
}
