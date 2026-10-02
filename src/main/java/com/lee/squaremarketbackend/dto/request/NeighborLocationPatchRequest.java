package com.lee.squaremarketbackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// NeighborLocationPatchRequest.java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NeighborLocationPatchRequest {
    private String country;
    private String administrativeDivision;
    private String municipality;
}