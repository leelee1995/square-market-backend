package com.lee.squaremarketbackend.controller;

import com.lee.squaremarketbackend.dto.request.WantedPostItemPatchRequest;
import com.lee.squaremarketbackend.dto.request.WantedPostItemRequest;
import com.lee.squaremarketbackend.dto.response.WantedPostItemResponse;
import com.lee.squaremarketbackend.service.WantedPostItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/wanted-posts/{postId}/items")
@RequiredArgsConstructor
public class WantedPostItemController {

    private final WantedPostItemService wantedPostItemService;

    @PostMapping
    public ResponseEntity<WantedPostItemResponse> create(
            @PathVariable UUID postId,
            @Valid @RequestBody WantedPostItemRequest request,
            Authentication auth
    ) {
        UUID uid = UUID.fromString(auth.getName());
        return ResponseEntity.ok(wantedPostItemService.create(uid, postId, request));
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<WantedPostItemResponse> update(
            @PathVariable UUID postId,
            @PathVariable UUID itemId,
            @Valid @RequestBody WantedPostItemPatchRequest request,
            Authentication auth
    ) {
        UUID uid = UUID.fromString(auth.getName());
        return ResponseEntity.ok(wantedPostItemService.update(uid, postId, itemId, request));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID postId,
            @PathVariable UUID itemId,
            Authentication auth
    ) {
        UUID uid = UUID.fromString(auth.getName());
        wantedPostItemService.delete(uid, postId, itemId);
        return ResponseEntity.noContent().build();
    }
}