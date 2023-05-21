package com.example.core.model.request;

import lombok.Data;

@Data
public class DocumentRequest {


    private String name;
    private Long folderId;
    private String text;

}
