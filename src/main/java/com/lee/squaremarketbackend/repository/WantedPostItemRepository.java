package com.lee.squaremarketbackend.repository;

import com.lee.squaremarketbackend.entity.WantedPostItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WantedPostItemRepository extends JpaRepository<WantedPostItem, UUID> {
    List<WantedPostItem> findByWantedPostId(UUID wantedPostId);
}