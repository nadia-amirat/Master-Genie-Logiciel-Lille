package fr.univ.givr.utils;

import fr.univ.givr.security.Token;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

/**
 * Utils class to manage token using jwt.
 */
@Component
public class JWTUtils {

    /**
     * Create a new token.
     *
     * @param content   Subject of the token.
     * @param validity  Time while the token is valid after now.
     * @param secret    Secret key to encrypt the token.
     * @param algorithm Algorithm to encrypt the token.
     * @return The token with his useful data.
     */
    public Token createToken(
            @NonNull String content,
            long validity,
            @NonNull String secret,
            @NonNull SignatureAlgorithm algorithm
    ) {
        Instant nowInstant = Instant.now();
        Date nowDate = Date.from(nowInstant);

        Instant expireAtInstant = nowInstant.plusMillis(validity);
        Date expireAtDate = Date.from(expireAtInstant);

        String token = Jwts.builder()
                .setSubject(content)
                .setIssuedAt(nowDate)
                .setExpiration(expireAtDate)
                .signWith(algorithm, secret)
                .compact();

        return new Token(token, expireAtInstant.toEpochMilli());
    }

    /**
     * Retrieve the subject of a token from the secret.
     *
     * @param token  Token generated with the same secret.
     * @param secret Secret key used to encrypt the token.
     * @return The subject of the token if the token is valid, or {@code null} otherwise.
     */
    public String getContentOrNull(@NonNull String token, @NonNull String secret) {
        try {
            return getContentOrThrow(token, secret);
        } catch (Exception ignored) {
            return null;
        }
    }

    /**
     * Retrieve the subject of a token from the secret or throw an exception if the token is invalid.
     *
     * @param token  Token generated with the same secret.
     * @param secret Secret key used to encrypt the token.
     * @return The subject of the token if the token is valid.
     */
    public String getContentOrThrow(@NonNull String token, @NonNull String secret) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }
}
