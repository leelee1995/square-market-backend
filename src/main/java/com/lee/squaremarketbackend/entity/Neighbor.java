// entity/Neighbor.java
package com.lee.squaremarketbackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "neighbor")
@SQLDelete(sql = "UPDATE neighbor SET deleted_at = now(), " +
        "username = left(username, 18) || '_del_' || left(id::text, 8), " +
        "email = email || '_del_' || id " +
        "WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Neighbor extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 32)
    private String username;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(nullable = false, unique = true)
    @Email
    private String email;

    @Column(nullable = false, length = 72)
    private String password;

    @Column
    private String country;

    //  State/province
    @Column(name = "administrative_division")
    private String administrativeDivision;

    //  City/town/village
    @Column
    private String municipality;

    @OneToMany(mappedBy = "neighbor", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<Listing> listings = new ArrayList<>();

    @OneToMany(mappedBy = "neighbor", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<WantedPost> wantedPosts = new ArrayList<>();
}