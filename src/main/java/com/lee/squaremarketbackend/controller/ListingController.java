package com.lee.squaremarketbackend.controller;

import com.lee.squaremarketbackend.dto.request.ListingRequest;
import com.lee.squaremarketbackend.dto.response.ListingResponse;
import com.lee.squaremarketbackend.entity.ListingCategory;
import com.lee.squaremarketbackend.entity.ListingStatus;
import com.lee.squaremarketbackend.service.ListingService;
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
@RequestMapping("/api/listings")
@RequiredArgsConstructor
public class ListingController {

    private final ListingService listingService;

    @GetMapping("/mine")
    public ResponseEntity<List<ListingResponse>> getMine(Authentication auth) {
        UUID uid = UUID.fromString(auth.getName());
        return ResponseEntity.ok(listingService.getMine(uid));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListingResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(listingService.getById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<ListingResponse>> getAll(
            @RequestParam(required = false)ListingCategory category,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(listingService.getAll(category, pageable));
    }

    @PostMapping("/create")
    public ResponseEntity<ListingResponse> createListing(
            @Valid
            @RequestBody
            ListingRequest req,
            Authentication auth) {

        UUID uid = UUID.fromString(auth.getName());

        return ResponseEntity.ok(listingService.create(uid, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id, Authentication auth) {
        UUID uid = UUID.fromString(auth.getName());
        listingService.delete(uid, id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ListingResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody ListingRequest request,
            Authentication auth
    ) {
        UUID uid = UUID.fromString(auth.getName());
        return ResponseEntity.ok(listingService.update(uid, id, request));
    }
}