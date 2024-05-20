package com.rasim.videoservice.services.comment;

import com.rasim.videoservice.dto.CommentDTO;
import com.rasim.videoservice.entities.Comment;
import com.rasim.videoservice.exceptions.NotFoundException;
import com.rasim.videoservice.mappers.CommentMapper;
import com.rasim.videoservice.repositories.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServiceJPA implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public CommentServiceJPA(CommentMapper commentMapper, CommentRepository commentRepository) {
        this.commentMapper = commentMapper;
        this.commentRepository = commentRepository;
    }

    @Override
    public List<CommentDTO> findAllComments() {
        List<Comment> comments = (List<Comment>) commentRepository.findAll();
        return comments.stream()
                .map(commentMapper::commentToCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDTO> findCommentsByUserId(Long id) {
        List<Comment> comments = (List<Comment>) commentRepository.findByUserId(id);
        return comments.stream()
                .map(commentMapper::commentToCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDTO> findCommentsByVideoId(Long id) {
        List<Comment> comments = (List<Comment>) commentRepository.findByVideo_VideoId(id);
        return comments.stream()
                .map(commentMapper::commentToCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CommentDTO> findCommentById(Long id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        Comment comment = optionalComment.orElseThrow(() -> new NotFoundException("Comment not found with id: " + id));
        return Optional.of(commentMapper.commentToCommentDto(comment));
    }

    @Override
    public CommentDTO saveComment(CommentDTO dto) {
        Comment savedComment = commentRepository.save(commentMapper.commentDtoToComment(dto));
        return commentMapper.commentToCommentDto(savedComment);
    }

    @Override
    public void deleteComment(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new NotFoundException("Comment not found with id: " + id);
        }
        commentRepository.deleteById(id);
    }
}
