package com.example.processor.facade;

import com.example.processor.model.documents.Change;
import com.example.processor.model.documents.DocumentChange;
import com.example.processor.model.documents.TextDocument;
import com.example.processor.model.enums.ProcessingStatus;
import com.example.processor.model.request.DocumentCreateRequest;
import com.example.processor.model.request.DocumentUpdateRequest;
import com.example.processor.model.response.DocumentResponse;
import com.example.processor.service.TextDocumentService;
import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Chunk;
import com.github.difflib.patch.Patch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TextDocumentFacade {

    @Value("${text.delimiter}")
    private String delimiterRegex;
    @Value("${myapp.topics.response.create}")
    private String createTopic;
    @Value("${myapp.topics.response.update}")
    private String updateTopic;

    private final TextDocumentService documentService;
    private final KafkaTemplate<String, DocumentResponse> createDocumentResponseTemplate;

    public void create(DocumentCreateRequest request){
        TextDocument textDocument = new TextDocument(request.getText());
        TextDocument savedDocument = documentService.save(textDocument);

        DocumentResponse response = new DocumentResponse(savedDocument.getId(), request.getExternalDocumentId(), request.getVersionId(), ProcessingStatus.DONE);
        createDocumentResponseTemplate.send(createTopic,response);

    }

    public void update(DocumentUpdateRequest request){
        Optional<TextDocument> documentOptional = documentService.findDocumentById(request.getDocumentId());
        if (documentOptional.isEmpty()){
            DocumentResponse response = new DocumentResponse(null, request.getExternalDocumentId(), request.getVersionId(), ProcessingStatus.FAILED);
            createDocumentResponseTemplate.send(updateTopic,response);
            log.debug("Document do not exists on this id");
            return;
        }
        TextDocument document = documentOptional.get();
        String text = request.getText();
        List<Change> changes = processData(document.getText(), text);
        document.setText(text);
        appendChanges(document,changes, request.getVersionId());
        documentService.save(document);
        DocumentResponse response = new DocumentResponse(document.getId(), request.getExternalDocumentId(), request.getVersionId(), ProcessingStatus.DONE);
        createDocumentResponseTemplate.send(updateTopic,response);
    }




    private List<Change>  processData(String source, String target){
        List<String> oldLines = Arrays.asList(source.split(delimiterRegex));
        List<String> newLines = Arrays.asList(target.split(delimiterRegex));
        Patch<String> patch = DiffUtils.diff(oldLines, newLines);

        List<Change> changes = new ArrayList<>();

        for (AbstractDelta<String> delta : patch.getDeltas()) {
            Chunk<String> deltaSource = delta.getSource();
            Change change = new Change(delta.getType(), deltaSource.getPosition(), deltaSource.size(), deltaSource.getLines());
            changes.add(change);
        }
        return changes;
    }

    private void appendChanges(TextDocument document,List<Change> changes,Long versionId){
        if(document.getDocumentChanges()==null){
            document.setDocumentChanges(new ArrayList<>());
        }
        document.getDocumentChanges().add(new DocumentChange(changes,versionId));
    }



}
