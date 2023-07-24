package com.example.core.configs;

import com.example.core.util.KeycloakRoleConverter;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.apache.http.impl.client.HttpClients;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.ws.rs.client.ClientBuilder;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.config.Customizer.withDefaults;

@org.springframework.context.annotation.Configuration
public class AuthConfig {
    @Bean
    public Keycloak keycloak(KeycloakConfigContainer keycloakConfigContainer) {
        ClientBuilder clientBuilder = ResteasyClientBuilder.newBuilder();

        return KeycloakBuilder.builder()
                .serverUrl(keycloakConfigContainer.getServerUrl())
                .realm(keycloakConfigContainer.getRealm())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(keycloakConfigContainer.getAdminId())
                .clientSecret(keycloakConfigContainer.getAdminSecret())
                .resteasyClient(clientBuilder
                        .build()
                )
                .build();

    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());

        http.csrf().disable()
                .cors()
                .disable()
                .authorizeRequests()
                .antMatchers(
                        "/client/auth/login",
                        "/client/auth/refresh-token",
                        "/client/auth/reset-password",
                        "/client/auth/send-reset-code",
                        "/client/auth/check-code",
                        "/client/auth/otp-access",
                        "/swagger-ui/**",
                        "/v2/api-docs/**",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/actuator/**",
                        "/swagger-ui.html/**"
                ).permitAll()
                .antMatchers(HttpMethod.OPTIONS,"/**")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(jwtAuthenticationConverter);
        return http.build();
    }


    @Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-token",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }
}
