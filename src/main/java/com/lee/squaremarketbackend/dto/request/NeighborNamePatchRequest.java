package com.lee.squaremarketbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NeighborNamePatchRequest {
    @NotBlank
    @Size(min = 2, max = 255)
    private String firstName;

    @Size(max = 255)
    private String lastName;
}