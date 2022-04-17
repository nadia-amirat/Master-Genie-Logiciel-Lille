package fr.univ.givr.exception.email;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception if a mail cannot be sent.
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Impossible d'envoyer le mail")
public class SendingMailException extends RuntimeException {
}
