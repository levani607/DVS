package com.example.core.service;

import com.example.core.exceptions.CoreException;
import com.example.core.exceptions.ErrorCode;
import com.example.core.model.entity.*;
import com.example.core.model.enums.CommentStatus;
import com.example.core.model.request.CommentEditRequest;
import com.example.core.model.request.CommentRequest;
import com.example.core.model.response.CommentResponse;
import com.example.core.model.response.CommentShortResponse;
import com.example.core.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentFacade {
    private final CommentService commentService;
    private final UserService userService;
    private final DocumentSharingService documentSharingService;
    private final DocumentVersionService documentVersionService;

    public CommentResponse create(CommentRequest request) {
        User loggedInUser = userService.findLoggedInUser();
        checkDocumentStatus(request.getDetailId(), loggedInUser);
        Comment save = commentService.save(new Comment(request));
        return new CommentResponse(save);
    }

    public CommentResponse update(CommentEditRequest request) {
        User loggedInUser = userService.findLoggedInUser();
        Comment comment = commentService.findById(request.getCommentId());
        if (!comment.getOwner().getId().equals(loggedInUser.getId())) {
            throw new CoreException(ErrorCode.FORBIDDEN, "You can not edit this comment");
        }
        checkDocumentStatus(comment.getDetailId(), loggedInUser);
        comment.setText(request.getText());
        comment.setLineNumber(request.getLineNumber());
        Comment save = commentService.save(comment);
        return new CommentResponse(save);
    }

    public void checkDocumentStatus(Long detailId, User loggedInUser) {
        DocumentVersion documentVersion = documentVersionService.findByIdAndStatus(detailId);
        Document document = documentVersion.getDocument();
        User user = document.getUser();
        boolean isOwner = user.getId().equals(loggedInUser.getId());
        Optional<DocumentSharingContract> accessToDocument = documentSharingService.findAccessToDocument(loggedInUser.getId(), document.getId());
        if (accessToDocument.isEmpty() && !isOwner) {
            throw new CoreException(ErrorCode.FORBIDDEN, "You do not have access to this document!");
        }
    }

    public void delete(Long id) {
        User loggedInUser = userService.findLoggedInUser();
        Comment comment = commentService.findById(id);
        boolean commentOwner = comment.getOwner().getId().equals(loggedInUser.getId());
        DocumentVersion documentVersion = documentVersionService.findByIdAndStatus(comment.getDetailId());
        boolean documentOwner = loggedInUser.getId().equals(documentVersion.getDocument().getId());
        if (!commentOwner && !documentOwner) {
            throw new CoreException(ErrorCode.FORBIDDEN, "You can not delete this comment");
        }
        comment.setStatus(CommentStatus.DELETED);
        commentService.save(comment);
    }

    public List<CommentShortResponse> get(Long detailsId) {
        return commentService.findCommentsByDetailId(detailsId);
    }

    public Page<CommentResponse> view(Long detailsId, Long lineNumber, PageRequest pageRequest) {
        return commentService.findByDetailIdAndLineNumber(detailsId,lineNumber,pageRequest);
    }
}
