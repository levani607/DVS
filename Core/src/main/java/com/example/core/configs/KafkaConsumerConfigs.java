package com.example.core.configs;

import com.example.core.model.request.KafkaDocumentVersionRequest;
import com.example.core.model.request.UserRegistrationRequest;
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
    @Value("${myapp.topics.groupId}")
    String groupId;
    private Map<String, Object> createConfig() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        return configProps;
    }
    @Bean
    public ConsumerFactory<String, UserRegistrationRequest> userRegistrationConsumerFactory() {
        Map<String, Object> configProps=createConfig();
        JsonDeserializer<UserRegistrationRequest> deserializer = new JsonDeserializer<>(UserRegistrationRequest.class);
        deserializer.setUseTypeHeaders(false);
        return new DefaultKafkaConsumerFactory<>(configProps,new StringDeserializer(), deserializer);
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String,UserRegistrationRequest> kafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String, UserRegistrationRequest> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(userRegistrationConsumerFactory());
        return factory;
    }


    @Bean
    public ConsumerFactory<String, KafkaDocumentVersionRequest> documentVersionConsumerFactory() {

        Map<String, Object> configProps=createConfig();
        JsonDeserializer<KafkaDocumentVersionRequest> deserializer = new JsonDeserializer<>(KafkaDocumentVersionRequest.class);
        deserializer.setUseTypeHeaders(false);
        return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(), deserializer);
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, KafkaDocumentVersionRequest> documentVersionContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, KafkaDocumentVersionRequest> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(documentVersionConsumerFactory());
        return factory;
    }
}
