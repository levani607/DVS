package com.example.core.model.response;

import com.example.core.model.entity.Document;
import com.example.core.model.entity.Folder;
import com.example.core.model.enums.ItemType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ItemShortResponse {

    private Long id;
    private String name;
    private Long parentFolderId;
    private Long latestVersion;
    private ItemType type;

    public ItemShortResponse(Document document) {
        this.id = document.getId();
        this.name = document.getName();
        this.parentFolderId = document.getFolder().getId();
        this.latestVersion = document.getLatestVersion();
        this.type = ItemType.DOCUMENT;
    }

    public ItemShortResponse(Folder folder) {
        this.id = folder.getId();
        this.name = folder.getName();
        Folder parentFolder = folder.getParentFolder();
        if(parentFolder!=null){
            this.parentFolderId = parentFolder.getId();
        }


        this.type = ItemType.FOLDER;
    }
}
