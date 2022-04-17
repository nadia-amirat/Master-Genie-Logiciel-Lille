package fr.univ.givr.service;

import fr.univ.givr.exception.user.UnableAuthenticateException;
import fr.univ.givr.model.account.Account;
import fr.univ.givr.model.account.AuthenticateDto;
import fr.univ.givr.security.JWTRequestFilter;
import fr.univ.givr.security.Token;
import fr.univ.givr.security.TokenManager;
import fr.univ.givr.service.account.AccountService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Service to manage the authentication of customer.
 */
@Slf4j
@Service
public class AuthenticateService {

    /**
     * Service to manage accounts.
     */
    private final AccountService accountService;

    /**
     * Token manager to create a token.
     */
    private final TokenManager tokenManager;

    /**
     * Encoder to encrypt the password of the account.
     */
    private final PasswordEncoder encoder;

    public AuthenticateService(
            AccountService accountService,
            @Qualifier("JWTTokenManager") TokenManager tokenManager,
            PasswordEncoder encoder
    ) {
        this.accountService = accountService;
        this.tokenManager = tokenManager;
        this.encoder = encoder;
    }

    /**
     * Create an authentication token for the associated account.
     *
     * @param auth Account to create the token.
     * @return The token if the account is valid, or an error code otherwise.
     */
    public Token createToken(@NonNull AuthenticateDto auth) {
        String email = auth.getEmail();
        Account account = accountService.getByEmailOrNull(email);

        if (account == null || !encoder.matches(auth.getPassword(), account.getPassword())) {
            throw new UnableAuthenticateException();
        }

        accountService.connectionAuthorizedOrThrow(account);

        return tokenManager.createToken(email);
    }

    /**
     * Create the cookie corresponding to the token value with the max age.
     *
     * @param token Token.
     * @return A new cookie for the entire application.
     */
    public Cookie createCookieForToken(@NonNull Token token) {
        Cookie cookie = new Cookie(JWTRequestFilter.COOKIE_NAME, token.getToken());
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) token.getExpireAt());
        return cookie;
    }

    /**
     * Remove the auth token from the cookies.
     *
     * @param response Response to change the cookie.
     */
    public void removeCookieAuth(@NonNull HttpServletResponse response) {
        Cookie cookie = new Cookie(JWTRequestFilter.COOKIE_NAME, "invalid");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
