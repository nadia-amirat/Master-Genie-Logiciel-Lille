package fr.univ.givr.security;

import lombok.NonNull;

/**
 * Allows creating and retrieving data about token.
 */
public interface TokenManager {

    /**
     * Generate a new token for the user.
     *
     * @param username Identification store in the token.
     * @return A new instance of {@link Token} with all information for the authentication.
     */
    Token createToken(@NonNull String username);

    /**
     * Find the username in the token if the token is valid and not outdated.
     *
     * @param token Token.
     * @return The username presents in the token if it is valid, `null` otherwise.
     */
    String getContentFromTokenOrNull(@NonNull String token);
}
