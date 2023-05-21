package com.example.core.controller;

import com.example.core.facade.DocumentSharingFacade;
import com.example.core.model.request.DocumentSharingRequest;
import com.example.core.model.request.DocumentUnshareRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/document-sharing")
public class DocumentSharingController {

    private final DocumentSharingFacade documentSharingFacade;

    @PostMapping
    @Operation(
            security = @SecurityRequirement(name = "bearer-token")
    )
    public ResponseEntity<Void> share(@RequestBody @Valid DocumentSharingRequest request){
        documentSharingFacade.share(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    @Operation(
            security = @SecurityRequirement(name = "bearer-token")
    )
    public ResponseEntity<Void> update(@RequestBody @Valid DocumentSharingRequest request) {
        documentSharingFacade.update(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @Operation(
            security = @SecurityRequirement(name = "bearer-token")
    )
    public ResponseEntity<Void> unshare(@RequestBody @Valid DocumentUnshareRequest request) {
        documentSharingFacade.unshare(request);
        return ResponseEntity.ok().build();
    }
}
