package fr.univ.givr.security;

import fr.univ.givr.model.account.Account;
import fr.univ.givr.model.role.Role;
import fr.univ.givr.service.account.AccountService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Service to load all information about a user.
 */
@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    /**
     * Service to manage accounts.
     */
    private final AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountService.getByEmailOrThrow(username);
        accountService.connectionAuthorizedOrThrow(account);

        String mail = account.getEmail();
        return new User(mail, account.getPassword(), getAuthorities(account));
    }

    /**
     * Load the permission of the user.
     *
     * @param account Account.
     * @return Collection of permission that have the user.
     */
    private Collection<GrantedAuthority> getAuthorities(@NonNull Account account) {
        Set<Role> roles = account.getRoles();

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(createGrantedAuthorityFromRole(Role.USER));

        for (Role role : roles) {
            authorities.add(createGrantedAuthorityFromRole(role.getPermission()));
        }

        return authorities;
    }

    /**
     * Create a new granted authority with role.
     *
     * @param role Role permission.
     * @return A new instance of {@link GrantedAuthority}.
     */
    private GrantedAuthority createGrantedAuthorityFromRole(@NonNull String role) {
        return new SimpleGrantedAuthority(role);
    }
}
