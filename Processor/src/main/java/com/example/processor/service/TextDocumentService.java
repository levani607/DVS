package com.example.processor.service;

import com.example.processor.model.documents.TextDocument;
import com.example.processor.repositories.TextDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TextDocumentService {


    private final TextDocumentRepository documentRepository;


    public TextDocument save(TextDocument document){
        return documentRepository.save(document);
    }

    public Optional<TextDocument> findDocumentById(String id) {
        return documentRepository.findById(id);
    }

}
