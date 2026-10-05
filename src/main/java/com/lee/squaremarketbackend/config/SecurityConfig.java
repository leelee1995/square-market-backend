package com.lee.squaremarketbackend.config;

import com.lee.squaremarketbackend.security.CookieAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CookieAuthFilter cookieAuthFilter;

    @Value("${cookie.secure}")
    private boolean cookieSecure;

    @Value("${cookie.same-site}")
    private String cookieSameSite;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName(null);

        // Frontend (Vercel) and backend (Render) are different sites, so the
        // XSRF-TOKEN cookie must be SameSite=None; Secure or the browser drops it.
        CrossSiteCsrfTokenRepository csrfRepository =
                new CrossSiteCsrfTokenRepository(cookieSecure, cookieSameSite);

        http
                .cors(cors -> {
                })
                .csrf(csrf -> csrf
                        .csrfTokenRepository(csrfRepository)
                        .csrfTokenRequestHandler(requestHandler)
                        .ignoringRequestMatchers("/api/auth/login", "/api/auth/register")
                )
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/me/**", "/api/auth/logout").authenticated()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/listings/mine").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/listings/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/listings/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/listings/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/wanted-posts/mine").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/wanted-posts/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/wanted-posts/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/wanted-posts/**").authenticated()
                        .anyRequest().permitAll()
                ).addFilterBefore(
                        cookieAuthFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}