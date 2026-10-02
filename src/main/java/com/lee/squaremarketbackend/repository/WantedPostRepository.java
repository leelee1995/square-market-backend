package com.lee.squaremarketbackend.repository;

import com.lee.squaremarketbackend.entity.WantedPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WantedPostRepository extends JpaRepository<WantedPost, UUID> {

    List<WantedPost> findByNeighborId(UUID neighborId);

    Page<WantedPost> findAllBy(Pageable pageable);
}