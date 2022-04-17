package fr.univ.givr.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception if an account already exists.
 */
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "L'adresse mail existe déjà")
public class MailExistsException extends RuntimeException {
}
