package com.rasim.videoservice.mappers;

import com.rasim.videoservice.dto.VideoDTO;
import com.rasim.videoservice.entities.Video;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface VideoMapper {

    @Mapping(source = "createdBy.id", target = "createdBy")
    VideoDTO videoToVideoDto(Video video);


    @Mapping(source = "createdBy", target = "createdBy.id")
    Video videoDtoToVideo(VideoDTO videoDTO);
}
