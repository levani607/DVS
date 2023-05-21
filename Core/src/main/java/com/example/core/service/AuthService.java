package com.example.core.service;

import com.example.core.configs.KeycloakConfigContainer;
import com.example.core.model.response.KeycloakLoginResponse;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ResponseStatusException;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final Keycloak adminClient;
    private final KeycloakConfigContainer configs;


    public void changeStatus(String keyCloakId,boolean status) {
        UserResource userResource = adminClient.realm(configs.getRealm()).users().get(keyCloakId);
        UserRepresentation userRepresentation = userResource.toRepresentation();
        userRepresentation.setEnabled(status);
        userResource.update(userRepresentation);
    }

    public void setEmailVerifyAction(String keyCloakId){
        UserResource userResource = adminClient.realm(configs.getRealm()).users().get(keyCloakId);
        UserRepresentation userRepresentation = userResource.toRepresentation();
        userRepresentation.setEmailVerified(false);
        userRepresentation.setRequiredActions(List.of("VERIFY_EMAIL"));
    }

}
