package com.lee.squaremarketbackend.dto.request;

import com.lee.squaremarketbackend.entity.ItemCondition;
import com.lee.squaremarketbackend.entity.ListingCategory;
import com.lee.squaremarketbackend.entity.ListingStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ListingRequest {

    @NotBlank
    private String title;

    private String details;

    @NotNull
    @PositiveOrZero
    private Long price;

    private List<String> images;

    private ListingStatus status;

    @NotNull
    private ItemCondition condition;

    @NotNull
    private ListingCategory category;

    @NotNull
    private String municipality;

    @NotNull
    private String administrativeDivision;

    @NotNull
    private String country;
}