package com.example.core.facade;

import com.example.core.exceptions.CoreException;
import com.example.core.exceptions.ErrorCode;
import com.example.core.model.entity.Document;
import com.example.core.model.entity.DocumentSharingContract;
import com.example.core.model.entity.User;
import com.example.core.model.enums.DocumentStatus;
import com.example.core.model.enums.SharingStatus;
import com.example.core.model.request.DocumentSharingRequest;
import com.example.core.model.request.DocumentUnshareRequest;
import com.example.core.model.response.DocumentSharingResponse;
import com.example.core.repository.DocumentSharingRepository;
import com.example.core.service.DocumentService;
import com.example.core.service.DocumentSharingService;
import com.example.core.service.FolderService;
import com.example.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DocumentSharingFacade {

    private final DocumentSharingService documentSharingService;
    private final DocumentService documentService;
    private final UserService userService;

    public DocumentSharingResponse share(DocumentSharingRequest request) {
        Document document = documentService
                .findByIdAndStatus(request.getDocumentId(), List.of(DocumentStatus.ACTIVE));
        User loggedInUser = userService.findLoggedInUser();
        if(!Objects.equals(loggedInUser.getId(), document.getUser().getId())){
            throw new CoreException(ErrorCode.FORBIDDEN,"You can not share this object");
        }
        User recipient = userService.findActiveById(request.getUserId());
        documentSharingService.findByRecipientIdStatusAndDocumentId(recipient.getId(),List.of(SharingStatus.ACTIVE), document.getId())
                .ifPresent((x)->{throw new CoreException(ErrorCode.CONFLICT, "This document is already shared with user");});
        DocumentSharingContract documentSharingContract = new DocumentSharingContract(document, request.getContractType(), loggedInUser, recipient);
        DocumentSharingContract save = documentSharingService.save(documentSharingContract);
        return new DocumentSharingResponse(save);
    }

    public DocumentSharingResponse update(DocumentSharingRequest request) {

        DocumentSharingContract contract= findContractOrThrow(request.getUserId(), request.getDocumentId());;
        contract.setType(request.getContractType());
        DocumentSharingContract save = documentSharingService.save(contract);
        return new DocumentSharingResponse(save);
    }

    public DocumentSharingResponse unshare(DocumentUnshareRequest request) {

        DocumentSharingContract contract= findContractOrThrow(request.getUserId(), request.getDocumentId());;
        contract.setStatus(SharingStatus.INACTIVE);
        DocumentSharingContract save = documentSharingService.save(contract);
        return new DocumentSharingResponse(save);
    }

    private DocumentSharingContract findContractOrThrow(Long userId, Long documentId) {
        DocumentSharingContract contract = documentSharingService.findByRecipientIdStatusAndDocumentId(userId, List.of(SharingStatus.ACTIVE), documentId)
                .orElseThrow(() -> {
                    throw new CoreException(ErrorCode.OBJECT_NOT_FOUND, "Contract not found");
                });
        User loggedInUser = userService.findLoggedInUser();
        if (!Objects.equals(loggedInUser.getId(), contract.getOwnerUser().getId())) {
            throw new CoreException(ErrorCode.FORBIDDEN, "You can not share this object");
        }
        return contract;
    }


}
