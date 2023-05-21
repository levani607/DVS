package com.example.core.util;

import com.example.core.model.request.UserRegistrationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;


import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class MyJsonDeserializer implements Deserializer<UserRegistrationRequest> {

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public UserRegistrationRequest deserialize(String topic, byte[] data) {
        try {
            if (data == null){
                System.out.println("Null received at deserializing");
                return null;
            }
            System.out.println("Deserializing...");
            return objectMapper.readValue(new String(data, StandardCharsets.UTF_8), UserRegistrationRequest.class);
        } catch (Exception e) {
            throw new SerializationException("Error when deserializing byte[] to MessageDto");
        }
    }
}
