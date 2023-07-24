package com.example.core.service;

import com.example.core.exceptions.CoreException;
import com.example.core.exceptions.ErrorCode;
import com.example.core.model.entity.Document;
import com.example.core.model.enums.DocumentStatus;
import com.example.core.model.enums.FolderStatus;
import com.example.core.model.response.DocumentShortResponse;
import com.example.core.model.response.ItemShortResponse;
import com.example.core.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository repository;

    public Document save(Document document) {
        return repository.save(document);
    }

    public Document findByIdAndStatus(Long id, List<DocumentStatus> statuses) {
        return repository.findByIdAndStatusIn(id, statuses)
                .orElseThrow(() -> {
                    throw new CoreException(ErrorCode.OBJECT_NOT_FOUND, String.format("Document not found with id: %s", id));
                });
    }

    public List<Document> findEveryDocumentInFolder(Long id){
        return repository.findEveryDocumentInFolder(id, FolderStatus.ACTIVE.toString(),DocumentStatus.ACTIVE.toString());
    }

    public Page<ItemShortResponse> findDocumentsInFolder(Long id, Pageable pageable){
        return repository.findDocumentsInFolder(id,List.of(DocumentStatus.ACTIVE,DocumentStatus.ACTIVE),pageable);
    }

    public Page<Document> filterByName(String name, PageRequest pageRequest) {
        return repository.findDocumentByName(name, List.of(DocumentStatus.ACTIVE),pageRequest);
    }
}
