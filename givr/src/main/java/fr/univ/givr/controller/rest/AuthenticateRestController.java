package fr.univ.givr.controller.rest;

import fr.univ.givr.model.StatusResource;
import fr.univ.givr.model.account.AuthenticateDto;
import fr.univ.givr.security.Token;
import fr.univ.givr.service.AuthenticateService;
import fr.univ.givr.utils.RestUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Controller to manage the authentication of customer and send the tokens.
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/authenticate")
public class AuthenticateRestController {

    /**
     * Service to manage authentication.
     */
    private final AuthenticateService service;

    private final RestUtils restUtils;

    /**
     * Create an authentication token for the associated account.
     *
     * @param auth Account to create the token.
     * @return The token if the account is valid.
     */
    @PostMapping
    public ResponseEntity<Token> createToken(@Valid @RequestBody AuthenticateDto auth, HttpServletResponse response) {
        Token token = service.createToken(auth);
        response.addCookie(service.createCookieForToken(token));
        return ResponseEntity.ok(token);
    }

    /**
     * Remove the auth cookie through the http response.
     * @param response Http response.
     */
    @PostMapping("/disconnect")
    public ResponseEntity<StatusResource> disconnect(HttpServletResponse response) {
        service.removeCookieAuth(response);
        return restUtils.statusToResponse(new StatusResource(HttpStatus.OK, "Vous êtes déconnecté"));
    }

}
