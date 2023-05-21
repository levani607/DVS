package com.example.core.model.response;

import com.example.core.model.entity.Document;
import com.example.core.model.enums.DocumentStatus;
import lombok.Data;

@Data
public class DocumentShortResponse {

    private Long id;
    private String name;
    private Long parentFolderId;
    private DocumentStatus status;
    private Long latestVersion;

    public DocumentShortResponse(Document document) {
        this.id = document.getId();
        this.name = document.getName();
        this.latestVersion= document.getLatestVersion();
        this.parentFolderId = document.getFolder().getId();
        this.status = document.getStatus();
    }
}
