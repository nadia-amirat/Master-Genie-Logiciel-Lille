package fr.univ.givr.security;

import fr.univ.givr.AbstractUserIT;
import fr.univ.givr.model.account.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class JWTRequestFilterIT extends AbstractUserIT {

    @Autowired
    private PasswordEncoder encoder;

    @BeforeEach
    public void onBefore() {
        accountRepository.deleteAll();
    }

    @Test
    public void unauthorizedWhenIsBadTokenInHeaderTest() {
        getWebTestClient().get()
                .uri("/")
                .headers(getHeaderAuthToken("test"))
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void unauthorizedWhenIsBadTokenInCookieTest() {
        getWebTestClient().get()
                .uri("/")
                .cookie(JWTRequestFilter.COOKIE_NAME, "test")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void unauthorizedIfTheAccountIsBannedWithAuthByCookieTest() {
        getWebTestClient().get()
                .uri("/")
                .cookie(JWTRequestFilter.COOKIE_NAME, createBannedUserAndGetToken())
                .exchange()
                .expectStatus()
                .isOk()
                .expectCookie()
                .valueEquals(JWTRequestFilter.COOKIE_NAME, "invalid");
    }

    @Test
    public void unauthorizedIfTheAccountIsBannedWithAuthByHeaderTest() {
        getWebTestClient().get()
                .uri("/")
                .headers(getHeaderAuthToken(createBannedUserAndGetToken()))
                .exchange()
                .expectStatus()
                .isOk()
                .expectCookie()
                .valueEquals(JWTRequestFilter.COOKIE_NAME, "invalid");
    }

    @Test
    public void unauthorizedIfAccountUnverifiedTest() {
        Account account = createAndSaveAccount(false);
        getWebTestClient().get()
                .uri("/")
                .headers(getHeaderAuthToken(createToken(account)))
                .exchange()
                .expectStatus()
                .isOk()
                .expectCookie()
                .valueEquals(JWTRequestFilter.COOKIE_NAME, "invalid");
    }

    @Test
    public void authorizedWhenIsBadAuthorizationHeaderTest() {
        getWebTestClient().get()
                .uri("/")
                .header(JWTRequestFilter.HEADER_AUTHORIZATION, "test")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void canAccessToResourceWithValidTokenWithPermissionTest() {
        String token = createUserAndToken();
        getWebTestClient().get()
                .uri("/")
                .headers(getHeaderAuthToken(token))
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void canAccessToResourceWithVerifiedAccountTest() {
        Account account = createAndSaveAccount(true);
        getWebTestClient().get()
                .uri("/")
                .headers(getHeaderAuthToken(createToken(account)))
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void canAccessToResourceWithValidTokenInCookieWithPermissionTest() {
        String token = createUserAndToken();
        getWebTestClient().get()
                .uri("/")
                .cookie(JWTRequestFilter.COOKIE_NAME, token)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void canAccessToResourceWithoutTokenTest() {
        getWebTestClient().get()
                .uri("/")
                .exchange()
                .expectStatus()
                .isOk();
    }


    private String createUserAndToken() {
        Account account = createValidVerifiedAccount();
        accountService.createAccount(account);
        return createToken(account);
    }

    private String createBannedUserAndGetToken() {
        Account account = createValidVerifiedAccount();
        account.setBanned(true);
        accountService.createAccount(account);
        return createToken(account);
    }
}
