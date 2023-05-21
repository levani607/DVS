package com.example.core.service;

import com.example.core.exceptions.CoreException;
import com.example.core.exceptions.ErrorCode;
import com.example.core.model.UserStatus;
import com.example.core.model.entity.User;
import com.example.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthService authService;

    public User findActiveById(Long id) {
        return userRepository
                .findByIdAndStatus(id, List.of(UserStatus.ACTIVE))
                .orElseThrow(() -> {
                    throw new CoreException(ErrorCode.OBJECT_NOT_FOUND, "User not found on given id");
                });
    }

    public User findActiveByKeyCloakId(String id) {
        return userRepository
                .findByKeycloakIdAndStatus(id, List.of(UserStatus.ACTIVE))
                .orElseThrow(() -> {
                    throw new CoreException(ErrorCode.OBJECT_NOT_FOUND, "User not found on given id");
                });
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User findLoggedInUser() {
        JwtAuthenticationToken jwt = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        String keycloakId = jwt.getName();

        if (keycloakId == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Authorization error");
        }
        return findActiveByKeyCloakId(keycloakId);
    }
}
