package com.lee.squaremarketbackend.repository;

import com.lee.squaremarketbackend.entity.Listing;
import com.lee.squaremarketbackend.entity.ListingCategory;
import com.lee.squaremarketbackend.entity.ListingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ListingRepository extends JpaRepository<Listing, UUID> {

    List<Listing> findByNeighborId(UUID neighborId);

    Page<Listing> findByCategory(ListingCategory category, Pageable pageable);
    //Page<Listing> findByStatus(ListingStatus status, Pageable pageable);
}