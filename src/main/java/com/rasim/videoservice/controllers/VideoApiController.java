package com.rasim.videoservice.controllers;

import com.rasim.videoservice.dto.VideoDTO;
import com.rasim.videoservice.exceptions.NotFoundException;
import com.rasim.videoservice.services.video.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/v1")
public class VideoApiController {

    private final String VIDEO_PATH = "/video";
    private final String USER_PATH = "/user/{id}";
    private final String ID_PATH = VIDEO_PATH + "/{id}";

    private final VideoService videoService;

    @GetMapping("/video")
    public List<VideoDTO> getAllVideos() {
        return videoService.findAllVideos();
    }

    @GetMapping(ID_PATH)
    public VideoDTO getById(@PathVariable Long id) {
        log.info("Getting video with id: {}", id);
        return videoService.findVideoByID(id).orElseThrow(() -> new NotFoundException("Video not found with id: " + id));
    }

    @GetMapping(VIDEO_PATH + USER_PATH)
    public List<VideoDTO> getByUserId(@PathVariable Long id) {
        return videoService.findVideosByUserId(id);
    }

    @PostMapping(VIDEO_PATH)
    public VideoDTO createVideo(@Validated @RequestBody VideoDTO videoDTO) {
        return videoService.saveVideo(videoDTO);
    }

    @PutMapping(ID_PATH)
    public VideoDTO updateVideo(@PathVariable Long id, @Validated @RequestBody VideoDTO videoDTO) {
        videoService.findVideoByID(id).orElseThrow(() -> new NotFoundException("Video not found with id: " + id));
        videoDTO.setVideoId(id);
        return videoService.saveVideo(videoDTO);
    }

    @PatchMapping(ID_PATH)
    public VideoDTO patchVideo(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        VideoDTO existingVideo = videoService.findVideoByID(id)
                .orElseThrow(() -> new NotFoundException("Video not found with id: " + id));

        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    existingVideo.setVideoName((String) value);
                    break;
                case "description":
                    existingVideo.setDescription((String) value);
                    break;
                case "date":
                    existingVideo.setDate((Date) value);
                    break;
            }
        });

        return videoService.saveVideo(existingVideo);
    }

    @DeleteMapping(ID_PATH)
    public void deleteVideo(@PathVariable Long id) {
        videoService.deleteVideo(id);
    }
}
