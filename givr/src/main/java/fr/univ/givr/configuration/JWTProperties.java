package fr.univ.givr.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

/**
 * Properties about the JWT token.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@ConfigurationProperties("jwt")
@Validated
public class JWTProperties {

    /**
     * Secret to generate JWT token.
     */
    @Size(min = 1)
    @NotNull
    private String secret;

    /**
     * Validity of the token in millisecond, cannot be less or equals than 0.
     */
    @Positive
    private long validity = 604800000;

}
