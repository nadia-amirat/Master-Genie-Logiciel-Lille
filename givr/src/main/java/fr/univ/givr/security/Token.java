package fr.univ.givr.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Store information about token.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Token {

    /**
     * Token string.
     */
    @JsonProperty("token")
    private String token;

    /**
     * Date when the token will be expired.
     */
    @JsonProperty("expireAt")
    private long expireAt;
}
