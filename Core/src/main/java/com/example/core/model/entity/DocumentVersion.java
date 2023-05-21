package com.example.core.model.entity;

import com.example.core.model.enums.DocumentVersionStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class DocumentVersion extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "document_id")
    private Document document;

    @Column(name = "version_id")
    private Long versionId;
    @Column(name = "elastic_id")
    private String elasticId;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private DocumentVersionStatus status;

    public DocumentVersion(Document document, Long versionId) {
        this.status = DocumentVersionStatus.IN_PROCESS;
        this.versionId =versionId;
        this.document = document;
    }


}
