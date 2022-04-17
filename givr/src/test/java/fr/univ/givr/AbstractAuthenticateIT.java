package fr.univ.givr;

import fr.univ.givr.model.account.Account;
import fr.univ.givr.model.account.AuthenticateDto;
import fr.univ.givr.security.Token;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@Getter
public abstract class AbstractAuthenticateIT extends AbstractUserIT {

    @Autowired
    protected PasswordEncoder encoder;

    protected void verifyContentToken(Account account, Token token) {
        assertThat(token.getExpireAt()).isGreaterThan(Instant.now().toEpochMilli());
        String id = securityTokenManager.getContentFromTokenOrNull(token.getToken());
        assertThat(id).isEqualTo(account.getEmail());
    }

    protected AuthenticateDto createAuthenticateFromAccount(Account account) {
        return new AuthenticateDto(account.getEmail(), account.getPassword());
    }

    @Test
    void getTokenFromValidAccountTest() {
        Account account = createValidVerifiedAccount();
        String pwd = account.getPassword();

        String encryptPwd = encoder.encode(pwd);
        account.setPassword(encryptPwd);

        accountRepository.save(account);

        AuthenticateDto auth = createAuthenticateFromAccount(account);
        auth.setPassword(pwd);

        assertGetTokenFromValidAuth(account, auth);
    }

    @Test
    void getTokenFromValidAccountButNotEncryptInDatabaseTest() {
        Account account = createValidVerifiedAccount();
        accountRepository.save(account);

        AuthenticateDto auth = createAuthenticateFromAccount(account);
        assertGetTokenFromNonExistantAuth(auth);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "test", "test@", "test@test."})
    @NullSource
    void getTokenWithInvalidAuthMailTest(String mail) {
        AuthenticateDto authInvalidMail = new AuthenticateDto(mail, passwordUtils.generateValidPassword());
        assertGetTokenFromInvalidAuth(authInvalidMail);
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
    void getTokenWithInvalidAuthPasswordTest(String password) {
        AuthenticateDto authInvalidPassword = new AuthenticateDto("test@test.com", password);
        assertGetTokenFromInvalidAuth(authInvalidPassword);
    }

    @Test
    void getTokenFromANonExistentAccountTest() {
        AuthenticateDto auth = new AuthenticateDto("test@test.com", passwordUtils.generateValidPassword());
        assertGetTokenFromNonExistantAuth(auth);
    }

    protected abstract void assertGetTokenFromValidAuth(Account account, AuthenticateDto authenticateDto);

    protected abstract void assertGetTokenFromNonExistantAuth(AuthenticateDto authenticateDto);

    protected abstract void assertGetTokenFromInvalidAuth(AuthenticateDto authenticateDto);
}
