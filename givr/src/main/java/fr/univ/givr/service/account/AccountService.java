package fr.univ.givr.service.account;

import fr.univ.givr.configuration.AccountModifyConfiguration;
import fr.univ.givr.exception.token.InvalidTokenException;
import fr.univ.givr.exception.user.AccountBannedException;
import fr.univ.givr.exception.user.AccountNotFoundException;
import fr.univ.givr.exception.user.AccountNotVerifiedException;
import fr.univ.givr.exception.user.MailExistsException;
import fr.univ.givr.mapper.AccountMapper;
import fr.univ.givr.model.account.Account;
import fr.univ.givr.model.account.ModifyAccountDTO;
import fr.univ.givr.model.account.UserInfo;
import fr.univ.givr.repository.AccountRepository;
import fr.univ.givr.security.TokenManager;
import fr.univ.givr.service.EmailService;
import fr.univ.givr.utils.URIUtils;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Objects;

/**
 * Service to manage account data.
 */
@Service
public class AccountService {

    /**
     * Repository to interact with the account in database.
     */
    private final AccountRepository repository;

    /**
     * Encoder to encrypt the account's password.
     */
    private final PasswordEncoder encoder;

    /**
     * Mail service to send email.
     */
    private final EmailService emailService;

    /**
     * Token manager to create token.
     */
    private final TokenManager tokenManager;

    /**
     * Configuration about account.
     */
    private final AccountModifyConfiguration configuration;

    /**
     * Mapper for account data.
     */
    private final AccountMapper mapper;

    public AccountService(
            AccountRepository repository,
            PasswordEncoder encoder,
            EmailService emailService,
            @Qualifier("accountTokenManager") TokenManager tokenManager,
            AccountModifyConfiguration configuration,
            AccountMapper mapper
    ) {
        this.repository = repository;
        this.encoder = encoder;
        this.emailService = emailService;
        this.tokenManager = tokenManager;
        this.configuration = configuration;
        this.mapper = mapper;
    }

    /**
     * Retrieve an account from the email.
     *
     * @param email Email to find the account.
     * @return The instance stored in database, or {@code null} if no account is found.
     */
    public Account getByEmailOrNull(@NonNull String email) {
        return repository.findByEmail(email);
    }

    /**
     * Retrieve an account from the email.
     *
     * @param email Email to find the account.
     * @return The instance stored in database.
     * @throws AccountNotFoundException If no account is linked to the mail.
     */
    public Account getByEmailOrThrow(@NonNull String email) throws AccountNotFoundException {
        Account account = getByEmailOrNull(email);
        if (account == null) {
            throw new AccountNotFoundException();
        }
        return account;
    }

    /**
     * Check if an account associated to the email exists.
     *
     * @param email Email to find the account.
     * @return {@code true} if an account is linked to the email, {@code false} otherwise.
     */
    public boolean existsEmail(@NonNull String email) {
        return repository.existsByEmail(email);
    }

    /**
     * Check if an account associated to the email exists.
     * If an account exists, throw an exception.
     *
     * @param email Email to find the account.
     * @throws MailExistsException If a mail exists.
     */
    public void existsEmailThrow(@NonNull String email) throws MailExistsException {
        if (existsEmail(email)) {
            throw new MailExistsException();
        }
    }

    /**
     * Encrypt the password of the account and save it into the database.
     *
     * @param account Account that will be stored.
     * @return The account instance from JPA.
     */
    public Account createAccount(@NonNull Account account) {
        String encryptPassword = encoder.encode(account.getPassword());
        account.setPassword(encryptPassword);
        return repository.save(account);
    }

    /**
     * Change the verified status of an account.
     *
     * @param email    Email associated to the account that will be changed.
     * @param verified New status.
     */
    public void setVerified(@NonNull String email, boolean verified) {
        repository.setVerified(email, verified);
    }

    /**
     * Change the banned status of an account.
     *
     * @param email  Email associated to the account that will be changed.
     * @param banned New status.
     */
    public void setBanned(@NonNull String email, boolean banned) {
        repository.setBanned(email, banned);
    }

    /**
     * Send a mail to validate the change of mail.
     *
     * @param headers  HTTP request header.
     * @param email    Current user's mail.
     * @param newEmail New mail for the change.
     * @throws MessagingException If an error is produced during mail sending.
     */
    public void sendMailToModifyEmail(
            @NonNull HttpHeaders headers,
            @NonNull String email,
            @NonNull String newEmail
    ) throws MessagingException {
        sendEmail(email, buildURIValidation(headers, newEmail), newEmail);
    }

