package fr.univ.givr.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration to load bean for encrypt and decrypt string.
 */
@Configuration
public class EncoderConfiguration {

    /**
     * Password encoder to protect password.
     *
     * @return A new instance of [PasswordEncoder].
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
