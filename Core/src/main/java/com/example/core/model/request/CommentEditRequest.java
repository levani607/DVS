package com.example.core.model.request;

import lombok.Data;

@Data
public class CommentEditRequest {
    private Long commentId;
    private Long lineNumber;
    private String text;
}
