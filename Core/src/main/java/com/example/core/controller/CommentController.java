package com.example.core.controller;

import com.example.core.model.request.CommentEditRequest;
import com.example.core.model.request.CommentRequest;
import com.example.core.model.response.CommentResponse;
import com.example.core.model.response.CommentShortResponse;
import com.example.core.service.CommentFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {


    private final CommentFacade commentFacade;

    @PostMapping
    @Operation(
            security = @SecurityRequirement(name = "bearer-token")
    )
    public ResponseEntity<CommentResponse> create(@RequestBody CommentRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(commentFacade.create(request));
    }
    @PutMapping
    @Operation(
            security = @SecurityRequirement(name = "bearer-token")
    )
    public ResponseEntity<CommentResponse> update(@RequestBody CommentEditRequest request){
        return ResponseEntity.ok(commentFacade.update(request));
    }

    @DeleteMapping("/{id}")
    @Operation(
            security = @SecurityRequirement(name = "bearer-token")
    )
    public ResponseEntity<Void> update(@PathVariable("id") Long id) {
        commentFacade.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{detailId}")
    @Operation(
            security = @SecurityRequirement(name = "bearer-token")
    )
    public ResponseEntity<List<CommentShortResponse>> get(@PathVariable("detailId") Long id) {
        return ResponseEntity.ok(commentFacade.get(id));
    }

    @GetMapping("/list/{detailId}")
    @Operation(
            security = @SecurityRequirement(name = "bearer-token")
    )
    public ResponseEntity<Page<CommentResponse>> view(@PathVariable("detailId") Long id,
                                                      @RequestParam("lineNumber") Long lineNumber,
                                                      @RequestParam("page") Integer page,
                                                      @RequestParam("size") Integer size,
                                                      @RequestParam("sort") String columnName,
                                                      @RequestParam("dir") Sort.Direction direction
                                                      ) {
        PageRequest pageRequest = PageRequest.of(page, size, direction, columnName);
        return ResponseEntity.ok(commentFacade.view(id,lineNumber,pageRequest));
    }


}
