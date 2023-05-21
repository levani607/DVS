package com.example.core.model.entity;

import com.example.core.model.enums.SharingContractType;
import com.example.core.model.enums.SharingStatus;
import com.example.core.model.request.DocumentSharingRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class DocumentSharingContract extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    private Document document;

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

    public DocumentSharingContract(Document document, SharingContractType type,User ownerUser, User recipientUser) {
        this.document = document;
        this.ownerUser = ownerUser;
        this.recipientUser = recipientUser;
        this.type = type;
        this.status = SharingStatus.ACTIVE;
    }
}
