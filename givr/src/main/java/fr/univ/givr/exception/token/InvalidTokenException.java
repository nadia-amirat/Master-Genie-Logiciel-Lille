package fr.univ.givr.exception.token;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception if a token is not valid.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Le jeton n'est pas valide")
public class InvalidTokenException extends RuntimeException {
}
