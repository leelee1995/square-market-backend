package com.lee.squaremarketbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WantedPostResponse {

    private UUID id;
    private String summary;
    private String details;
    private List<String> images;
    private Long minPrice;
    private Long maxPrice;
    private List<String> items;
    private UUID neighborId;
    private String neighborUsername;
    private Instant createdAt;
    private Instant updatedAt;
}