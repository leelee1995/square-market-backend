package com.lee.squaremarketbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "wanted_post")
@SQLDelete(sql = "UPDATE wanted_post SET deleted_at = now() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class WantedPost extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String summary;

    @Column(columnDefinition = "TEXT")
    private String details;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "images", columnDefinition = "text[]", nullable = false)
    @Builder.Default
    private List<String> images = new ArrayList<>();

    @Column(name = "min_price", nullable = false)
    @Builder.Default
    private Long minPrice = 0L;

    @Column(name = "max_price", nullable = false)
    @Builder.Default
    private Long maxPrice = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "neighbor_id", nullable = false)
    private Neighbor neighbor;

    @OneToMany(mappedBy = "wantedPost", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<WantedPostItem> items = new ArrayList<>();
}