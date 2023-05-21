package com.example.core.model.request;

import lombok.Data;

@Data
public class FolderRequest {
    private Long parentFolderId;
    private String name;
}
