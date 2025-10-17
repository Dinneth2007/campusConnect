package com.example.CampusConnectService.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.lang.NonNull;

import java.util.Optional;

@Component("auditorAwareImpl")
@Slf4j
public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public @NonNull Optional<String> getCurrentAuditor() {

        //Once you implement authentication, fetch the logged in user details from security context and return the username
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                String user = authentication.getName();
                log.debug("AuditorAware called, returning user: {}", user);
                return Optional.ofNullable(user);
            }
        } catch (Exception ex) {
            log.warn("Error resolving current auditor", ex);
        }
        log.debug("AuditorAware called, no authenticated user found - returning 'system'");
        return Optional.of("system");
    }
}
