package fr.univ.givr.service.register;

import fr.univ.givr.configuration.RegisterConfiguration;
import fr.univ.givr.security.Token;
import fr.univ.givr.security.TokenManager;
import fr.univ.givr.utils.JWTUtils;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class RegisterTokenManager implements TokenManager {

    /**
     * Properties to generate token.
     */
    private final RegisterConfiguration configuration;

    /**
     * Utils class about Token manipulation.
     */
    private final JWTUtils jwtUtils;

    @NonNull
    @Override
    public Token createToken(@NonNull String username) {
        RegisterConfiguration.TokenConfiguration tokenConfiguration = configuration.getToken();
        return jwtUtils.createToken(
                username,
                tokenConfiguration.getValidity(),
                tokenConfiguration.getSecret(),
                SignatureAlgorithm.HS256
        );
    }

    @Override
    public String getContentFromTokenOrNull(@NonNull String token) {
        return jwtUtils.getContentOrNull(token, configuration.getToken().getSecret());
    }

}
