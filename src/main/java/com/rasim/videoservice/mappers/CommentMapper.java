package com.rasim.videoservice.mappers;

import com.rasim.videoservice.dto.CommentDTO;
import com.rasim.videoservice.entities.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CommentMapper {

    @Mapping(source = "videoId", target = "video.videoId")
    @Mapping(source = "userId", target = "user.id")
    Comment commentDtoToComment(CommentDTO dto);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "video.videoId", target = "videoId")
    CommentDTO commentToCommentDto(Comment comment);
}
