package fr.univ.givr;

import fr.univ.givr.mapper.AccountMapper;
import fr.univ.givr.model.account.Account;
import fr.univ.givr.model.role.Role;
import fr.univ.givr.repository.AccountRepository;
import fr.univ.givr.repository.RoleRepository;
import fr.univ.givr.security.TokenManager;
import fr.univ.givr.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class AbstractUserIT extends AbstractIT {

    @Autowired
    protected AccountRepository accountRepository;

    @Autowired
    protected AccountService accountService;

    @Autowired
    @Qualifier("JWTTokenManager")
    protected TokenManager securityTokenManager;

    @Autowired
    protected RoleRepository roleRepository;

    @Autowired
    protected AccountMapper accountMapper;

    protected Account createAndSaveAccount(boolean verified, String... roles) {
        Account account = createValidVerifiedAccount();
        account.setVerified(verified);
        Set<Role> rolesSet = Arrays.stream(roles).map(role -> Role.builder()
                .permission(role)
                .build()).collect(Collectors.toSet()
        );
        account.setRoles(rolesSet);
        return accountRepository.saveAndFlush(account);
    }

    protected String createAccountAndGetAuthToken() {
        return saveAccountAndGetToken(createValidVerifiedAccount());
    }

    protected String createAccountVerifiedAndGetAuthToken() {
        Account account = createValidVerifiedAccount();
        account.setVerified(true);
        return saveAccountAndGetToken(account);
    }

    protected String saveAccountAndGetToken(Account account) {
        accountRepository.save(account);
        return createToken(account);
    }

    protected String createToken(Account account) {
        return securityTokenManager.createToken(account.getEmail()).getToken();
    }

}
