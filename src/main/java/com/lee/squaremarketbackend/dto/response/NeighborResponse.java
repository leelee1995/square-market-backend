package com.lee.squaremarketbackend.dto.response;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NeighborResponse {

    private UUID id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String country;
    private String administrativeDivision;
    private String municipality;
    private Instant createdAt;
}
