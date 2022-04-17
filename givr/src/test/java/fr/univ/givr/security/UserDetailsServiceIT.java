package fr.univ.givr.security;

import fr.univ.givr.AbstractIT;
import fr.univ.givr.exception.user.AccountNotFoundException;
import fr.univ.givr.model.account.Account;
import fr.univ.givr.model.role.Role;
import fr.univ.givr.repository.AccountRepository;
import fr.univ.givr.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserDetailsServiceIT extends AbstractIT {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    public void onBefore() {
        accountRepository.deleteAll();
    }

    @Test
    void loadUserDetailWithUsernameNotPresentTest() {
        assertThrows(AccountNotFoundException.class, () -> userDetailsService.loadUserByUsername("test"));
    }

    @Test
    void loadUserDetailWithUsernamePresentWithoutAuthoritiesTest() {
        Account account = createValidVerifiedAccount();
        accountRepository.save(account);
        assertUserDetailsEqualsToAccountContent(
                account,
                new SimpleGrantedAuthority(Role.USER)
        );
    }

    @Test
    void loadUserDetailWithUsernamePresentWithAuthoritiesTest() {
        Account account = createValidVerifiedAccount();
        Role role = Role.builder()
                .permission("MOD")
                .build();
        account.setRoles(Set.of(role));
        accountRepository.save(account);

        assertUserDetailsEqualsToAccountContent(
                account,
                new SimpleGrantedAuthority(Role.USER),
                new SimpleGrantedAuthority(role.getPermission())
        );
    }

    @Test
    void loadUserDetailWithUsernamePresentWithDuplicateAuthoritiesTest() {
        Account account = createValidVerifiedAccount();
        account.setRoles(Set.of(Role.builder()
                                        .permission(Role.USER)
                                        .build()
        ));
        account = accountRepository.save(account);
        assertUserDetailsEqualsToAccountContent(
                account,
                new SimpleGrantedAuthority(Role.USER)
        );
    }

    @Test
    void loadUserDetailWithUsernamePresentVerifiedTest() {
        Account account = createValidVerifiedAccount();
        account.setVerified(true);
        accountRepository.save(account);
        assertUserDetailsEqualsToAccountContent(
                account,
                new SimpleGrantedAuthority(Role.USER)
        );
    }

    private void assertUserDetailsEqualsToAccountContent(
            Account account,
            SimpleGrantedAuthority... ExpectedAuthorities
    ) {
        UserDetails details = userDetailsService.loadUserByUsername(account.getEmail());
        assertThat(details.getUsername()).isEqualTo(account.getEmail());

        Collection<? extends GrantedAuthority> authorities = details.getAuthorities();
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(authorities);
        assertThat(grantedAuthorities).containsExactlyInAnyOrder(ExpectedAuthorities);

        assertThat(details.getPassword()).isEqualTo(account.getPassword());
        assertThat(details.isAccountNonExpired()).isTrue();
        assertThat(details.isAccountNonLocked()).isTrue();
        assertThat(details.isEnabled()).isTrue();
        assertThat(details.isCredentialsNonExpired()).isTrue();
    }

}
