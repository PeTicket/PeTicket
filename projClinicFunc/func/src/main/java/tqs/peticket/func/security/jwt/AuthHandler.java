package tqs.peticket.func.security.jwt;

import java.io.IOException;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import tqs.peticket.func.security.services.UserDetailsImpl;
import tqs.peticket.func.repository.FuncRepository;


@Component
public final class AuthHandler {
    
    @Autowired
    private FuncRepository funcRepository;

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public String getUserEmail() {
        Authentication authentication = getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetailsImpl) {
                return ((UserDetailsImpl) principal).getEmail();
            }
        }
        return null;
    }

    public UUID getUserId() {
        Authentication authentication = getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetailsImpl) {
                return ((UserDetailsImpl) principal).getId();
            }
        }
        return null;
    }

    public boolean isFunc() {
        UUID userId = getUserId();
        if (userId != null && funcRepository.existsById(userId)) {
            return true;
        }
        return false;
    }
}