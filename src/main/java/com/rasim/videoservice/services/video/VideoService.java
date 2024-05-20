package com.rasim.videoservice.services.video;

import com.rasim.videoservice.dto.VideoDTO;

import java.util.List;
import java.util.Optional;

public interface VideoService {
    List<VideoDTO> findAllVideos();

    List<VideoDTO> findVideosByUserId(Long userId);

    Optional<VideoDTO> findVideoByID(Long id);

    VideoDTO saveVideo(VideoDTO dto);

    void deleteVideo(Long id);
}
