package com.lee.squaremarketbackend.service;

import com.lee.squaremarketbackend.dto.request.WantedPostRequest;
import com.lee.squaremarketbackend.dto.response.WantedPostResponse;
import com.lee.squaremarketbackend.entity.Neighbor;
import com.lee.squaremarketbackend.entity.WantedPost;
import com.lee.squaremarketbackend.entity.WantedPostItem;
import com.lee.squaremarketbackend.entity.WantedPostItemStatus;
import com.lee.squaremarketbackend.repository.NeighborRepository;
import com.lee.squaremarketbackend.repository.WantedPostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WantedPostService {

    private final WantedPostRepository wantedPostRepository;
    private final NeighborRepository neighborRepository;

    public WantedPostResponse getById(UUID id) {
        WantedPost wantedPost = wantedPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Wanted post not found"));
        return toWantedPostResponse(wantedPost);
    }

    public Page<WantedPostResponse> getAll(Pageable pageable) {
        return wantedPostRepository.findAllBy(pageable).map(this::toWantedPostResponse);
    }

    public List<WantedPostResponse> getMine(UUID uid) {
        return wantedPostRepository.findByNeighborId(uid).stream()
                .map(this::toWantedPostResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public WantedPostResponse create(UUID uid, WantedPostRequest request) {

        Neighbor neighbor = neighborRepository
                .findById(uid)
                .orElseThrow(() -> {
                    log.warn("Authenticated user no longer exist: {}", uid);
                    return new IllegalArgumentException("Authenticated user error");
                });

        WantedPost wantedPost = WantedPost.builder()
                .summary(request.getSummary())
                .details(request.getDetails())
                .images(request.getImages() != null ? request.getImages() : new java.util.ArrayList<>())
                .neighbor(neighbor)
                .build();

        if (request.getItems() != null) {
            List<WantedPostItem> items = request.getItems().stream()
                    .map(itemRequest -> WantedPostItem.builder()
                            .title(itemRequest.getTitle())
                            .wantedPost(wantedPost)
                            .build())
                    .collect(Collectors.toList());
            wantedPost.setItems(items);
        }

        wantedPostRepository.save(wantedPost);

        log.info("Wanted post created: {} by user: {}", wantedPost.getId(), uid);

        return toWantedPostResponse(wantedPost);
    }

    @Transactional
    public WantedPostResponse update(UUID uid, UUID postId, WantedPostRequest request) {

        WantedPost wantedPost = getOwnedWantedPostOrThrow(uid, postId);

        wantedPost.setSummary(request.getSummary());
        wantedPost.setDetails(request.getDetails());

        if (request.getImages() != null) {
            wantedPost.setImages(request.getImages());
        }

        log.info("Wanted post updated: {} by user: {}", postId, uid);

        return toWantedPostResponse(wantedPost);
    }

    @Transactional
    public void delete(UUID uid, UUID postId) {
        WantedPost wantedPost = getOwnedWantedPostOrThrow(uid, postId);
        wantedPostRepository.delete(wantedPost); // intercepted by @SQLDelete -> soft delete
        log.info("Wanted post soft-deleted: {} by user: {}", postId, uid);
    }

    private WantedPost getOwnedWantedPostOrThrow(UUID uid, UUID postId) {
        WantedPost wantedPost = wantedPostRepository
                .findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Wanted post not found"));

        if (!wantedPost.getNeighbor().getId().equals(uid)) {
            log.warn("User {} attempted to modify wanted post {} they don't own", uid, postId);
            throw new IllegalArgumentException("Not the owner of this wanted post");
        }

        return wantedPost;
    }

    private WantedPostResponse toWantedPostResponse(WantedPost wantedPost) {
        return WantedPostResponse.builder()
                .id(wantedPost.getId())
                .summary(wantedPost.getSummary())
                .details(wantedPost.getDetails())
                .images(wantedPost.getImages())
                .minPrice(wantedPost.getMinPrice())
                .maxPrice(wantedPost.getMaxPrice())
                .items(wantedPost.getItems().stream()
                        .map(WantedPostItem::getTitle)
                        .collect(Collectors.toList()))
                .neighborId(wantedPost.getNeighbor().getId())
                .neighborUsername(wantedPost.getNeighbor().getUsername())
                .createdAt(wantedPost.getCreatedAt())
                .updatedAt(wantedPost.getUpdatedAt())
                .build();
    }
}