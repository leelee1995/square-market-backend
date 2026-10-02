// service/AuthService.java
package com.lee.squaremarketbackend.service;

import com.lee.squaremarketbackend.dto.request.*;
import com.lee.squaremarketbackend.dto.response.LoginResponse;
import com.lee.squaremarketbackend.dto.response.NeighborResponse;
import com.lee.squaremarketbackend.dto.response.RegisterResponse;
import com.lee.squaremarketbackend.entity.Neighbor;
import com.lee.squaremarketbackend.repository.NeighborRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final NeighborRepository neighborRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Value("${cookie.access-token-name}")
    private String cookieName;

    @Value("${cookie.secure}")
    private boolean cookieSecure;

    @Value("${cookie.same-site}")
    private String cookieSameSite;

    @Value("${jwt.expiration-ms}")
    private long jwtExpirationMs;

    @Transactional
    public RegisterResponse register(RegisterRequest request, HttpServletResponse response) {

        log.info("Registering attempt for new user: {}", request);

        if (neighborRepository.existsByUsername(request.getUsername())) {
            log.warn("Register failed: username already in use: {}", request.getUsername());
            throw new IllegalArgumentException("Username already in use");
        }

        if (neighborRepository.existsByEmail(request.getEmail())) {
            log.warn("Register failed: email already in use: {}", request.getEmail());
            throw new IllegalArgumentException("Email already in use");
        }

        Neighbor neighbor = Neighbor.builder()
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .country(request.getCountry())
                .administrativeDivision(request.getAdministrativeDivision())
                .municipality(request.getMunicipality())
                .build();

        neighborRepository.save(neighbor);

        log.info("Register successful for new user: {}", neighbor.getId());

        issueCookie(neighbor.getId(), response);

        return RegisterResponse.builder()
                .neighbor(toNeighborResponse(neighbor))
                .build();
    }

    public LoginResponse login(LoginRequest request, HttpServletResponse response) {

        Neighbor neighbor = neighborRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> {
                    log.warn("Login attempt failed for username: {}", request.getUsername());
                    return new IllegalArgumentException("Invalid credentials");
                });

        if (!passwordEncoder.matches(request.getPassword(), neighbor.getPassword())) {
            log.warn("Login attempt failed for password: {}", request.getPassword());
            throw new IllegalArgumentException("Invalid credentials");
        }

        log.info("Login successfully: {}", neighbor.getId());

        issueCookie(neighbor.getId(), response);

        return LoginResponse.builder()
                .neighbor(toNeighborResponse(neighbor))
                .build();
    }

    public NeighborResponse getMe(UUID uid) {
        Neighbor neighbor = getNeighborOrThrow(uid);
        log.info("Authenticated user successfully: {}", uid);
        return toNeighborResponse(neighbor);
    }

    @Transactional
    public void deleteMe(UUID uid, HttpServletResponse response) {
        Neighbor neighbor = getNeighborOrThrow(uid);

        neighborRepository.delete(neighbor); // cascades -> Listing/WantedPost @SQLDelete -> soft delete

        clearCookie(response);

        log.info("Account soft-deleted: {}", uid);
    }

    public void logout(UUID uid, HttpServletResponse response) {
        clearCookie(response);
        log.info("Logged out: {}", uid);
    }

    private void clearCookie(HttpServletResponse response) {
        ResponseCookie expired = ResponseCookie.from(cookieName, "")
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite(cookieSameSite)
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader("Set-Cookie", expired.toString());
    }

    @Transactional
    public NeighborResponse updateName(UUID uid, NeighborNamePatchRequest request) {
        Neighbor neighbor = getNeighborOrThrow(uid);
        neighbor.setFirstName(request.getFirstName());
        neighbor.setLastName(request.getLastName());
        log.info("Name updated for user: {}", uid);
        return toNeighborResponse(neighbor);
    }

    @Transactional
    public NeighborResponse updateUsername(UUID uid, NeighborUsernamePatchRequest request) {
        Neighbor neighbor = getNeighborOrThrow(uid);

        if (!neighbor.getUsername().equals(request.getUsername())
                && neighborRepository.existsByUsername(request.getUsername())) {
            log.warn("Username update failed: already in use: {}", request.getUsername());
            throw new IllegalArgumentException("Username already in use");
        }

        neighbor.setUsername(request.getUsername());
        log.info("Username updated for user: {}", uid);
        return toNeighborResponse(neighbor);
    }

    @Transactional
    public NeighborResponse updateLocation(UUID uid, NeighborLocationPatchRequest request) {
        Neighbor neighbor = getNeighborOrThrow(uid);
        neighbor.setCountry(request.getCountry());
        neighbor.setAdministrativeDivision(request.getAdministrativeDivision());
        neighbor.setMunicipality(request.getMunicipality());
        log.info("Location updated for user: {}", uid);
        return toNeighborResponse(neighbor);
    }

    @Transactional
    public NeighborResponse updateEmail(UUID uid, NeighborEmailPatchRequest request) {
        Neighbor neighbor = getNeighborOrThrow(uid);

        if (!neighbor.getEmail().equals(request.getEmail())
                && neighborRepository.existsByEmail(request.getEmail())) {
            log.warn("Email update failed: already in use: {}", request.getEmail());
            throw new IllegalArgumentException("Email already in use");
        }

        neighbor.setEmail(request.getEmail());
        log.info("Email updated for user: {}", uid);
        return toNeighborResponse(neighbor);
    }

    public Neighbor getNeighborOrThrow(UUID uid) {
        return neighborRepository
                .findById(uid)
                .orElseThrow(() -> {
                    log.warn("Authenticated user no longer exist: {}", uid);
                    return new IllegalArgumentException("Authenticated user error");
                });
    }

    private void issueCookie(UUID neighborId, HttpServletResponse response) {
        String token = jwtService.generateToken(neighborId);

        ResponseCookie cookie = ResponseCookie.from(cookieName, token)
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite(cookieSameSite)
                .path("/")
                .maxAge(jwtExpirationMs / 1000)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    private NeighborResponse toNeighborResponse(Neighbor neighbor) {
        return NeighborResponse.builder()
                .id(neighbor.getId())
                .username(neighbor.getUsername())
                .firstName(neighbor.getFirstName())
                .lastName(neighbor.getLastName())
                .email(neighbor.getEmail())
                .country(neighbor.getCountry())
                .administrativeDivision(neighbor.getAdministrativeDivision())
                .municipality(neighbor.getMunicipality())
                .createdAt(neighbor.getCreatedAt())
                .build();
    }
}