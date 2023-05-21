package com.example.core.service;

import com.example.core.exceptions.CoreException;
import com.example.core.exceptions.ErrorCode;
import com.example.core.model.entity.Folder;
import com.example.core.model.enums.FolderStatus;
import com.example.core.model.enums.SharingStatus;
import com.example.core.model.response.FolderShortResponse;
import com.example.core.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;

    public Folder save(Folder folder) {
        return folderRepository.save(folder);
    }

    public Folder findActiveFolderById(Long id) {
        return folderRepository
                .findFolderByIdAndStatusIn(id, List.of(FolderStatus.ACTIVE))
                .orElseThrow(() -> {
                    throw new CoreException(ErrorCode.OBJECT_NOT_FOUND, String.format("Folder with id: %s not found", id));
                });
    }

    public List<Folder> findFoldersInsideFolder(Long id) {
        return folderRepository.findFoldersInsideFolder(id, FolderStatus.ACTIVE.toString());
    }
    public Page<FolderShortResponse> findFoldersInFolder(Long id,Pageable pageable){
        return folderRepository.findFoldersInFolder(id,List.of(FolderStatus.ACTIVE),pageable);
    }

    public List<FolderShortResponse> findMasterFoldersForUser(Long userId){
        return folderRepository.findMasterFolders(userId,List.of(FolderStatus.ACTIVE));
    }

    public List<FolderShortResponse> findFoldersSharedToUser(Long userId) {
        return folderRepository.findsFoldersSharedToUser(userId, List.of(SharingStatus.ACTIVE),List.of(FolderStatus.ACTIVE));
    }

    public Page<Folder> filterByName(String name, Pageable pageable) {
        return folderRepository.filterByName(name,List.of(FolderStatus.ACTIVE), pageable);
    }
}
