package fr.univ.givr.service;

import fr.univ.givr.AbstractAuthenticateIT;
import fr.univ.givr.exception.user.UnableAuthenticateException;
import fr.univ.givr.model.account.Account;
import fr.univ.givr.model.account.AuthenticateDto;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthenticateServiceIT extends AbstractAuthenticateIT {

    @Autowired
    private AuthenticateService service;

    @Override
    protected void assertGetTokenFromInvalidAuth(AuthenticateDto authenticateDto) {
        assertGetTokenFromNonExistantAuth(authenticateDto);
    }

    @Override
    protected void assertGetTokenFromNonExistantAuth(AuthenticateDto authenticateDto) {
        Class<? extends Throwable> exception = authenticateDto.getEmail() == null
                ? NullPointerException.class
                : UnableAuthenticateException.class;
        assertThrows(exception, () -> service.createToken(authenticateDto));
    }

    @Override
    protected void assertGetTokenFromValidAuth(Account account, AuthenticateDto authenticateDto) {
        verifyContentToken(account, service.createToken(authenticateDto));
    }
}
