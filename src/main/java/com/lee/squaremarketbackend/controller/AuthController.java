package com.lee.squaremarketbackend.controller;

import com.lee.squaremarketbackend.dto.request.*;
import com.lee.squaremarketbackend.dto.response.LoginResponse;
import com.lee.squaremarketbackend.dto.response.NeighborResponse;
import com.lee.squaremarketbackend.dto.response.RegisterResponse;
import com.lee.squaremarketbackend.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(authService.register(request, response));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(authService.login(request, response));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(Authentication auth, HttpServletResponse response) {
        UUID uid = UUID.fromString(auth.getName());
        authService.logout(uid, response);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<NeighborResponse> getMe(Authentication auth) {

        UUID uid = UUID.fromString(auth.getName());

        return ResponseEntity.ok(authService.getMe(uid));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMe(Authentication auth, HttpServletResponse response) {
        UUID uid = UUID.fromString(auth.getName());
        authService.deleteMe(uid, response);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/me/username")
    public ResponseEntity<NeighborResponse> updateUsername(
            @Valid @RequestBody NeighborUsernamePatchRequest request,
            Authentication auth
    ) {
        UUID uid = UUID.fromString(auth.getName());
        return ResponseEntity.ok(authService.updateUsername(uid, request));
    }

    @PatchMapping("/me/name")
    public ResponseEntity<NeighborResponse> updateName(
            @Valid @RequestBody NeighborNamePatchRequest request,
            Authentication auth
    ) {
        UUID uid = UUID.fromString(auth.getName());
        return ResponseEntity.ok(authService.updateName(uid, request));
    }

    @PatchMapping("/me/location")
    public ResponseEntity<NeighborResponse> updateLocation(
            @Valid @RequestBody NeighborLocationPatchRequest request,
            Authentication auth
    ) {
        UUID uid = UUID.fromString(auth.getName());
        return ResponseEntity.ok(authService.updateLocation(uid, request));
    }

    @PatchMapping("/me/email")
    public ResponseEntity<NeighborResponse> updateEmail(
            @Valid @RequestBody NeighborEmailPatchRequest request,
            Authentication auth
    ) {
        UUID uid = UUID.fromString(auth.getName());
        return ResponseEntity.ok(authService.updateEmail(uid, request));
    }
}