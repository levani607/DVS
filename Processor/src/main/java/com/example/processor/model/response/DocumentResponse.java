package com.example.processor.model.response;

import com.example.processor.model.enums.ProcessingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentResponse {

    private String documentId;
    private Long externalDocumentId;
    private Long versionId;
    private ProcessingStatus status;


}
