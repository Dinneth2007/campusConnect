package com.example.CampusConnectService.security;

import com.example.CampusConnectService.entity.User;
import com.example.CampusConnectService.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.lang.NonNull;


import java.io.IOException;
import java.util.List;
import java.util.Collections;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        String token = null;
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            token = header.substring(7);
        }

        if (token != null) {
            try {
                if (tokenProvider.validateToken(token)) {
                    String username = tokenProvider.getUsername(token);
                    User user = userRepository.findByEmail(username).orElse(null);
                    if (user != null) {
                        // Load authorities from DB rather than parsing roles from the token
                        var roles = user.getRoles();
                        List<SimpleGrantedAuthority> authorities;
                        if (roles == null || roles.isEmpty()) {
                            authorities = Collections.emptyList();
                        } else {
                            authorities = roles.stream()
                                    .map(SimpleGrantedAuthority::new)
                                    .toList();
                        }

                        var auth = new UsernamePasswordAuthenticationToken(user.getEmail(), null, authorities);
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
            } catch (Exception ex) {
                // log and continue filter chain to allow authentication endpoints to work
                log.warn("JWT filter exception: {}", ex.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}
