package com.lee.squaremarketbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WantedPostRequest {

    @NotBlank
    private String summary;

    private String details;

    private List<String> images;

    private List<WantedPostItemRequest> items;
}