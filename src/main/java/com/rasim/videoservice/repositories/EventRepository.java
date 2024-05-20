package com.rasim.videoservice.repositories;

import com.rasim.videoservice.entities.Video;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EventRepository extends CrudRepository<Video, Long> {
    List<Video> findByCreatedById(Long userId);
}
