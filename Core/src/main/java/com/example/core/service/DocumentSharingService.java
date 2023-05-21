package com.example.core.service;

import com.example.core.exceptions.CoreException;
import com.example.core.exceptions.ErrorCode;
import com.example.core.model.entity.DocumentSharingContract;
import com.example.core.model.enums.DocumentStatus;
import com.example.core.model.enums.FolderStatus;
import com.example.core.model.enums.SharingStatus;
import com.example.core.repository.DocumentSharingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DocumentSharingService {


    private final DocumentSharingRepository sharingRepository;

    public DocumentSharingContract save(DocumentSharingContract contract){
        return sharingRepository.save(contract);
    }
    public Optional<DocumentSharingContract> findByRecipientIdStatusAndDocumentId(Long recipientId, List<SharingStatus> statuses, Long documentId){
        return sharingRepository.findByRecipientIdStatusAndDocumentId(recipientId,statuses,documentId);
    }

    public void saveAll(List<DocumentSharingContract> contracts){
        sharingRepository.saveAll(contracts);
    }

    public List<DocumentSharingContract> findContractByFolderId(Long folderId,Long recipientId){
        return sharingRepository.findContractByFolder(folderId,recipientId, FolderStatus.ACTIVE.toString(), DocumentStatus.ACTIVE.toString(),SharingStatus.ACTIVE.toString());
    }
    public Optional<DocumentSharingContract> findAccessToDocument(Long userId, Long documentId) {
     return sharingRepository.findSharingForUser(userId, documentId,SharingStatus.ACTIVE);
    }
}
