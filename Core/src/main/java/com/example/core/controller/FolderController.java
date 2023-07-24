package com.example.core.controller;

import com.example.core.facade.FolderFacade;
import com.example.core.model.request.FolderRequest;
import com.example.core.model.response.FolderResponse;
import com.example.core.model.response.FolderShortResponse;
import com.example.core.model.response.ItemShortResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/folder")
public class FolderController {

    private final FolderFacade folderFacade;

    @PostMapping
    @Operation(
            security = @SecurityRequirement(name = "bearer-token")
    )
    public ResponseEntity<FolderShortResponse> create(@RequestBody @Valid FolderRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(folderFacade.create(request));
    }
     @GetMapping
    @Operation(
            security = @SecurityRequirement(name = "bearer-token")
    )
    public ResponseEntity<Page<ItemShortResponse>> listMasterFolders(){
        return ResponseEntity.status(HttpStatus.CREATED).body(new PageImpl<>(folderFacade.listMasterFolders()));
    }

    @GetMapping("/{id}")
    @Operation(
            security = @SecurityRequirement(name = "bearer-token")
    )
    public ResponseEntity<Page<ItemShortResponse>> listContent(@PathVariable Long id,
                                                               @RequestParam("page") Integer page,
                                                               @RequestParam("size") Integer size) {
        return ResponseEntity.status(HttpStatus.CREATED).body(folderFacade.listContents(id,page,size));
    }

}
