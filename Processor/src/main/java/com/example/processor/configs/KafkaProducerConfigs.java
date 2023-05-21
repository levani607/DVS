package com.example.processor.configs;

import com.example.processor.model.response.DocumentResponse;
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
    public ProducerFactory<String, DocumentResponse> createDocumentResponseProducerFactory() {
        return new DefaultKafkaProducerFactory<>(getConfigProps());
    }
    @Bean
    public KafkaTemplate<String, DocumentResponse> createDocumentResponseTemplate(ProducerFactory<String, DocumentResponse> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
