package com.example.core.configs;

import com.example.core.model.request.CreateDocumentKafkaRequest;
import com.example.core.model.request.UpdateDocumentKafkaRequest;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfigs {

    @Value("${myapp.bootstrap-servers}")
    private String bootstrapAddress;

    private Map<String, Object> getConfigProps() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return configProps;
    }

    @Bean
    public ProducerFactory<String, CreateDocumentKafkaRequest> createDocumentProducerFactory() {
        return new DefaultKafkaProducerFactory<>(getConfigProps());
    }
    @Bean
    public KafkaTemplate<String, CreateDocumentKafkaRequest> createDocumentRequestTemplate(ProducerFactory<String, CreateDocumentKafkaRequest> createDocumentProducerFactory) {
        return new KafkaTemplate<>(createDocumentProducerFactory);
    }

    @Bean
    public ProducerFactory<String, UpdateDocumentKafkaRequest> updateDocumentProducerFactory() {
        return new DefaultKafkaProducerFactory<>(getConfigProps());
    }

    @Bean
    public KafkaTemplate<String, UpdateDocumentKafkaRequest> updateDocumentRequestTemplate(ProducerFactory<String, UpdateDocumentKafkaRequest> updateDocumentProducerFactory) {
        return new KafkaTemplate<>(updateDocumentProducerFactory);
    }
}
