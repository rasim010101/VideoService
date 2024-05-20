package com.rasim.videoservice.mappers;

import com.rasim.videoservice.dto.VideoDTO;
import com.rasim.videoservice.entities.Video;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface EventMapper {

    @Mapping(source = "createdBy", target = "createdBy.id")
    Video videoToVideoDto(VideoDTO dto);


    @Mapping(source = "createdBy.id", target = "createdBy")
    VideoDTO videoDtoToVideo(Video video);
}
