package com.lee.squaremarketbackend.repository;

import com.lee.squaremarketbackend.entity.Neighbor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface NeighborRepository extends JpaRepository<Neighbor, UUID> {

    Optional<Neighbor> findByEmail(String email);

    Optional<Neighbor> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}