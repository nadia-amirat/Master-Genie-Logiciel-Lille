package fr.univ.givr.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception if the authentication is impossible.
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Le compte n'est pas valide")
public class UnableAuthenticateException extends RuntimeException {
}
