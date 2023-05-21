package com.example.core.model.entity;

import com.example.core.model.enums.SharingContractType;
import com.example.core.model.enums.SharingStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class FolderSharingContract extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id")
    private Folder folder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User ownerUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id")
    private User recipientUser;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private SharingContractType type;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private SharingStatus status;

    public FolderSharingContract(Folder folder,User ownerUser,User recipientUser,SharingContractType type) {
        this.folder = folder;
        this.ownerUser = ownerUser;
        this.recipientUser = recipientUser;
        this.type = type;
        this.status = SharingStatus.ACTIVE;
    }
}
