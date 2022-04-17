package fr.univ.givr.controller;

import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.GreenMailUtil;
import fr.univ.givr.AbstractIT;
import fr.univ.givr.configuration.RegisterConfiguration;
import fr.univ.givr.mapper.AccountMapper;
import fr.univ.givr.model.account.Account;
import fr.univ.givr.model.account.AccountDTO;
import fr.univ.givr.repository.AccountRepository;
import fr.univ.givr.security.JWTRequestFilter;
import fr.univ.givr.security.Token;
import fr.univ.givr.security.TokenManager;
import fr.univ.givr.service.account.AccountService;
import fr.univ.givr.utils.MailUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Mono;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RegisterControllerIT extends AbstractIT {

    @RegisterExtension
    static final GreenMailExtension GREEN_MAIL = MailUtils.loadGreenMail();

    private static int counterMail;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RegisterConfiguration registerConfiguration;

    @Autowired
    @Qualifier("JWTTokenManager")
    private TokenManager securityTokenManager;

    @Autowired
    @Qualifier("registerTokenManager")
    private TokenManager registerTokenManager;

    @Autowired
    private AccountService accountService;

    @BeforeAll
    static void onBeforeAll() {
        counterMail = 0;
    }

    @BeforeEach
    void onBefore() {
        accountRepository.deleteAll();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "test"
    })
    @NullSource
    void registerAccountWithEmailFieldInvalidTest(String email) {
        AccountDTO accountDto = createValidAccountDTO();
        accountDto.setEmail(email);

        requestCreateAccount(accountDto)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "1",
            "a-b"
    })
    @NullSource
    void registerAccountWithFirstnameFieldInvalidTest(String firstname) {
        AccountDTO accountDto = createValidAccountDTO();
        accountDto.setFirstname(firstname);

        requestCreateAccount(accountDto)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "1",
            "a-b"
    })
    @NullSource
    void registerAccountWithLastnameFieldInvalidTest(String lastname) {
        AccountDTO accountDto = createValidAccountDTO();
        accountDto.setLastname(lastname);

        requestCreateAccount(accountDto)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "", // empty
            "1azerty", // < 8 char
            "1azertyu", // No upper
            "azertyuI", // No number
            "12345678", // No letter
            "1AERTYUI" // No lower
    })
    @NullSource
    void registerAccountWithPasswordFieldInvalidTest(String password) {
        AccountDTO accountDto = createValidAccountDTO();
        accountDto.setPassword(password);

        requestCreateAccount(accountDto)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            // empty
            // 256 chars
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
    })
    @NullSource
    void registerAccountWithAddressFieldInvalidTest(String address) {
        AccountDTO accountDto = createValidAccountDTO();
        accountDto.setAddress(address);

        requestCreateAccount(accountDto)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    void registerAccountWithMailAlreadyExistsTest() {
        Account account = createValidVerifiedAccount();
        accountRepository.save(account);

        AccountDTO accountDto = accountMapper.accountToDTO(account);
        requestCreateAccount(accountDto)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void registerAccountSaveTheNewAccountAndSendMailTest() throws MessagingException, InterruptedException {
        AccountDTO accountDto = createValidAccountDTO();

        requestCreateAccount(accountDto)
                .exchange()
                .expectStatus()
                .isOk();

        Account account = accountService.getByEmailOrThrow(accountDto.getEmail());
        assertThat(account.isVerified()).isFalse();
        assertThat(account.getEmail()).isEqualTo(accountDto.getEmail());
        assertThat(account.getAddress()).isEqualTo(accountDto.getAddress());
        assertThat(account.getFirstname()).isEqualTo(accountDto.getFirstname());
        assertThat(account.getLastname()).isEqualTo(accountDto.getLastname());

        // encrypt
        assertThat(account.getPassword()).isNotEqualTo(accountDto.getPassword());
        assertThat(passwordEncoder.matches(accountDto.getPassword(), account.getPassword())).isTrue();

        MimeMessage mail = MailUtils.waitNewMessage(counterMail, GREEN_MAIL);
        assertThat(mail).isNotNull();
        counterMail++;
        assertMailContentIsConfirmAccount(account.getEmail(), mail);
    }

    @Test
    void resendValidationMailWithNonExistentAccountTest() {
        requestResendMail("test")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void resendValidationMailWithVerifiedAccountTest() throws InterruptedException {
        Account account = createValidVerifiedAccount();
        account.setVerified(true);
        accountRepository.save(account);

        requestResendMail(account.getEmail())
                .exchange()
                .expectStatus()
                .isOk();

        MimeMessage mail = MailUtils.waitNewMessage(counterMail, GREEN_MAIL);
        assertThat(mail).isNull();
    }

    @Test
    void resendValidationMailWithUnverifiedAccountTest() throws MessagingException, InterruptedException {
        Account account = createValidVerifiedAccount();
        account.setVerified(false);
        accountRepository.save(account);

        requestResendMail(account.getEmail())
                .exchange()
                .expectStatus()
                .isOk();

        MimeMessage mail = MailUtils.waitNewMessage(counterMail, GREEN_MAIL);
        assertThat(mail).isNotNull();
        counterMail++;
        assertMailContentIsConfirmAccount(account.getEmail(), mail);
    }

    @Test
    void confirmAccountWithBadTokenTest() {
        requestConfirmAccount("test")
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    void confirmAccountWithTokenLinkedToANonExistentAccountTest() {
        String token = registerTokenManager.createToken("test").getToken();
        requestConfirmAccount(token)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void confirmAccountWithTokenLinkedToAVerifiedAccountWithoutAuthTokenTest() {
        Account account = createValidVerifiedAccount();
        account.setVerified(true);
        accountRepository.save(account);

        String token = registerTokenManager.createToken(account.getEmail()).getToken();
        requestConfirmAccount(token)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void confirmAccountWithTokenLinkedToAVerifiedAccountWithAuthTokenTest() {
        Account account = createValidVerifiedAccount();
        account.setVerified(true);
        accountRepository.save(account);

        String email = account.getEmail();

        String token = registerTokenManager.createToken(email).getToken();
        String authToken = securityTokenManager.createToken(email).getToken();
        requestConfirmAccount(token)
                .headers(getHeaderAuthToken(authToken))
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void confirmAccountWithTokenLinkedToUnverifiedAccountWithoutAuthTokenTest() {
        Account account = createValidVerifiedAccount();
        account.setVerified(false);
        accountRepository.save(account);

        String token = registerTokenManager.createToken(account.getEmail()).getToken();
        requestConfirmAccount(token)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void confirmAccountWithTokenLinkedToUnverifiedAccountWithAuthTokenTest() {
        Account account = createValidVerifiedAccount();
        account.setVerified(false);
        accountRepository.save(account);

        String email = account.getEmail();

        String token = registerTokenManager.createToken(email).getToken();
        String authToken = securityTokenManager.createToken(email).getToken();
        requestConfirmAccount(token)
                .headers(getHeaderAuthToken(authToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectCookie()
                .valueEquals(JWTRequestFilter.COOKIE_NAME, "invalid");
    }

    @Test
    void createAccountAndValidItByTheURIWithoutAuthenticateTokenTest() throws InterruptedException {
        AccountDTO accountDTO = createValidAccountDTO();
        requestCreateAccount(accountDTO)
                .exchange()
                .expectStatus()
                .isOk();

        Account account = accountService.getByEmailOrThrow(accountDTO.getEmail());
        assertThat(account.isVerified()).isFalse();

        MimeMessage mail = MailUtils.waitNewMessage(counterMail, GREEN_MAIL);
        assertThat(mail).isNotNull();
        counterMail++;
        String uri = retrieveURIFromBodyMail(GreenMailUtil.getBody(mail));
        getWebTestClient()
                .get()
                .uri(uri)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void createAccountAndValidItByTheURIWithAuthenticateTokenTest() throws InterruptedException {
        AccountDTO accountDTO = createValidAccountDTO();
        requestCreateAccount(accountDTO)
                .exchange()
                .expectStatus()
                .isOk();

        final Account account = accountService.getByEmailOrThrow(accountDTO.getEmail());
        assertThat(account.isVerified()).isFalse();

        MimeMessage mail = MailUtils.waitNewMessage(counterMail, GREEN_MAIL);
        assertThat(mail).isNotNull();
        counterMail++;
        String uri = retrieveURIFromBodyMail(GreenMailUtil.getBody(mail));

        Token token = securityTokenManager.createToken(account.getEmail());
        getWebTestClient()
                .get()
                .uri(uri)
                .headers(getHeaderAuthToken(token.getToken()))
                .exchange()
                .expectStatus()
                .isOk()
                .expectCookie()
                .valueEquals(JWTRequestFilter.COOKIE_NAME, "invalid");

        Account accountDB = accountService.getByEmailOrThrow(accountDTO.getEmail());
        assertThat(accountDB.isVerified()).isTrue();
    }

    private WebTestClient.RequestHeadersSpec<?> requestResendMail(String email) {
        return getWebTestClient()
                .get()
                .uri((uriBuilder -> uriBuilder.path("/api/register")
                        .queryParam("email", email)
                        .build()));
    }

    private WebTestClient.RequestHeadersSpec<?> requestConfirmAccount(String token) {
        return getWebTestClient()
                .get()
                .uri((uriBuilder -> uriBuilder.path("/mail/confirm") // /api/register/confirm
                        .queryParam("id", token)
                        .build()));
    }

    private WebTestClient.RequestHeadersSpec<?> requestCreateAccount(AccountDTO accountDto) {
        return getWebTestClient()
                .post()
                .uri((uriBuilder -> uriBuilder.path("/api/register")
                        .build()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(accountDto), AccountDTO.class);
    }

    private void assertMailContentIsConfirmAccount(String userEmail, MimeMessage receivedMessage) throws
            MessagingException {
        RegisterConfiguration.MailConfiguration mailConfig = registerConfiguration.getMail();
        assertThat(receivedMessage.getSubject()).isEqualTo(mailConfig.getSubject());

        String body = GreenMailUtil.getBody(receivedMessage);
        assertThat(body).contains(getURIConfirmAccount());

        String uri = retrieveURIFromBodyMail(body);
        String token = getTokenConfirmFromURI(uri);

        String content = registerTokenManager.getContentFromTokenOrNull(token);
        assertThat(content).isEqualTo(userEmail);
    }

    private String getURIConfirmAccount() {
        return new DefaultUriBuilderFactory().builder()
                .scheme("http")
                .host("localhost")
                .port(port)
                .path("/mail/confirm") // /api/register/confirm
                .queryParam("id")
                .build()
                .toString();
    }

    private String retrieveURIFromBodyMail(String body) {
        RegisterConfiguration.MailConfiguration mailRegisterConfig = registerConfiguration.getMail();
        String mailBodyBuilder = mailRegisterConfig.getContent();
        String tag = mailRegisterConfig.getTagLink();

        int indexTagBegin = mailBodyBuilder.indexOf(tag);

        String rightPartContent = mailBodyBuilder.substring(indexTagBegin + tag.length());

        String bodyWithoutLeftPart = body.substring(indexTagBegin);
        int leftPartBegin = bodyWithoutLeftPart.indexOf(rightPartContent);
        return bodyWithoutLeftPart.substring(0, leftPartBegin);
    }

    private String getTokenConfirmFromURI(String uri) {
        String tag = "id=";
        int index = uri.indexOf(tag);
        return uri.substring(index + tag.length());
    }

    private AccountDTO createValidAccountDTO() {
        Account account = createValidVerifiedAccount();
        return accountMapper.accountToDTO(account);
    }
}
