package com.example.core.controller.kafka;

import com.example.core.facade.DocumentFacade;
import com.example.core.model.request.KafkaDocumentVersionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProcessorListener {

    private final DocumentFacade facade;

    @KafkaListener(topics = {"${myapp.topics.doc-response.create}"},containerFactory = "documentVersionContainerFactory")
    public void createDocument(KafkaDocumentVersionRequest request) {
        facade.confirmVersion(request);
    }

}
