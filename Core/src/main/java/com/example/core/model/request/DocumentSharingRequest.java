package com.example.core.model.request;

import com.example.core.model.enums.SharingContractType;
import lombok.Data;

@Data
public class DocumentSharingRequest {

    private Long documentId;
    private Long userId;
    private SharingContractType contractType;
}
