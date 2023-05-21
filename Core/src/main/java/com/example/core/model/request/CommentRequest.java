package com.example.core.model.request;

import lombok.Data;

@Data
public class CommentRequest {
    private Long detailId;
    private Long lineNumber;
    private String text;
}
