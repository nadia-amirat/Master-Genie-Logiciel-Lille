package fr.univ.givr.utils;

import lombok.NonNull;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class RequestUtils {

    /**
     * Know if the user has a specific permission.
     * @param user User information
     * @param role Role researched.
     * @return {@code true} if the user has the role, {@code false} otherwise.
     */
    public boolean hasPermission(@NonNull User user, @NonNull String role) {
        return user.getAuthorities().stream().anyMatch(perm -> perm.getAuthority().equals(role));
    }
}
