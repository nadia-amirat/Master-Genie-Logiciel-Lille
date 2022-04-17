package fr.univ.givr.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception if the account is not found
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Le compte demandé n'a pas été trouvé")
public class AccountNotFoundException extends RuntimeException {
}
