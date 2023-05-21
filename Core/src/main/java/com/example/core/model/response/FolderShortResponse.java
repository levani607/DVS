package com.example.core.model.response;

import com.example.core.model.entity.Folder;
import com.example.core.model.enums.FolderStatus;
import lombok.Data;

@Data
public class FolderShortResponse {

    private Long id;
    private String name;
    private FolderStatus status;
    private Long parentFolderId;

    public FolderShortResponse(Folder folder) {
        this.id = folder.getId();
        this.name = folder.getName();
        this.status = folder.getStatus();
        Folder parentFolder = folder.getParentFolder();
        if(parentFolder!=null){
            this.parentFolderId = parentFolder.getId();
        }

    }
}
