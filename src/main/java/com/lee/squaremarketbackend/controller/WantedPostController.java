package com.lee.squaremarketbackend.controller;

import com.lee.squaremarketbackend.dto.request.WantedPostRequest;
import com.lee.squaremarketbackend.dto.response.WantedPostResponse;
import com.lee.squaremarketbackend.service.WantedPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/wanted-posts")
@RequiredArgsConstructor
public class WantedPostController {

    private final WantedPostService wantedPostService;

    @GetMapping("/mine")
    public ResponseEntity<List<WantedPostResponse>> getMine(Authentication auth) {
        UUID uid = UUID.fromString(auth.getName());
        return ResponseEntity.ok(wantedPostService.getMine(uid));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WantedPostResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(wantedPostService.getById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<WantedPostResponse>> getAll(@PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(wantedPostService.getAll(pageable));
    }

    @PostMapping("/create")
    public ResponseEntity<WantedPostResponse> create(
            @Valid @RequestBody WantedPostRequest request,
            Authentication auth
    ) {
        UUID uid = UUID.fromString(auth.getName());
        return ResponseEntity.ok(wantedPostService.create(uid, request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<WantedPostResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody WantedPostRequest request,
            Authentication auth
    ) {
        UUID uid = UUID.fromString(auth.getName());
        return ResponseEntity.ok(wantedPostService.update(uid, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id, Authentication auth) {
        UUID uid = UUID.fromString(auth.getName());
        wantedPostService.delete(uid, id);
        return ResponseEntity.noContent().build();
    }
}