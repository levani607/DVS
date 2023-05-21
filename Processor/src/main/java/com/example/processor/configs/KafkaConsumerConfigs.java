package com.example.processor.configs;

import com.example.processor.model.request.DocumentCreateRequest;
import com.example.processor.model.request.DocumentUpdateRequest;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfigs {

    @Value("${myapp.bootstrap-servers}")
    private String bootstrapAddress;
    @Value("${myapp.groupId}")
    String groupId;

    private Map<String, Object> getConfigProps() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        return configProps;
    }

    //create request
    @Bean
    public ConsumerFactory<String, DocumentCreateRequest> createRequestConsumerFactory() {
        Map<String, Object> configProps = getConfigProps();
        JsonDeserializer<DocumentCreateRequest> deserializer = new JsonDeserializer<>(DocumentCreateRequest.class);
        deserializer.setUseTypeHeaders(false);
        return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, DocumentCreateRequest> createRequestContainer(ConsumerFactory<String, DocumentCreateRequest> createRequestConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, DocumentCreateRequest> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(createRequestConsumerFactory);
        return factory;
    }

    //update request
    @Bean
    public ConsumerFactory<String, DocumentUpdateRequest> updateRequestConsumerFactory() {
        Map<String, Object> configProps = getConfigProps();
        JsonDeserializer<DocumentUpdateRequest> deserializer = new JsonDeserializer<>(DocumentUpdateRequest.class);
        deserializer.setUseTypeHeaders(false);
        return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, DocumentUpdateRequest> updateRequestContainer(ConsumerFactory<String, DocumentUpdateRequest> updateRequestConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, DocumentUpdateRequest> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(updateRequestConsumerFactory);
        return factory;
    }


}
