package com.example.core.facade;

import com.example.core.exceptions.CoreException;
import com.example.core.exceptions.ErrorCode;
import com.example.core.model.entity.*;
import com.example.core.model.enums.SharingContractType;
import com.example.core.model.enums.SharingStatus;
import com.example.core.model.request.FolderSharingRequest;
import com.example.core.model.request.FolderUnshareRequest;
import com.example.core.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FolderSharingFacade {
    private final FolderSharingService folderSharingService;
    private final DocumentSharingService documentSharingService;
    private final UserService userService;
    private final FolderService folderService;
    private final DocumentService documentService;


    @Transactional
    public void share(FolderSharingRequest request) {
        Folder parentFolder = folderService.findActiveFolderById(request.getFolderID());
        User loggedInUser = userService.findLoggedInUser();
        if (!Objects.equals(loggedInUser.getId(), parentFolder.getUser().getId())) {
            throw new CoreException(ErrorCode.FORBIDDEN, "You can not share this object");
        }
        folderSharingService.findContractByFolderIdAndUserId(request.getFolderID(), request.getUserId())
                .ifPresent(x->{throw new CoreException(ErrorCode.CONFLICT,"Folder is already shared!");});
        User recipient = userService.findActiveById(request.getUserId());
        SharingContractType contractType = request.getContractType();
        List<Folder> foldersToShare = folderService.findFoldersInsideFolder(parentFolder.getId());
        List<FolderSharingContract> folderSharingContracts = foldersToShare
                .stream()
                .map(folder -> new FolderSharingContract(folder, loggedInUser, recipient, contractType)).toList();
        List<Document> documentsInsideFolder = documentService.findEveryDocumentInFolder(parentFolder.getId());
        List<DocumentSharingContract> documentSharingContracts = documentsInsideFolder
                .stream()
                .map(document -> new DocumentSharingContract(document, contractType, loggedInUser, recipient))
                .toList();
        folderSharingService.saveAll(folderSharingContracts);
        documentSharingService.saveAll(documentSharingContracts);
    }

    @Transactional
    public void update(FolderSharingRequest request) {
        checkAccess(request.getFolderID(), request.getUserId());
        SharingContractType contractType = request.getContractType();
        List<FolderSharingContract> folderSharingContracts = folderSharingService.findContractsByFolderId(request.getFolderID(), request.getUserId());
        folderSharingContracts.forEach(contract->{
            contract.setType(contractType);
        });
        List<DocumentSharingContract> documentSharingContracts = documentSharingService.findContractByFolderId(request.getFolderID(), request.getUserId());
        documentSharingContracts.forEach(contract -> {
            contract.setType(contract.getType());
        });
        documentSharingService.saveAll(documentSharingContracts);
        folderSharingService.saveAll(folderSharingContracts);
    }

    @Transactional
    public void unshare(FolderUnshareRequest request) {
        checkAccess(request.getFolderID(), request.getUserId());

        List<FolderSharingContract> folderSharingContracts = folderSharingService.findContractsByFolderId(request.getFolderID(), request.getUserId());
        folderSharingContracts.forEach(contract -> {
            contract.setStatus(SharingStatus.INACTIVE);
        });
        List<DocumentSharingContract> documentSharingContracts = documentSharingService.findContractByFolderId(request.getFolderID(), request.getUserId());
        documentSharingContracts.forEach(contract -> {
            contract.setStatus(SharingStatus.INACTIVE);
        });
        documentSharingService.saveAll(documentSharingContracts);
        folderSharingService.saveAll(folderSharingContracts);
    }

    private void checkAccess(Long folderID, Long userId) {
        FolderSharingContract folderSharingContract = folderSharingService.findContractByFolderIdAndUserId(folderID, userId)
                .orElseThrow(() -> {
                    throw new CoreException(ErrorCode.OBJECT_NOT_FOUND, "No contract exists for given folder!");
                });
        User loggedInUser = userService.findLoggedInUser();
        if (!Objects.equals(loggedInUser.getId(), folderSharingContract.getOwnerUser().getId())) {
            throw new CoreException(ErrorCode.FORBIDDEN, "You can not share this object");
        }
    }


}
