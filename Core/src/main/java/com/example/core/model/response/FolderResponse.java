package com.example.core.model.response;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
@Data
public class FolderResponse {

    Page<DocumentShortResponse> documents;
    Page<FolderShortResponse> folders;

    public FolderResponse(Page<DocumentShortResponse> documents, Page<FolderShortResponse> folders) {
        this.documents = documents;
        this.folders = folders;
    }
}
