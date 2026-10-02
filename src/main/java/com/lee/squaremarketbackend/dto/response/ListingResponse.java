package com.lee.squaremarketbackend.dto.response;

import com.lee.squaremarketbackend.entity.ItemCondition;
import com.lee.squaremarketbackend.entity.ListingStatus;
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
public class ListingResponse {

    private UUID id;
    private String title;
    private String details;
    private Long price;
    private List<String> images;
    private ListingStatus status;
    private ItemCondition condition;
    private String municipality;
    private String administrativeDivision;
    private String country;
    private UUID neighborId;
    private String neighborUsername;
    private Instant createdAt;
    private Instant updatedAt;
}