    /**
     * Modify the information about an account.
     *
     * @param account       Account that will be modified.
     * @param modifyAccount Set of data that will be changed into the user's account.
     * @return {@code true} if at least one field is modified, {@code false} otherwise.
     */
    public boolean modifyInfoAccount(@NonNull Account account, @NonNull ModifyAccountDTO modifyAccount) {
        String firstname = modifyAccount.getFirstname();
        boolean wantsModifyFirstname = Objects.nonNull(firstname) && !account.getFirstname().equals(firstname);

        String lastname = modifyAccount.getLastname();
        boolean wantsModifyLastname = Objects.nonNull(lastname) && !account.getLastname().equals(lastname);

        String address = modifyAccount.getAddress();
        boolean wantsModifyAddress = Objects.nonNull(address) && !account.getAddress().equals(address);

        if (!wantsModifyFirstname && !wantsModifyLastname && !wantsModifyAddress) {
            return false;
        }

        if (wantsModifyFirstname) {
            account.setFirstname(firstname);
        }
        if (wantsModifyLastname) {
            account.setLastname(lastname);
        }
        if (wantsModifyAddress) {
            account.setAddress(address);
        }

        repository.save(account);
        return true;
    }

    /**
     * Send the email confirmation with a link to interact with the endpoint to confirm the account.
     *
     * @param email       User's email.
     * @param uriValidate URI link to valid the account.
     * @throws MessagingException If the mail cannot be sent.
     */
    private void sendEmail(
            @NonNull String email,
            @NonNull String uriValidate,
            @NonNull String newEmail
    ) throws MessagingException {
        AccountModifyConfiguration.MailConfiguration mail = configuration.getMail();
        emailService.sendHtmlMail(
                email,
                mail.getSubject(),
                createBodyWithURI(uriValidate, mail, newEmail)
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
            @NonNull AccountModifyConfiguration.MailConfiguration mail,
            @NonNull String newEmail
    ) {
        String mailContent = mail.getContent();
        String mailWithLink = mailContent.replace(mail.getTagLink(), uriValidate);

        String tagEmail = mail.getTagEmail();
        if (tagEmail == null) {
            return mailWithLink;
        }

        return mailWithLink.replace(tagEmail, newEmail);
    }

    /**
     * Build the URI to valid the user account.
     *
     * @param headers  Headers to find the current address of the server.
     * @param newEmail New free email.
     * @return The personal URI.
     */
    private String buildURIValidation(@NonNull HttpHeaders headers, @NonNull String newEmail) {
        return URIUtils.getRootURI(headers)
                .path("/mail/modify")
                .queryParam("id", tokenManager.createToken(newEmail).getToken())
                .build()
                .toString();
    }

    /**
     * Retrieve the data into the token and change the current mail of the user.
     *
     * @param email Current email.
     * @param token Token to change email.
     */
    public void setNewEmailFromToken(@NonNull String email, @NonNull String token) {
        String newEmail = tokenManager.getContentFromTokenOrNull(token);
        if (newEmail == null) {
            throw new InvalidTokenException();
        }

        existsEmailThrow(newEmail);

        Account account = getByEmailOrThrow(email);
        account.setEmail(newEmail);
        repository.save(account);
    }

    /**
     * Get the non critic information about a user from his mail.
     *
     * @param email Email of the account.
     * @return The instance of user information.
     */
    public UserInfo getUserInfoByEmail(@NonNull String email) {
        Account account = getByEmailOrThrow(email);
        return mapper.accountToUserInfo(account);
    }

    /**
     * Check if the account can interact with resources.
     * If the account is banned, or unverified, throw an exception.
     *
     * @param account Account.
     */
    public void connectionAuthorizedOrThrow(@NonNull Account account) {
        if (account.isBanned()) {
            throw new AccountBannedException();
        }

        if (!account.isVerified()) {
            throw new AccountNotVerifiedException();
        }
    }

    /**
     * Save the instance of an account.
     * @param account Data that will be saved.
     */
    public Account save(Account account) {
        return repository.save(account);
    }

    /**
     *  Delete an account
     * @param id the id of the account that will be deleted.
     */
    public void deleteAccountById(long id) {
        repository.deleteById(id);
    }
}
