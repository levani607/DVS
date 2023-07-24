package com.example.core.repository;

import com.example.core.model.entity.Document;
import com.example.core.model.enums.DocumentStatus;
import com.example.core.model.response.DocumentShortResponse;
import com.example.core.model.response.ItemShortResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {


    Optional<Document> findByIdAndStatusIn(Long id, List<DocumentStatus> statuses);

    @Query(nativeQuery = true, value = """
            WITH RECURSIVE folder_tree AS (
                SELECT id
                FROM folder
                WHERE id = :id
                        
                UNION ALL
                        
                SELECT f.id
                FROM folder f
                         JOIN folder_tree ft ON f.parent_folder_id = ft.id
                and f.status = :folderStatus
            )
            SELECT d.*
            FROM document d
                     JOIN folder f ON d.folder = f.id
            WHERE f.id IN (SELECT id FROM folder_tree)
            and d.status = :documentStatus
            """)
    List<Document> findEveryDocumentInFolder(@Param("id") Long id,
                                             @Param("folderStatus") String folderStatus,
                                             @Param("documentStatus") String documentStatus);

    @Query("""
            select new com.example.core.model.response.ItemShortResponse(d) from Document d
            where d.folder.id=:folderId
            and d.status in (:statuses)
            """)
    Page<ItemShortResponse> findDocumentsInFolder(@Param("folderId") Long folderId,
                                                  @Param("statuses") List<DocumentStatus> statuses,
                                                  Pageable pageable);

    @Query("""
            select d
            from Document d 
            where d.name like :name
            and d.status in (:statuses)
            """)
    Page<Document> findDocumentByName(@Param("name") String name,
                                      @Param("statuses") List<DocumentStatus> statuses,
                                      Pageable pageable);

}
