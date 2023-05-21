package com.example.core.controller;

import com.example.core.facade.FolderSharingFacade;
import com.example.core.model.request.FolderSharingRequest;
import com.example.core.model.request.FolderUnshareRequest;
import com.example.core.model.response.FolderSharingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/folder-sharing")
public class FolderSharingController {

    private final FolderSharingFacade folderSharingFacade;

    @PostMapping
    @Operation(
            security = @SecurityRequirement(name = "bearer-token")
    )
    public ResponseEntity<FolderSharingResponse> share(@RequestBody @Valid FolderSharingRequest request){
        folderSharingFacade.share(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    @Operation(
            security = @SecurityRequirement(name = "bearer-token")
    )
    public ResponseEntity<FolderSharingResponse> update(@RequestBody @Valid FolderSharingRequest request) {
        folderSharingFacade.update(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @Operation(
            security = @SecurityRequirement(name = "bearer-token")
    )
    public ResponseEntity<Void> unshare(@RequestBody @Valid FolderUnshareRequest request) {
        folderSharingFacade.unshare(request);
        return ResponseEntity.ok().build();
    }
}
