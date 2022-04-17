package fr.univ.givr.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PasswordUtils {

    public String generateValidPassword() {
        return "1Ae" + UUID.randomUUID();
    }
}
