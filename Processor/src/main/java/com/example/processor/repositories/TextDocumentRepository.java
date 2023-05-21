package com.example.processor.repositories;

import com.example.processor.model.documents.TextDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TextDocumentRepository extends ElasticsearchRepository<TextDocument,String> {
}
