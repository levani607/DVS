package com.example.core.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class KeycloakConfigContainer {

    @Value("${KEYCLOAK_HOST}")
    private String serverUrl;

    @Value("${REALM}")
    private String realm;

    @Value("${CLIENT_ID}")
    private String clientId;

    @Value("${CLIENT_SECRET}")
    private String clientSecret;

    @Value("${ADMIN_CLIENT_ID}")
    private String adminId;

    @Value("${ADMIN_SECRET}")
    private String adminSecret;

    @Value("${REFRESH_TOKEN_URI}")
    private String refreshTokenUri;
    @Value("${POOL_SIZE}")
    private Integer poolSize;
    @Value("${TOKEN_ENDPOINT}")
    private String tokenEndpoint;
}
