package fr.univ.givr.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception if an account is already verified
 */
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Le compte est déjà vérifié")
public class AccountAlreadyVerifiedException extends RuntimeException {
}
