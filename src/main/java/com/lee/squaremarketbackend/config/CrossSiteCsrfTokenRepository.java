package com.lee.squaremarketbackend.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;

/**
 * Same behavior as CookieCsrfTokenRepository.withHttpOnlyFalse(), but writes the
 * XSRF-TOKEN cookie ourselves so it always carries SameSite/Secure
 * (needed because the frontend and backend are on different sites).
 */
public class CrossSiteCsrfTokenRepository implements CsrfTokenRepository {

    private static final String COOKIE_NAME = "XSRF-TOKEN";

    private final CookieCsrfTokenRepository delegate = CookieCsrfTokenRepository.withHttpOnlyFalse();
    private final boolean secure;
    private final String sameSite;

    public CrossSiteCsrfTokenRepository(boolean secure, String sameSite) {
        this.secure = secure;
        this.sameSite = sameSite;
    }

    @Override
    public CsrfToken generateToken(HttpServletRequest request) {
        return delegate.generateToken(request);
    }

    @Override
    public void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(COOKIE_NAME, token != null ? token.getToken() : "")
                .httpOnly(false)
                .secure(secure)
                .sameSite(sameSite)
                .path("/")
                .maxAge(token != null ? -1 : 0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    @Override
    public CsrfToken loadToken(HttpServletRequest request) {
        return delegate.loadToken(request);
    }
}