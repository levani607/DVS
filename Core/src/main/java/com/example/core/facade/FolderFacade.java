package com.example.core.facade;

import com.example.core.model.entity.Document;
import com.example.core.model.entity.Folder;
import com.example.core.model.entity.User;
import com.example.core.model.request.FolderRequest;
import com.example.core.model.response.DocumentShortResponse;
import com.example.core.model.response.FolderResponse;
import com.example.core.model.response.FolderShortResponse;
import com.example.core.service.DocumentService;
import com.example.core.service.FolderService;
import com.example.core.service.FolderSharingService;
import com.example.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FolderFacade {

    private final FolderService folderService;
    private final UserService userService;
    private final DocumentService documentService;
    private final FolderSharingService folderSharingService;

    @Transactional
    public FolderShortResponse create(FolderRequest request){
        User loggedInUser = userService.findLoggedInUser();
        Folder folder = new Folder(request,loggedInUser);

        Long parentFolderId = request.getParentFolderId();
        if(parentFolderId!=null){
            Folder parentFolder = folderService.findActiveFolderById(parentFolderId);
            if(!Objects.equals(parentFolder.getUser().getId(), loggedInUser.getId())){
                folderSharingService.hasAccessToFolder(loggedInUser.getId(),parentFolderId);
            }
            folder.setParentFolder(parentFolder);
        }
        return new FolderShortResponse(folderService.save(folder));
    }

    public FolderResponse listContents(Long folderId,Integer page, Integer size){

        Folder folder = folderService.findActiveFolderById(folderId);
        User loggedInUser = userService.findLoggedInUser();
        if(!Objects.equals(folder.getUser().getId(), loggedInUser.getId())){
            folderSharingService.hasAccessToFolder(loggedInUser.getId(),folderId);
        }
        Page<DocumentShortResponse> documentsInFolder = documentService.findDocumentsInFolder(folderId,PageRequest.of(page,size));
        Page<FolderShortResponse> foldersInFolder = folderService.findFoldersInFolder(folderId,PageRequest.of(page,size));
        return new FolderResponse(documentsInFolder,foldersInFolder);
    }

    public List<FolderShortResponse> listMasterFolders(){
        User loggedInUser = userService.findLoggedInUser();
        List<FolderShortResponse> masterFolders = folderService.findMasterFoldersForUser(loggedInUser.getId());
        masterFolders.addAll(folderService.findFoldersSharedToUser(loggedInUser.getId()));
        return masterFolders;
    }


}
