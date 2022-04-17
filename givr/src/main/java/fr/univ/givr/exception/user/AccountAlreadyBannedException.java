package fr.univ.givr.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception if an account is already banned.
 */
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Le compte est déjà banni")
public class AccountAlreadyBannedException extends RuntimeException {
}
