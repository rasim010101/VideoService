package com.rasim.videoservice.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Video {
    @Id
    @GeneratedValue
    private Long videoId;

    @NotBlank(message = "Video name is required")
    private String videoName;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Date is required")
    private Date date;

    @NotNull(message = "Created by user is required")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User createdBy;

    @OneToMany(mappedBy = "video")
    @Builder.Default
    private Set<Comment> comments = new HashSet<>();
}
