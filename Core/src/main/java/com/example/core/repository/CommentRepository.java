package com.example.core.repository;

import com.example.core.model.entity.Comment;
import com.example.core.model.enums.CommentStatus;
import com.example.core.model.response.CommentResponse;
import com.example.core.model.response.CommentShortResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {


    Optional<Comment> findByIdAndStatus(Long id, CommentStatus status);

    @Query("""
            select new com.example.core.model.response.CommentShortResponse(c.id,c.lineNumber)
            from Comment c
            where c.detailId = :detailId
            and c.status=:status
            """)
    List<CommentShortResponse> findCommentsByDetailId(@Param("detailId") Long detailId,
                                                      @Param("status") CommentStatus status);

    @Query("""
            select new com.example.core.model.response.CommentResponse(c.id,c.lineNumber,c.text)
            from Comment c
            where c.detailId = :detailId
            and c.lineNumber = :lineNumber
            and c.status=:status
            """)
    Page<CommentResponse> findByDetailIdAndLineNumber(@Param("detailId")Long detailId,
                                                      @Param("lineNumber")Long lineNumber,
                                                      @Param("status")CommentStatus status,
                                                      PageRequest request );
}
