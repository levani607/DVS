package com.example.core.model.request;

import lombok.Data;

@Data
public class DocumentUpdateRequest {
    private Long id;
    private String text;
}
