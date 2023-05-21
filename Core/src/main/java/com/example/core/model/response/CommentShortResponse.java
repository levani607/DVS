package com.example.core.model.response;

import lombok.Data;

@Data
public class CommentShortResponse {
    private Long commentId;
    private Long lineNumber;

    public CommentShortResponse(Long commentId, Long lineNumber) {
        this.commentId = commentId;
        this.lineNumber = lineNumber;
    }
}
