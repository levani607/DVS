package com.example.core.repository;

import com.example.core.model.entity.DocumentVersion;
import com.example.core.model.enums.DocumentVersionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentVersionRepository extends JpaRepository<DocumentVersion,Long> {

    @Query("select v from DocumentVersion v where v.document.id=:documentId and v.versionId = :versionId")
    Optional<DocumentVersion> findNewestVersion(@Param("documentId")Long documentId,
                                                @Param("versionId") Long versionId);

    Optional<DocumentVersion> findByIdAndStatusIn(Long id,
                                                  List<DocumentVersionStatus> statuses);
}
