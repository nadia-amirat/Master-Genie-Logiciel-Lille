package fr.univ.givr.exception.donations;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception if an account is already banned.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Donation inconnue")
public class DonationNotFoundException extends RuntimeException {
}
