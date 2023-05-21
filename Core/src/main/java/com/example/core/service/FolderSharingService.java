package com.example.core.service;

import com.example.core.exceptions.CoreException;
import com.example.core.exceptions.ErrorCode;
import com.example.core.model.entity.Folder;
import com.example.core.model.entity.FolderSharingContract;
import com.example.core.model.enums.FolderStatus;
import com.example.core.model.enums.SharingStatus;
import com.example.core.repository.FolderSharingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FolderSharingService {
    private final FolderSharingRepository repository;

    public FolderSharingContract save(FolderSharingContract folder) {
        return repository.save(folder);
    }

    public void saveAll(List<FolderSharingContract> folders) {
        repository.saveAll(folders);
    }

    public List<FolderSharingContract> findContractsByFolderId(Long folderId, Long recipientId) {
        return repository.findContractsWithFolder(folderId, recipientId, FolderStatus.ACTIVE.toString(), SharingStatus.ACTIVE.toString());
    }

    public Optional<FolderSharingContract> findContractByFolderIdAndUserId(Long folderId, Long recipientId) {
        return repository.findContractByFolderIdAndUserId(folderId, recipientId, SharingStatus.ACTIVE);
    }

    public void hasAccessToFolder(Long userId, Long folderId) {
        if (!repository.sharingExistsForFolder(userId, folderId)){
            throw new CoreException(ErrorCode.FORBIDDEN,"You do not have access to this folder!");
        }
    }

}
