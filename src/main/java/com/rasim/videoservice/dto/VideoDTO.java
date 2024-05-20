package com.rasim.videoservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VideoDTO {

    private Long videoId;

    @NotBlank(message = "Event name is required")
    private String videoName;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Date is required")
    private Date date;

    @NotNull(message = "Created by user is required")
    private Long createdBy;
}
