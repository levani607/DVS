package com.example.core.repository;

import com.example.core.model.entity.FolderSharingContract;
import com.example.core.model.enums.FolderStatus;
import com.example.core.model.enums.SharingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FolderSharingRepository extends JpaRepository<FolderSharingContract, Long> {


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
            SELECT fd.*
            FROM folder_sharing_contract fd
                     JOIN folder f ON fd.folder_id = f.id
            WHERE f.id IN (SELECT id FROM folder_tree)
            and fd.status =:sharingStatus
            and fd.recipient_id = :recipientId
            """)
    List<FolderSharingContract> findContractsWithFolder(@Param("folderId") Long folderId,
                                                        @Param("recipientId") Long recipientId,
                                                        @Param("folderStatus") String folderStatus,
                                                        @Param("sharingStatus") String sharingStatus);


    @Query("""
            select fs from FolderSharingContract fs
            where fs.folder.id = :folderId
            and fs.recipientUser.id = :recipientId
            and fs.status = :status
            """)
    Optional<FolderSharingContract> findContractByFolderIdAndUserId(@Param("folderId") Long folderId,
                                                                    @Param("recipientId") Long recipientId,
                                                                    @Param("status") SharingStatus status);


    @Query("""
            SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END 
            FROM FolderSharingContract f
            WHERE f.recipientUser.id = :userId
            and f.folder.id = :folderId
            """)
    boolean sharingExistsForFolder(@Param("userId") Long userId,
                                   @Param("folderId") Long folderId);
}
