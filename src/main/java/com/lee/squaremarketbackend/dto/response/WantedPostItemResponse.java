package com.lee.squaremarketbackend.dto.response;

import com.lee.squaremarketbackend.entity.WantedPostItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WantedPostItemResponse {
    private UUID id;
    private String title;
    private WantedPostItemStatus status;
    private UUID wantedPostId;
}