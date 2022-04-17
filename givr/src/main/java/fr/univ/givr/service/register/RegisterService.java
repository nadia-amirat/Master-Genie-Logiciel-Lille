package fr.univ.givr.service.register;

import fr.univ.givr.configuration.RegisterConfiguration;
import fr.univ.givr.exception.email.SendingMailException;
import fr.univ.givr.exception.token.InvalidTokenException;
import fr.univ.givr.exception.user.AccountAlreadyVerifiedException;
import fr.univ.givr.model.account.Account;
import fr.univ.givr.security.Token;
import fr.univ.givr.security.TokenManager;
import fr.univ.givr.service.EmailService;
import fr.univ.givr.service.account.AccountService;
import fr.univ.givr.utils.URIUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

/**
 * Service to interact with pending mails.
 */
@Slf4j
@Service
public class RegisterService {

    /**
     * Service to interact with account data.
     */
    private final AccountService accountService;

    /**
     * Service to send email.
     */
    private final EmailService emailService;

    /**
     * Configuration for register component.
     */
    private final RegisterConfiguration configuration;

    /**
     * Token manager to generate token register.
     */
    private final TokenManager tokenManager;

    public RegisterService(
            AccountService accountService,
            EmailService emailService,
            RegisterConfiguration configuration,
            @Qualifier("registerTokenManager") TokenManager tokenManager
    ) {
        this.accountService = accountService;
        this.emailService = emailService;
        this.configuration = configuration;
        this.tokenManager = tokenManager;
    }

    /**
     * Send the email message to valid the account.
     *
     * @param email Target email where the email message will be sent.
     */
    public void sendEmailConfirmation(@NonNull HttpHeaders headers, @NonNull String email) {
        Account account = accountService.getByEmailOrNull(email);
        if (account == null || account.isVerified()) {
            return;
        }

        log.debug("Starting new thread to send confirmation email [{}]", email);
        new Thread(() -> {
            String uriValidate = buildURIValidation(headers, email);

            try {
                log.debug("Sending mail to verify the account with email [{}]", email);
                sendEmail(email, uriValidate);
            } catch (Exception ex) {
                log.error("Unable to send confirmation email to [{}]", email, ex);
                throw new SendingMailException();
            }
        }).start();

    }

    /**
     * Send the email confirmation with a link to interact with the endpoint to confirm the account.
     *
     * @param email       User's email.
     * @param uriValidate URI link to valid the account.
     * @throws MessagingException If the mail cannot be sent.
     */
    private void sendEmail(@NonNull String email, @NonNull String uriValidate) throws MessagingException {
        RegisterConfiguration.MailConfiguration mail = configuration.getMail();
        emailService.sendHtmlMail(
                email,
                mail.getSubject(),
                createBodyWithURI(uriValidate, mail)
        );
    }

    /**
     * Create a mail body with the link to valid the account inserted in it.
     *
     * @param uriValidate URI link to valid the account.
     * @param mail        Configuration of mail from the application.
     * @return The body of mail.
     */
    private String createBodyWithURI(
            @NonNull String uriValidate,
            @NonNull RegisterConfiguration.MailConfiguration mail
    ) {
        return mail.getContent().replace(mail.getTagLink(), uriValidate);
    }

    /**
     * Build the URI to valid the user account.
     *
     * @param headers Headers to find the current address of the server.
     * @param email   User's email.
     * @return The personal URI.
     */
    private String buildURIValidation(@NonNull HttpHeaders headers, @NonNull String email) {
        Token token = tokenManager.createToken(email);
        return URIUtils.getRootURI(headers)
                .path("/mail/confirm")
                .queryParam("id", token.getToken())
                .build()
                .toString();
    }

    /**
     * Extract the subject from the token and valid the account associated to it.
     *
     * @param token Authenticate token.
     * @throws InvalidTokenException                                If the token is invalid
     * @throws fr.univ.givr.exception.user.AccountNotFoundException If no account is link to the subject extracted from the topic.
     * @throws AccountAlreadyVerifiedException                      If the account is already verified.
     */
    public void confirmAccountByToken(@NonNull String token) {
        String email = tokenManager.getContentFromTokenOrNull(token);
        if (email == null) {
            throw new InvalidTokenException();
        }

        Account account = accountService.getByEmailOrThrow(email);
        if (account.isVerified()) {
            throw new AccountAlreadyVerifiedException();
        }

        accountService.setVerified(email, true);
        log.debug("The account with email [{}] is now verified", email);
    }

}
