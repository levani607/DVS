package com.example.core.controller;

import com.example.core.facade.UserFacade;
import com.example.core.model.request.RegistrationRequest;
import com.example.core.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserFacade userFacade;


    @PostMapping("/image")
    @Operation(
            security = @SecurityRequirement(name = "bearer-token")
    )
    public ResponseEntity<Void> updateUserImage(@RequestBody String image) {
        userFacade.updateUserImage(image);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/deactivate")
    @Operation(
            security = @SecurityRequirement(name = "bearer-token")
    )
    public ResponseEntity<Void> deactivate() {
        userFacade.deactivateAccount();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/activate")
    @Operation(
            security = @SecurityRequirement(name = "bearer-token")
    )
    public ResponseEntity<Void> activate() {
        userFacade.activateAccount();
        return ResponseEntity.ok().build();
    }

}
