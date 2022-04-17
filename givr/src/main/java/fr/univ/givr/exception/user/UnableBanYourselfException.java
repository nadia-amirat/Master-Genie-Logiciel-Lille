package fr.univ.givr.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception if a try is executed to ban yourself.
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Impossible de se bannir soi-même")
public class UnableBanYourselfException extends RuntimeException{
}
