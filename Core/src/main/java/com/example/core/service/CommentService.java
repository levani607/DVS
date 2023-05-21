package com.example.core.service;

import com.example.core.exceptions.CoreException;
import com.example.core.exceptions.ErrorCode;
import com.example.core.model.entity.Comment;
import com.example.core.model.enums.CommentStatus;
import com.example.core.model.response.CommentResponse;
import com.example.core.model.response.CommentShortResponse;
import com.example.core.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public Comment save(Comment comment){
        return commentRepository.save(comment);
    }

    public Comment findById(Long id){
        return commentRepository.findByIdAndStatus(id, CommentStatus.ACTIVE)
                .orElseThrow(()->{throw new CoreException(ErrorCode.FORBIDDEN,"Comment not found");});
    }
    public List<CommentShortResponse> findCommentsByDetailId(Long detailId){
        return commentRepository.findCommentsByDetailId(detailId,CommentStatus.ACTIVE);
    }

    public Page<CommentResponse> findByDetailIdAndLineNumber(Long detailId,
                                                             Long lineNumber,
                                                             PageRequest pageRequest) {
        return commentRepository.findByDetailIdAndLineNumber(detailId,lineNumber, CommentStatus.ACTIVE,pageRequest);
    }
}
