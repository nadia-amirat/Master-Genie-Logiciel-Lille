package fr.univ.givr.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception if an account is already not banned.
 */
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Le compte n'est pas banni")
public class AccountNotBannedException extends RuntimeException {
}
