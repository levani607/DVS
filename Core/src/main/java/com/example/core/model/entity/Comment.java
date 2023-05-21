package com.example.core.model.entity;

import com.example.core.model.enums.CommentStatus;
import com.example.core.model.request.CommentRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Comment extends BaseEntity{

    @Column(name = "detail_id")
    private Long detailId;
    @Column(name = "line_number")
    private Long lineNumber;
    @Column(name = "text")
    private String text;
    @JoinColumn(name = "user_id")
    @ManyToOne
    private User owner;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private CommentStatus status;

    public Comment(CommentRequest request) {
        this.detailId = request.getDetailId();
        this.lineNumber = request.getLineNumber();
        this.text = request.getText();
        this.status = CommentStatus.ACTIVE;
    }
}
