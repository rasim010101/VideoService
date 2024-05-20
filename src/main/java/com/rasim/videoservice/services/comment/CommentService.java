package com.rasim.videoservice.services.comment;

import com.rasim.videoservice.dto.CommentDTO;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    List<CommentDTO> findAllComments();
    List<CommentDTO> findCommentsByUserId(Long id);
    List<CommentDTO> findCommentsByVideoId(Long id);

    Optional<CommentDTO> findCommentById(Long id);

    CommentDTO saveComment(CommentDTO dto);

    void deleteComment(Long id);
}