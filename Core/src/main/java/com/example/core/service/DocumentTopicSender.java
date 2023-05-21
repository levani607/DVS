package com.example.core.service;

import com.example.core.model.request.CreateDocumentKafkaRequest;
import com.example.core.model.request.UpdateDocumentKafkaRequest;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DocumentTopicSender {

    private final KafkaTemplate<String, UpdateDocumentKafkaRequest> updateDocumentTemplate;
    private final KafkaTemplate<String, CreateDocumentKafkaRequest> createDocumentTemplate;

    @Value("${myapp.topics.doc-request.create}")
    private String createTopic;
    @Value("${myapp.topics.doc-request.update}")
    private String updateTopic;

    public void create(CreateDocumentKafkaRequest request){
        createDocumentTemplate.send(createTopic,request);
    }

    public void update(UpdateDocumentKafkaRequest request){
        updateDocumentTemplate.send(updateTopic,request);
    }

}
