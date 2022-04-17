package fr.univ.givr.controller;

import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.GreenMailUtil;
import fr.univ.givr.AbstractUserIT;
import fr.univ.givr.configuration.AccountModifyConfiguration;
import fr.univ.givr.model.account.Account;
import fr.univ.givr.model.account.ModifyAccountDTO;
import fr.univ.givr.model.account.UserInfo;
import fr.univ.givr.model.role.Role;
import fr.univ.givr.security.TokenManager;
import fr.univ.givr.utils.MailUtils;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Mono;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountRestControllerIT extends AbstractUserIT {

    @RegisterExtension
    static final GreenMailExtension GREEN_MAIL = MailUtils.loadGreenMail();

    private static int counterMail;

    @Autowired
    private AccountModifyConfiguration accountModifyConfiguration;

    @Autowired
    @Qualifier("accountTokenManager")
    private TokenManager accountTokenManager;

    @BeforeAll
    static void onBeforeAll() {
        counterMail = 0;
    }

    private static MimeMessage getReceivedNextMessage() {
        return GREEN_MAIL.getReceivedMessages()[counterMail++];
    }

    private static int getNumberMail() {
        return GREEN_MAIL.getReceivedMessages().length;
    }

    @BeforeEach
    void onBefore() {
        accountRepository.deleteAll();
    }

    @Test
    void wantsModifyButNotAuthenticatedTest() {
        Account account = createValidVerifiedAccount();
        requestModifyAccount(new ModifyAccountDTO(
                account.getEmail(),
                account.getFirstname(),
                account.getLastname(),
                account.getAddress()
        ))
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "test", "test@", "test@test."})
    void wantsModifyButEmailInvalidTest(String email) {
        Account account = createValidVerifiedAccount();
        requestModifyAccount(new ModifyAccountDTO(
                email,
                account.getFirstname(),
                account.getLastname(),
                account.getAddress()
        ))
                .headers(getHeaderAuthToken(saveAccountAndGetToken(account)))
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
    void wantsModifyButFirstnameInvalidTest(String firstname) {
        Account account = createValidVerifiedAccount();
        requestModifyAccount(new ModifyAccountDTO(
                account.getEmail(),
                firstname,
                account.getLastname(),
                account.getAddress()
        ))
                .headers(getHeaderAuthToken(saveAccountAndGetToken(account)))
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
    void wantsModifyButLastnameInvalidTest(String lastname) {
        Account account = createValidVerifiedAccount();
        requestModifyAccount(new ModifyAccountDTO(
                account.getEmail(),
                account.getFirstname(),
                lastname,
                account.getAddress()
        ))
                .headers(getHeaderAuthToken(saveAccountAndGetToken(account)))
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
    void wantsModifyButAddressInvalidTest(String address) {
        Account account = createValidVerifiedAccount();
        requestModifyAccount(new ModifyAccountDTO(
                account.getEmail(),
                account.getFirstname(),
                account.getLastname(),
                address
        ))
                .headers(getHeaderAuthToken(saveAccountAndGetToken(account)))
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    void wantsModifyButAllFieldsAreNullTest() {
        requestModifyAccount(new ModifyAccountDTO(null, null, null, null))
                .headers(getHeaderAuthToken(createAccountAndGetAuthToken()))
                .exchange()
                .expectStatus()
                .isNotModified();
    }

    @Test
    void wantsModifyAllFieldsExceptMailTest() {
        Account account = createValidVerifiedAccount();

        String newFirstname = account.getFirstname() + "a";
        String newLastname = account.getLastname() + "a";
        String newAddress = account.getAddress() + "a";

        int previousNumberMail = getNumberMail();

        requestModifyAccount(new ModifyAccountDTO(null, newFirstname, newLastname, newAddress))
                .headers(getHeaderAuthToken(saveAccountAndGetToken(account)))
                .exchange()
                .expectStatus()
                .isOk();

        assertThat(previousNumberMail).isEqualTo(getNumberMail());

        Account newAccountData = accountService.getByEmailOrThrow(account.getEmail());
        assertThat(newAccountData.getFirstname()).isEqualTo(newFirstname);
        assertThat(newAccountData.getLastname()).isEqualTo(newLastname);
        assertThat(newAccountData.getAddress()).isEqualTo(newAddress);
    }

    @Test
    void wantsModifyEmailThatSendEmailToConfirmTest() {
        Account account = createValidVerifiedAccount();

        String newEmail = account.getEmail() + "a";

        int previousNumberMail = getNumberMail();

        requestModifyAccount(new ModifyAccountDTO(newEmail, null, null, null))
                .headers(getHeaderAuthToken(saveAccountAndGetToken(account)))
                .exchange()
                .expectStatus()
                .isOk();

        getReceivedNextMessage();
        assertThat(previousNumberMail).isLessThan(getNumberMail());
    }

    @Test
    void wantsModifyAllFieldsWithEmailNotSetAContrarioToOtherFieldsTest() {
        Account account = createValidVerifiedAccount();

        String newEmail = account.getEmail() + "a";
        String newFirstname = account.getFirstname() + "a";
        String newLastname = account.getLastname() + "a";
        String newAddress = account.getAddress() + "a";

        int previousNumberMail = getNumberMail();

        requestModifyAccount(new ModifyAccountDTO(newEmail, newFirstname, newLastname, newAddress))
                .headers(getHeaderAuthToken(saveAccountAndGetToken(account)))
                .exchange()
                .expectStatus()
                .isOk();

        getReceivedNextMessage();
        // Mail received
        assertThat(previousNumberMail).isLessThan(getNumberMail());

        Account newAccountData = accountService.getByEmailOrThrow(account.getEmail());
        assertThat(newAccountData.getEmail()).isNotEqualTo(newEmail);
        assertThat(newAccountData.getFirstname()).isEqualTo(newFirstname);
        assertThat(newAccountData.getLastname()).isEqualTo(newLastname);
        assertThat(newAccountData.getAddress()).isEqualTo(newAddress);
    }

    @Test
    void wantsModifyEmailThatSendEmailWithTokenCorrespondingToTheNewMailTest() throws MessagingException {
        Account account = createValidVerifiedAccount();

        String newEmail = account.getEmail() + "a";

        requestModifyAccount(new ModifyAccountDTO(newEmail, null, null, null))
                .headers(getHeaderAuthToken(saveAccountAndGetToken(account)))
                .exchange()
                .expectStatus()
                .isOk();

        assertMailContentHasNewMailEncrypt(newEmail, getReceivedNextMessage());
    }

    @Test
    void confirmMailButNotAuthenticateTest() {
        requestConfirmMail("test")
                .exchange()
                .expectStatus()
                .isOk(); // Redirection to login page
    }

    @Test
    void confirmMailWithInvalidAuthTokenTest() {
        requestConfirmMail("test")
                .headers(getHeaderAuthToken("test"))
                .exchange()
                .expectStatus()
                .isOk(); // Redirection to login page
    }

    @Test
    void confirmMailWithValidAuthTokenAndInvalidTokenMailTest() {
        Account account = createValidVerifiedAccount();
        String token = saveAccountAndGetToken(account);

        requestConfirmMail("test")
                .headers(getHeaderAuthToken(token))
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    void confirmMailWithValidAuthTokenAndValidTokenButAccountAlreadyExistsMailTest() {
        Account account = createValidVerifiedAccount();
        String token = saveAccountAndGetToken(account);

        Account account2 = createValidVerifiedAccount();
        accountRepository.save(account2);

        String tokenNewEmail = accountTokenManager.createToken(account2.getEmail()).getToken();

        requestConfirmMail(tokenNewEmail)
                .headers(getHeaderAuthToken(token))
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void confirmMailWithValidAuthTokenAndValidTokenAccountWithMailNotExistsTest() {
        Account account = createValidVerifiedAccount();
        String oldEmail = account.getEmail();
        String newEmail = "test";

        String token = saveAccountAndGetToken(account);
        String tokenNewEmail = accountTokenManager.createToken(newEmail).getToken();

        requestConfirmMail(tokenNewEmail)
                .headers(getHeaderAuthToken(token))
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.OK);

        Account newAccountData = accountService.getByEmailOrThrow(newEmail);
        assertThat(newAccountData.getEmail()).isNotEqualTo(oldEmail);
        assertThat(newAccountData.getPassword()).isEqualTo(account.getPassword());
        assertThat(newAccountData.getFirstname()).isEqualTo(account.getFirstname());
        assertThat(newAccountData.getLastname()).isEqualTo(account.getLastname());
        assertThat(newAccountData.getAddress()).isEqualTo(account.getAddress());
    }

    @Test
    void confirmMailByAskingAndInteractWithTheLinkTest() {
        Account account = createValidVerifiedAccount();
        String token = saveAccountAndGetToken(account);
        account = accountService.getByEmailOrThrow(account.getEmail());

        String newEmail = "test@test.com";

        requestModifyAccount(new ModifyAccountDTO(newEmail, null, null, null))
                .headers(getHeaderAuthToken(token))
                .exchange()
                .expectStatus()
                .isOk();

        MimeMessage message = getReceivedNextMessage();
        String uri = retrieveURIFromBodyMail(newEmail, GreenMailUtil.getBody(message));
        getWebTestClient()
                .get()
                .uri(uri)
                .headers(getHeaderAuthToken(token))
                .exchange()
                .expectStatus()
                .isOk();

        Long id = account.getId();

        Account newAccountData = accountRepository.findById(id).orElseThrow();
        assertThat(newAccountData.getEmail()).isEqualTo(newEmail);
        assertThat(newAccountData.getPassword()).isEqualTo(account.getPassword());
        assertThat(newAccountData.getFirstname()).isEqualTo(account.getFirstname());
        assertThat(newAccountData.getLastname()).isEqualTo(account.getLastname());
        assertThat(newAccountData.getAddress()).isEqualTo(account.getAddress());
    }

    @Test
    void getUserInfoButCurrentAccountIsNotAuthenticateTest() {
        requestGetInfoAccountByEmail("test")
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }

    @Test
    void getUserInfoButCurrentAccountIsNotAdminAndNotVerifiedTest() {
        Account account = createAndSaveAccount(false);
        String token = createToken(account);

        requestGetInfoAccountByEmail("test")
                .headers(getHeaderAuthToken(token))
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }

    @Test
    void getUserInfoButCurrentAccountIsNotAdminAndVerifiedTest() {
        requestGetInfoAccountByEmail("test")
                .headers(getHeaderAuthToken(createAccountVerifiedAndGetAuthToken()))
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }

    @Test
    void getUserInfoButCurrentAccountIsAdminButNotVerifiedTest() {
        Account account = createAndSaveAccount(false, Role.ADMIN);
        requestGetInfoAccountByEmail("test")
                .headers(getHeaderAuthToken(createToken(account)))
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }

    @Test
    void getUserInfoButEmailNotLinkedToAnAccountTest() {
        Account account = createAndSaveAccount(true, Role.ADMIN);
        String token = createToken(account);

        requestGetInfoAccountByEmail("test")
                .headers(getHeaderAuthToken(token))
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void getUserInfoTest() {
        Account account = createAndSaveAccount(true, Role.ADMIN);
        String token = createToken(account);

        Account client = createAndSaveAccount(false);

        FluxExchangeResult<UserInfo> result = requestGetInfoAccountByEmail(client.getEmail())
                .headers(getHeaderAuthToken(token))
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(UserInfo.class);

        UserInfo expected = accountMapper.accountToUserInfo(client);
        verifyByPredicate(result.getResponseBody().single(), userInfo -> userInfo.equals(expected));
    }

    private WebTestClient.RequestHeadersSpec<?> requestModifyAccount(ModifyAccountDTO modifyAccountDTO) {
        return getWebTestClient()
                .patch()
                .uri((uriBuilder -> uriBuilder.path("/api/account/modify")
                        .build()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(modifyAccountDTO), ModifyAccountDTO.class);
    }

    private WebTestClient.RequestHeadersSpec<?> requestConfirmMail(String token) {
        return getWebTestClient()
                .get()
                .uri((uriBuilder -> uriBuilder.path("/mail/modify") // /api/account/modify/confirm
                        .queryParam("id", token)
                        .build()));
    }

    private WebTestClient.RequestHeadersSpec<?> requestGetInfoAccountByEmail(String email) {
        return getWebTestClient()
                .get()
                .uri((uriBuilder -> uriBuilder.path("/api/account")
                        .queryParam("email", email)
                        .build()));
    }

    private void assertMailContentHasNewMailEncrypt(String newEmail, MimeMessage receivedMessage) throws
            MessagingException {
        AccountModifyConfiguration.MailConfiguration mailConfig = accountModifyConfiguration.getMail();
        AssertionsForClassTypes.assertThat(receivedMessage.getSubject()).isEqualTo(mailConfig.getSubject());

        String body = GreenMailUtil.getBody(receivedMessage);
        AssertionsForClassTypes.assertThat(body).contains(getURIConfirmAccount());

        String uri = retrieveURIFromBodyMail(newEmail, body);
        String token = getTokenConfirmFromURI(uri);

        String content = accountTokenManager.getContentFromTokenOrNull(token);
        AssertionsForClassTypes.assertThat(content).isEqualTo(newEmail);
    }

    private String getURIConfirmAccount() {
        return new DefaultUriBuilderFactory().builder()
                .scheme("http")
                .host("localhost")
                .port(port)
                .path("/mail/modify")// /api/account/modify/confirm
                .queryParam("id")
                .build()
                .toString();
    }

    private String retrieveURIFromBodyMail(String newEmail, String body) {
        AccountModifyConfiguration.MailConfiguration mailConfig = accountModifyConfiguration.getMail();
        String mailBodyBuilder = mailConfig.getContent();
        String tag = mailConfig.getTagLink();

        int indexTagBegin = mailBodyBuilder.indexOf(tag);

        String rightPartContent = mailBodyBuilder.substring(indexTagBegin + tag.length())
                .replace(mailConfig.getTagEmail(), newEmail);

        String bodyWithoutLeftPart = body.substring(indexTagBegin);
        int leftPartBegin = bodyWithoutLeftPart.indexOf(rightPartContent);
        return bodyWithoutLeftPart.substring(0, leftPartBegin);
    }

    private String getTokenConfirmFromURI(String uri) {
        String tag = "id=";
        int index = uri.indexOf(tag);
        return uri.substring(index + tag.length());
    }
}
