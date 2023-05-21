package com.example.core.repository;


import com.example.core.model.entity.Folder;
import com.example.core.model.enums.DocumentStatus;
import com.example.core.model.enums.FolderStatus;
import com.example.core.model.enums.SharingStatus;
import com.example.core.model.response.DocumentShortResponse;
import com.example.core.model.response.FolderShortResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {


    Optional<Folder> findFolderByIdAndStatusIn(Long id, List<FolderStatus> statuses);

    @Query(nativeQuery = true, value = """
            WITH RECURSIVE folder_tree AS (
                SELECT *
                FROM folder
                WHERE id = :id
                        
                UNION ALL
                        
                SELECT f.*
                FROM folder f
                         JOIN folder_tree ft ON f.parent_folder_id = ft.id
                where f.status = :status
            )
            SELECT *
            FROM folder_tree
            """)
    List<Folder> findFoldersInsideFolder(@Param("id") Long id,
                                         @Param("status") String status);

    @Query("""
            select new com.example.core.model.response.FolderShortResponse(f) from Folder f
            where f.parentFolder.id=:folderId
            and f.status in (:statuses)
            """)
    Page<FolderShortResponse> findFoldersInFolder(@Param("folderId") Long folderId,
                                                  @Param("statuses") List<FolderStatus> statuses,
                                                  Pageable pageable);

    @Query("""
            select new com.example.core.model.response.FolderShortResponse(f) from Folder f
            where f.parentFolder.id is null
            and f.user.id = :userId
            and f.status in (:statuses)
            """)
    List<FolderShortResponse> findMasterFolders(@Param("userId") Long userId,
                                                @Param("statuses") List<FolderStatus> statuses);

    @Query("""
            select new com.example.core.model.response.FolderShortResponse(f) from Folder f
            join FolderSharingContract fsc on f.id = fsc.id
            where fsc.recipientUser.id = :userId
            and fsc.status in (:shareStatus)
            and f.status in (:statuses)
            """)
    List<FolderShortResponse> findsFoldersSharedToUser(@Param("userId") Long userId,
                                                       @Param("shareStatus") List<SharingStatus> sharingStatuses,
                                                       @Param("statuses") List<FolderStatus> statuses);

    @Query("""
            select d
            from Folder d
            where d.name like :name
            and d.status in (:statuses)
            """)
    Page<Folder> filterByName(@Param("name") String name,
                              @Param("statuses") List<FolderStatus> statuses,
                              Pageable pageable);


}
