package com.lee.squaremarketbackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "wanted_post_item")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class WantedPostItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wanted_post_id", nullable = false)
    private WantedPost wantedPost;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private WantedPostItemStatus status = WantedPostItemStatus.NEED;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ItemCondition condition = ItemCondition.ANY;
}