// dto/request/WantedPostItemRequest.java
package com.lee.squaremarketbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WantedPostItemRequest {
    @NotBlank
    private String title;
}