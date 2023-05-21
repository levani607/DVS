package com.example.core.controller;

import com.example.core.facade.DocumentFacade;
import com.example.core.model.request.ChangeApprovalRequest;
import com.example.core.model.request.DocumentRequest;
import com.example.core.model.request.FilterRequest;
import com.example.core.model.response.DocumentShortResponse;
import com.example.core.model.response.FilterResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/document")
public class DocumentController {

    private final DocumentFacade documentFacade;

    @PostMapping
    @Operation(
            security = @SecurityRequirement(name = "bearer-token")
    )
    public ResponseEntity<DocumentShortResponse> create(@RequestBody @Valid DocumentRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(documentFacade.create(request));
    }

    @PostMapping("/approve")
    @Operation(
            security = @SecurityRequirement(name = "bearer-token")
    )
    public ResponseEntity<Void> approveChange(@RequestBody @Valid ChangeApprovalRequest request) {
        documentFacade.approveChange(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/filter")
    @Operation(
            security = @SecurityRequirement(name = "bearer-token")
    )
    public ResponseEntity<FilterResponse> filterByName(@Valid FilterRequest request) {
        return ResponseEntity.ok(documentFacade.filterByName(request));
    }
}
