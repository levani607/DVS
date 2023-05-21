package com.example.core.model.request;

import com.example.core.model.enums.SharingContractType;
import lombok.Data;

@Data
public class FolderSharingRequest {

    private Long folderID;
    private Long userId;
    private SharingContractType contractType;
}
