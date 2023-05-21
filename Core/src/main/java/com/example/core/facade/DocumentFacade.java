package com.example.core.facade;

import com.example.core.exceptions.CoreException;
import com.example.core.exceptions.ErrorCode;
import com.example.core.model.entity.*;
import com.example.core.model.enums.DocumentStatus;
import com.example.core.model.enums.DocumentVersionStatus;
import com.example.core.model.enums.ProcessingStatus;
import com.example.core.model.enums.SharingContractType;
import com.example.core.model.request.*;
import com.example.core.model.response.DocumentShortResponse;
import com.example.core.model.response.FilterResponse;
import com.example.core.model.response.FolderShortResponse;
import com.example.core.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DocumentFacade {

    private final DocumentService documentService;
    private final UserService userService;
    private final FolderService folderService;

    private final FolderSharingService folderSharingService;

    private final DocumentSharingService documentSharingService;

    private final DocumentTopicSender documentTopicSender;
    private final DocumentVersionService documentVersionService;

    @Transactional
    public DocumentShortResponse create(DocumentRequest request){
        User loggedInUser = userService.findLoggedInUser();
        Document document = new Document(request);
        long versionId = 1L;
        document.setLatestVersion(versionId);
        Folder parentFolder = folderService.findActiveFolderById(request.getFolderId());
        if(!Objects.equals(parentFolder.getUser().getId(), loggedInUser.getId())){
            folderSharingService.hasAccessToFolder(loggedInUser.getId(),parentFolder.getId());
        }

        document.setFolder(parentFolder);
        document.setUser(parentFolder.getUser());
        Document save = documentService.save(document);

        documentVersionService.save(new DocumentVersion(save, versionId));

        documentTopicSender.create(new CreateDocumentKafkaRequest(request.getText(), versionId, save.getId()));
        return new DocumentShortResponse(save);
    }

    @Transactional
    public DocumentShortResponse update(DocumentUpdateRequest request) {
        Document document = documentService.findByIdAndStatus(request.getId(), List.of(DocumentStatus.ACTIVE));
        User loggedInUser = userService.findLoggedInUser();
        DocumentVersion oldVersion = documentVersionService.findNewestVersion(document.getId(), document.getLatestVersion());
        Long currentVersion = oldVersion.getVersionId()+1;
        DocumentVersion documentVersion = new DocumentVersion(document, currentVersion);
        if(!Objects.equals(document.getUser().getId(), loggedInUser.getId())){
            DocumentSharingContract accessToDocument = documentSharingService
                    .findAccessToDocument(loggedInUser.getId(), request.getId())
                    .orElseThrow(()->{throw new CoreException(ErrorCode.FORBIDDEN,"You do not have access to this document!");});
            SharingContractType type = accessToDocument.getType();
            switch (type){
                case EDIT -> documentVersion.setStatus(DocumentVersionStatus.IN_PROCESS);
                case EDIT_RESTRICTED -> documentVersion.setStatus(DocumentVersionStatus.WAITING_APPROVAL);
                case READ -> {throw new CoreException(ErrorCode.FORBIDDEN,"You can not edit this document");
                }
            }
        }
        documentVersionService.save(documentVersion);
        documentTopicSender.update(new UpdateDocumentKafkaRequest(oldVersion.getElasticId(),request.getText(),currentVersion, document.getId()));
        return new DocumentShortResponse(document);
    }

    //used from kafka
    @Transactional
    public void confirmVersion(KafkaDocumentVersionRequest request){
        DocumentVersion newestVersion = documentVersionService.findNewestVersion(request.getExternalDocumentId(), request.getVersionId());
        Document document = newestVersion.getDocument();
        if (request.getStatus().equals(ProcessingStatus.FAILED)){
            newestVersion.setStatus(DocumentVersionStatus.FAILED);
            documentVersionService.save(newestVersion);
            return;
        }
        DocumentVersionStatus status = newestVersion.getStatus();
        switch (status){
            case IN_PROCESS -> {
                newestVersion.setElasticId(request.getDocumentId());
                newestVersion.setStatus(DocumentVersionStatus.ACTIVE);
                document.incrementVersionId();
                documentService.save(document);
            }
            case WAITING_APPROVAL -> {
                newestVersion.setElasticId(request.getDocumentId());
                newestVersion.setStatus(DocumentVersionStatus.DONE_WAITING_APPROVAL);
            }
        }

        documentVersionService.save(newestVersion);

    }
//create controller
    @Transactional
    public void approveChange(ChangeApprovalRequest request){
        DocumentVersion newestVersion = documentVersionService.findNewestVersion(request.getDocumentId(), request.getVersionId());
        if(!request.isApproved()){
            newestVersion.setStatus(DocumentVersionStatus.DELETED);
        }
        else {
            DocumentVersionStatus status = newestVersion.getStatus();
            switch (status){
                case WAITING_APPROVAL -> newestVersion.setStatus(DocumentVersionStatus.IN_PROCESS);
                case DONE_WAITING_APPROVAL -> {
                    newestVersion.setStatus(DocumentVersionStatus.ACTIVE);
                    Document document = newestVersion.getDocument();
                    document.incrementVersionId();
                    documentService.save(document);
                }
            }
        }
        documentVersionService.save(newestVersion);
    }

    public FilterResponse filterByName(FilterRequest request) {
        PageRequest pageable = PageRequest.of(request.getPage(), request.getSize(), request.getDirection(), request.getSortBy());
        Page<FolderShortResponse> folders = folderService.filterByName(request.getName(), pageable).map(FolderShortResponse::new );
        Page<DocumentShortResponse> documents = documentService.filterByName(request.getName(), pageable).map(DocumentShortResponse::new);
        return new FilterResponse(documents,folders);
    }

}
