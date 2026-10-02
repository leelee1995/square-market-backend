package com.lee.squaremarketbackend.service;

import com.lee.squaremarketbackend.dto.request.WantedPostItemPatchRequest;
import com.lee.squaremarketbackend.dto.request.WantedPostItemRequest;
import com.lee.squaremarketbackend.dto.response.WantedPostItemResponse;
import com.lee.squaremarketbackend.entity.WantedPost;
import com.lee.squaremarketbackend.entity.WantedPostItem;
import com.lee.squaremarketbackend.entity.WantedPostItemStatus;
import com.lee.squaremarketbackend.repository.WantedPostItemRepository;
import com.lee.squaremarketbackend.repository.WantedPostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WantedPostItemService {

    private final WantedPostItemRepository wantedPostItemRepository;
    private final WantedPostRepository wantedPostRepository;

    @Transactional
    public WantedPostItemResponse create(UUID uid, UUID postId, WantedPostItemRequest request) {

        WantedPost wantedPost = getOwnedWantedPostOrThrow(uid, postId);

        WantedPostItem item = WantedPostItem.builder()
                .title(request.getTitle())
                .wantedPost(wantedPost)
                .build();

        wantedPostItemRepository.save(item);

        log.info("Wanted post item created: {} on post: {} by user: {}", item.getId(), postId, uid);

        return toWantedPostItemResponse(item);
    }

    @Transactional
    public WantedPostItemResponse update(UUID uid, UUID postId, UUID itemId, WantedPostItemPatchRequest request) {

        WantedPostItem item = getOwnedItemOrThrow(uid, postId, itemId);

        if (request.getTitle() != null) {
            item.setTitle(request.getTitle());
        }
        if (request.getStatus() != null) {
            item.setStatus(request.getStatus());
        }
        if (request.getCondition() != null) {
            item.setCondition(request.getCondition());
        }

        log.info("Wanted post item updated: {} on post: {} by user: {}", itemId, postId, uid);

        return toWantedPostItemResponse(item);
    }

    @Transactional
    public void delete(UUID uid, UUID postId, UUID itemId) {
        WantedPostItem item = getOwnedItemOrThrow(uid, postId, itemId);
        wantedPostItemRepository.delete(item);
        log.info("Wanted post item deleted: {} on post: {} by user: {}", itemId, postId, uid);
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

    private WantedPostItem getOwnedItemOrThrow(UUID uid, UUID postId, UUID itemId) {
        WantedPostItem item = wantedPostItemRepository
                .findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Wanted post item not found"));

        if (!item.getWantedPost().getId().equals(postId)) {
            throw new IllegalArgumentException("Item does not belong to this wanted post");
        }

        if (!item.getWantedPost().getNeighbor().getId().equals(uid)) {
            log.warn("User {} attempted to modify item {} they don't own", uid, itemId);
            throw new IllegalArgumentException("Not the owner of this wanted post");
        }

        return item;
    }

    private WantedPostItemResponse toWantedPostItemResponse(WantedPostItem item) {
        return WantedPostItemResponse.builder()
                .id(item.getId())
                .title(item.getTitle())
                .status(item.getStatus())
                .wantedPostId(item.getWantedPost().getId())
                .build();
    }
}