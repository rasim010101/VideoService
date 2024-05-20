package com.rasim.videoservice.controllers;

import com.rasim.videoservice.dto.CommentDTO;
import com.rasim.videoservice.exceptions.NotFoundException;
import com.rasim.videoservice.services.comment.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/v1")
public class CommentApiController {
    private final String USER_PATH = "/user/{id}";
    private final String EVENT_PATH = "/event/{id}";
    private final String COMMENT_PATH = "/comment";
    private final String ID_PATH = COMMENT_PATH + "/{id}";

    private final CommentService commentService;

    @GetMapping(COMMENT_PATH)
    public List<CommentDTO> getAllComments() {
        return commentService.findAllComments();
    }
    @GetMapping(COMMENT_PATH + USER_PATH)
    public List<CommentDTO> getCommentsByUserId(@PathVariable Long id) {
        return commentService.findCommentsByUserId(id);
    }

    @GetMapping(COMMENT_PATH + EVENT_PATH)
    public List<CommentDTO> getCommentsByVideoId(@PathVariable Long id) {
        return commentService.findCommentsByVideoId(id);
    }

    @GetMapping(ID_PATH)
    public CommentDTO getById(@PathVariable Long id) {
        log.info("Getting comment with id: {}", id);
        return commentService.findCommentById(id).orElseThrow(() -> new NotFoundException("Comment not found with id: " + id));
    }

    @PostMapping(COMMENT_PATH)
    public CommentDTO createComment(@Validated @RequestBody CommentDTO commentDTO) {
        return commentService.saveComment(commentDTO);
    }

    @PutMapping(ID_PATH)
    public CommentDTO updateComment(@PathVariable Long id, @Validated @RequestBody CommentDTO commentDTO) {
        commentService.findCommentById(id).orElseThrow(() -> new NotFoundException("Comment not found with id: " + id));
        commentDTO.setCommentId(id);
        return commentService.saveComment(commentDTO);
    }

    @PatchMapping(ID_PATH)
    public CommentDTO patchComment(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        CommentDTO existingComment = commentService.findCommentById(id)
                .orElseThrow(() -> new NotFoundException("Comment not found with id: " + id));

        // Implement logic to update fields based on the updates map
        // For simplicity, assuming updates map contains only the fields to be updated
        updates.forEach((key, value) -> {
            switch (key) {
                case "text":
                    existingComment.setText((String) value);
                    break;
            }
        });

        return commentService.saveComment(existingComment);
    }

    @DeleteMapping(ID_PATH)
    public void deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
    }
}
