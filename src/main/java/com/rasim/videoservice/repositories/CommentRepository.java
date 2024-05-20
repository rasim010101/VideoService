package com.rasim.videoservice.repositories;

import com.rasim.videoservice.entities.Comment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment,Long> {
    List<Comment> findByUserId(Long userId);
    List<Comment> findByVideo_VideoId(Long videoId);
}
