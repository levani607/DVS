package com.example.core.model.entity;

import com.example.core.model.enums.FolderStatus;
import com.example.core.model.request.FolderRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Folder extends BaseEntity{

    private String name;
    //document
    @OneToMany(mappedBy = "parentFolder",fetch = FetchType.LAZY)
    private List<Folder> insideFolders;

    @ManyToOne
    @JoinColumn(name = "parent_folder_id")
    private Folder parentFolder;

    @OneToMany(mappedBy = "folder",fetch = FetchType.LAZY)
    private List<Document> documents;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private FolderStatus status;

    public Folder(FolderRequest request, User user) {
        this.name = request.getName();
        this.user = user;
        this.status = FolderStatus.ACTIVE;
    }
}
