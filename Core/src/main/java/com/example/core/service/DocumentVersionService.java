package com.example.core.service;

import com.example.core.exceptions.CoreException;
import com.example.core.exceptions.ErrorCode;
import com.example.core.model.entity.DocumentVersion;
import com.example.core.model.enums.DocumentVersionStatus;
import com.example.core.repository.DocumentVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentVersionService {

    private final DocumentVersionRepository repository;

    public DocumentVersion save(DocumentVersion version){
        return repository.save(version);
    }

    public DocumentVersion findNewestVersion(Long documentId,Long versionId){
        return repository.findNewestVersion(documentId,versionId)
                .orElseThrow(()->{throw new CoreException(ErrorCode.OBJECT_NOT_FOUND);
                });
    }
    public DocumentVersion findByIdAndStatus(Long id){
        return repository
                .findByIdAndStatusIn(id, List.of(DocumentVersionStatus.ACTIVE))
                .orElseThrow(()->{throw new CoreException(ErrorCode.OBJECT_NOT_FOUND);
                });
    }
}
