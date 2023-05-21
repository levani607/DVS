package com.example.core.model.response;

import com.example.core.model.entity.Comment;
import lombok.Data;

@Data
public class CommentResponse {
    private Long commentId;
    private Long lineNumber;
    private String text;

    public CommentResponse(Comment comment) {
        this.commentId = comment.getId();
        this.lineNumber = comment.getLineNumber();
        this.text = comment.getText();
    }

    public CommentResponse(Long commentId, Long lineNumber, String text) {
        this.commentId = commentId;
        this.lineNumber = lineNumber;
        this.text = text;
    }
}
