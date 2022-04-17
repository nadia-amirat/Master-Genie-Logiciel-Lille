package fr.univ.givr.security;

import fr.univ.givr.configuration.JWTProperties;
import fr.univ.givr.utils.JWTUtils;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Manager to write and read jwt token.
 */
@Slf4j
@AllArgsConstructor
@Component
public class JWTTokenManager implements TokenManager {

    /**
     * Properties to generate jwt token.
     */
    private final JWTProperties jwtProperties;

    /**
     * Utils class about Token manipulation.
     */
    private final JWTUtils jwtUtils;

    @NonNull
    @Override
    public Token createToken(@NonNull String username) {
        return jwtUtils.createToken(
                username,
                jwtProperties.getValidity(),
                jwtProperties.getSecret(),
                SignatureAlgorithm.HS384
        );
    }

    @Override
    public String getContentFromTokenOrNull(@NonNull String token) {
        return jwtUtils.getContentOrNull(token, jwtProperties.getSecret());
    }
}
