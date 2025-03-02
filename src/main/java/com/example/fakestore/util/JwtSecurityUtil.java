package com.example.fakestore.util;

import com.example.fakestore.security.service.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public final class JwtSecurityUtil {
    public static Optional<UserDetailsImpl> getJwtUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getDetails() != null) {
            Object object =
                    SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            return object instanceof UserDetailsImpl userDetails ? Optional.of(userDetails) : Optional.empty();
        } else {
            return Optional.empty();
        }
    }

    private JwtSecurityUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
