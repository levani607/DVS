package com.example.core.controller.kafka;

import com.example.core.facade.UserFacade;
import com.example.core.model.request.UserRegistrationRequest;
import com.example.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeycloakListener {

    private final UserFacade userFacade;

    @KafkaListener(topics = {"${myapp.topics.userTopic}"})
    public void createRequiredParts(UserRegistrationRequest request) {

        userFacade.createUser(request);

    }
}
