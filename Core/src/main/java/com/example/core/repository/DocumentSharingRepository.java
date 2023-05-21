package com.example.core.repository;

import com.example.core.model.entity.DocumentSharingContract;
import com.example.core.model.enums.DocumentStatus;
import com.example.core.model.enums.FolderStatus;
import com.example.core.model.enums.SharingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentSharingRepository extends JpaRepository<DocumentSharingContract, Long> {

    @Query("""
            select c from DocumentSharingContract c 
            where c.recipientUser.id=:recipientId
            and c.status in (:statuses)
            and c.document.id = :documentId
            """)
    Optional<DocumentSharingContract> findByRecipientIdStatusAndDocumentId(@Param("recipientId") Long recipientId,
                                                                           @Param("statuses") List<SharingStatus> statuses,
                                                                           @Param("documentId") Long documentId);


    @Query(nativeQuery = true, value = """
            WITH RECURSIVE folder_tree AS (
                SELECT id
                FROM folder
                WHERE id = :folderId
                        
                UNION ALL
                        
                SELECT f.id
                FROM folder f
                         JOIN folder_tree ft ON f.parent_folder_id = ft.id
                    and f.status = :folderStatus
            )
            select ds.*
            from document_sharing_contract ds
                     join document d on ds.document_id = d.id
                    join folder f on d.folder=f.id
            where f.id in (SELECT id FROM folder_tree)
            and ds.recipient_id = :recipientId
            and d.status = :documentStatus
            and ds.status = :sharingStatus
            """)
    List<DocumentSharingContract> findContractByFolder(@Param("folderId") Long folderId,
                                                       @Param("recipientId") Long recipientId,
                                                       @Param("folderStatus") String folderStatus,
                                                       @Param("documentStatus") String documentStatus,
                                                       @Param("sharingStatus") String sharingStatus);


    @Query("""
            SELECT f
            FROM DocumentSharingContract f
            WHERE f.recipientUser.id = :userId
            and f.document.id = :documentId
            and f.status = :status
            """)
    Optional<DocumentSharingContract> findSharingForUser(@Param("userId") Long userId,
                                     @Param("documentId") Long documentId,
                                     @Param("status")SharingStatus status);
}
