package fr.univ.givr.controller;

import fr.univ.givr.AbstractAuthenticateIT;
import fr.univ.givr.model.account.Account;
import fr.univ.givr.model.account.AuthenticateDto;
import fr.univ.givr.security.Token;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

class AuthenticateRestControllerIT extends AbstractAuthenticateIT {

    @Override
    protected void assertGetTokenFromValidAuth(Account account, AuthenticateDto authenticateDto) {
        FluxExchangeResult<Token> result = sendRequestToGetToken(authenticateDto)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Token.class);

        verifyByPredicate(result.getResponseBody().single(), token -> {
            verifyContentToken(account, token);
            return true;
        });
    }

    @Override
    protected void assertGetTokenFromInvalidAuth(AuthenticateDto authenticateDto) {
        sendRequestToGetToken(authenticateDto)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Override
    protected void assertGetTokenFromNonExistantAuth(AuthenticateDto authenticateDto) {
        sendRequestToGetToken(authenticateDto)
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }

    private WebTestClient.RequestHeadersSpec<?> sendRequestToGetToken(AuthenticateDto value) {
        return getWebTestClient()
                .post()
                .uri("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(value), AuthenticateDto.class);
    }
}
