package com.example.core.model.entity;

import com.example.core.model.enums.DocumentStatus;
import com.example.core.model.request.DocumentRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Document extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "latest_version")
    private Long latestVersion;

    @ManyToOne
    @JoinColumn(name = "folder")
    private Folder folder;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private DocumentStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    public Document(DocumentRequest request) {
        this.name = request.getName();
        this.status = DocumentStatus.ACTIVE;
    }

    public void incrementVersionId(){
        this.latestVersion+=1;
    }
}
