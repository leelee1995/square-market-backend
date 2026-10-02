package com.lee.squaremarketbackend.service;

import com.lee.squaremarketbackend.dto.request.ListingRequest;
import com.lee.squaremarketbackend.dto.response.ListingResponse;
import com.lee.squaremarketbackend.entity.Listing;
import com.lee.squaremarketbackend.entity.ListingCategory;
import com.lee.squaremarketbackend.entity.ListingStatus;
import com.lee.squaremarketbackend.entity.Neighbor;
import com.lee.squaremarketbackend.repository.ListingRepository;
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
public class ListingService {

    private final ListingRepository listingRepository;
    private final AuthService authService;

    @Transactional(readOnly = true)
    public ListingResponse getById(UUID id) {
        Listing listing = listingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Listing not "
                + "found"));
        return toListingResponse(listing);
    }

    @Transactional(readOnly = true)
    public Page<ListingResponse> getAll(ListingCategory category, Pageable pageable) {
        Page<Listing> page = category != null ? listingRepository.findByCategory(category, pageable) :
                listingRepository.findAll(pageable);
        return page.map(this::toListingResponse);
    }

    @Transactional(readOnly = true)
    public List<ListingResponse> getMine(UUID uid) {
        return listingRepository.findByNeighborId(uid).stream().map(this::toListingResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ListingResponse create(UUID uid, ListingRequest req) {
        Neighbor neighbor = authService.getNeighborOrThrow(uid);

        Listing listing =
                Listing.builder()
                        .title(req.getTitle())
                        .details(req.getDetails())
                        .price(req.getPrice())
                        .images(req.getImages())
                        .status(req.getStatus())
                        .condition(req.getCondition())
                        .category(req.getCategory())
                        .municipality(req.getMunicipality())
                        .administrativeDivision(req.getAdministrativeDivision())
                        .country(req.getCountry()).neighbor(neighbor).build();

        listingRepository.save(listing);

        log.info("Listing created: {} by user: {}", listing.getId(), uid);

        return toListingResponse(listing);
    }

    @Transactional
    public ListingResponse update(UUID uid, UUID listingId, ListingRequest request) {

        Listing listing = getOwnedListingOrThrow(uid, listingId);

        listing.setTitle(request.getTitle());
        listing.setDetails(request.getDetails());
        listing.setPrice(request.getPrice());

        if (request.getImages() != null) {
            listing.setImages(request.getImages());
        }
        if (request.getStatus() != null) {
            listing.setStatus(request.getStatus());
        }
        if (request.getMunicipality() != null) {
            listing.setMunicipality(request.getMunicipality());
        }
        if (request.getAdministrativeDivision() != null) {
            listing.setAdministrativeDivision(request.getAdministrativeDivision());
        }
        if (request.getCountry() != null) {
            listing.setCountry(request.getCountry());
        }

        log.info("Listing updated: {} by user: {}", listingId, uid);

        return toListingResponse(listing);
    }

    @Transactional
    public void delete(UUID uid, UUID listingId) {
        Listing listing = getOwnedListingOrThrow(uid, listingId);
        listingRepository.delete(listing); // intercepted by @SQLDelete -> soft delete
        log.info("Listing soft-deleted: {} by user: {}", listingId, uid);
    }

    private Listing getOwnedListingOrThrow(UUID uid, UUID listingId) {
        Listing listing = listingRepository.findById(listingId).orElseThrow(() -> new IllegalArgumentException(
                "Listing not found"));

        if (!listing.getNeighbor().getId().equals(uid)) {
            log.warn("User {} attempted to modify listing {} they don't own", uid, listingId);
            throw new IllegalArgumentException("Not the owner of this listing");
        }

        return listing;
    }

    private ListingResponse toListingResponse(Listing listing) {
        return ListingResponse.builder().id(listing.getId()).title(listing.getTitle()).details(listing.getDetails())
                .price(listing.getPrice()).images(listing.getImages()).status(listing.getStatus())
                .condition(listing.getCondition()).municipality(listing.getMunicipality())
                .administrativeDivision(listing.getAdministrativeDivision()).country(listing.getCountry())
                .neighborId(listing.getNeighbor().getId()).neighborUsername(listing.getNeighbor().getUsername())
                .createdAt(listing.getCreatedAt()).updatedAt(listing.getUpdatedAt()).build();
    }
}