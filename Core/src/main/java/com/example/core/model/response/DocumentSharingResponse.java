package com.example.core.model.response;

import com.example.core.model.entity.Document;
import com.example.core.model.entity.DocumentSharingContract;
import com.example.core.model.enums.SharingContractType;
import com.example.core.model.enums.SharingStatus;
import lombok.Data;

@Data
public class DocumentSharingResponse {
    private Long ownerId;
    private Long recipientId;
    private Long documentId;
    private SharingContractType type;
    private SharingStatus status;

    public DocumentSharingResponse(DocumentSharingContract document) {
        this.ownerId = document.getOwnerUser().getId();
        this.recipientId = document.getRecipientUser().getId();
        this.documentId = document.getDocument().getId();
        this.type = document.getType();
        this.status = document.getStatus();
    }
}
