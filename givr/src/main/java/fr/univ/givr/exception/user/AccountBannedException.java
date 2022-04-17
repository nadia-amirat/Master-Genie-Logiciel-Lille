package fr.univ.givr.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception if an account is banned.
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Le compte est banni")
public class AccountBannedException extends RuntimeException {
}
