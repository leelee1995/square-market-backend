package com.lee.squaremarketbackend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// NeighborEmailPatchRequest.java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NeighborEmailPatchRequest {
    @NotBlank
    @Email
    private String email;
}