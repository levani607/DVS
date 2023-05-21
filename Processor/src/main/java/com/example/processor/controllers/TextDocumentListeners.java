package com.example.processor.controllers;

import com.example.processor.facade.TextDocumentFacade;
import com.example.processor.model.request.DocumentCreateRequest;
import com.example.processor.model.request.DocumentUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TextDocumentListeners {
    private final TextDocumentFacade documentFacade;

    @KafkaListener(topics = {"${myapp.topics.request.create}"},containerFactory = "createRequestContainer")
    public void createDocument(DocumentCreateRequest request) {
        documentFacade.create(request);
    }

    @KafkaListener(topics = {"${myapp.topics.request.update}"},containerFactory = "updateRequestContainer")
    public void updateDocument(DocumentUpdateRequest request) {
        documentFacade.update(request);
    }
}
