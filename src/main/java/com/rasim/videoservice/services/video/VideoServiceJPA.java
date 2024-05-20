package com.rasim.videoservice.services.video;

import com.rasim.videoservice.dto.VideoDTO;
import com.rasim.videoservice.entities.Video;
import com.rasim.videoservice.exceptions.InternalServerErrorException;
import com.rasim.videoservice.exceptions.InvalidRequestException;
import com.rasim.videoservice.exceptions.NotFoundException;
import com.rasim.videoservice.mappers.VideoMapper;
import com.rasim.videoservice.repositories.VideoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VideoServiceJPA implements VideoService {
    private final VideoRepository videoRepository;
    private final VideoMapper videoMapper;

    public VideoServiceJPA(VideoMapper videoMapper, VideoRepository videoRepository, VideoRepository videoRepository1) {
        this.videoMapper = videoMapper;
        this.videoRepository = videoRepository1;
    }

    @Override
    public List<VideoDTO> findAllVideos() {
        List<Video> videos = (List<Video>) videoRepository.findAll();
        return videos.stream()
                .map(videoMapper::videoToVideoDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<VideoDTO> findVideosByUserId(Long userId) {
        List<Video> videos = (List<Video>) videoRepository.findByCreatedById(userId);
        return videos.stream()
                .map(videoMapper::videoToVideoDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<VideoDTO> findVideoByID(Long id) {
        Optional<Video> optionalVideo = videoRepository.findById(id);
        Video video = optionalVideo.orElseThrow(() -> new NotFoundException("Video not found with id: " + id));
        return Optional.of(videoMapper.videoToVideoDto(video));
    }

    @Override
    public VideoDTO saveVideo(VideoDTO dto) {
        if (dto == null) {
            throw new InvalidRequestException("VideoDTO cannot be null");
        }
        Video savedVideo = videoRepository.save(videoMapper.videoDtoToVideo(dto));
        return videoMapper.videoToVideoDto(savedVideo);
    }

    @Override
    public void deleteVideo(Long id) {
        try {
            videoRepository.deleteById(id);
        } catch (Exception ex) {
            throw new InternalServerErrorException("Failed to delete video with id: " + id);
        }
    }
}
