package com.example.core.model.response;

import lombok.Data;
import org.springframework.data.domain.Page;
@Data
public class FilterResponse {
    Page<DocumentShortResponse> documents;
    Page<FolderShortResponse> folders;

    public FilterResponse(Page<DocumentShortResponse> documents, Page<FolderShortResponse> folders) {
        this.documents = documents;
        this.folders = folders;
    }
}
