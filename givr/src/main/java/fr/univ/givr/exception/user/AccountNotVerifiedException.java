package fr.univ.givr.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception if the account is not verified.
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Le compte n'est pas vérifié")
public class AccountNotVerifiedException extends RuntimeException {
}
