package com.example.core.model.request;

import lombok.Data;

@Data
public class FolderUnshareRequest {

    private Long folderID;
    private Long userId;
}
