package fr.univ.givr.utils;

import fr.univ.givr.AbstractIT;
import fr.univ.givr.security.Token;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class JWTUtilsIT extends AbstractIT {

    @Autowired
    private JWTUtils jwtUtils;

    @Nested
    class CreateToken {

        @Test
        void contentIsSimilarToTheUsedContentTest() {
            String content = "test";
            String secret = "test_secret";

            Token token = jwtUtils.createToken(content, 1000, secret, SignatureAlgorithm.HS256);
            String token_content = jwtUtils.getContentOrNull(token.getToken(), secret);

            assertThat(token_content).isEqualTo(content);
        }

        @Test
        void differentValidityCreateDifferentTokenTest() {
            String content = "test";
            String secret = "test_secret";

            Token token = jwtUtils.createToken(content, 1, secret, SignatureAlgorithm.HS256);
            Token token2 = jwtUtils.createToken(content, 1000 * 60, secret, SignatureAlgorithm.HS256);

            assertThat(token.getToken()).isNotEqualTo(token2.getToken());
        }

        @Test
        void differentContentCreateDifferentTokenTest() {
            String content = "test";
            String secret = "test_secret";

            Token token = jwtUtils.createToken(content, 1, secret, SignatureAlgorithm.HS256);
            Token token2 = jwtUtils.createToken(content + "a", 1, secret, SignatureAlgorithm.HS256);

            assertThat(token.getToken()).isNotEqualTo(token2.getToken());
        }

        @Test
        void differentSecretCreateDifferentTokenTest() {
            String content = "test";
            String secret = "test_secret";

            Token token = jwtUtils.createToken(content, 1, secret, SignatureAlgorithm.HS256);
            Token token2 = jwtUtils.createToken(content, 1, secret + secret, SignatureAlgorithm.HS256);

            assertThat(token.getToken()).isNotEqualTo(token2.getToken());
        }

        @Test
        void differentAlgorithmCreateDifferentTokenTest() {
            String content = "test";
            String secret = "test_secret";

            Token token = jwtUtils.createToken(content, 1, secret, SignatureAlgorithm.HS256);
            Token token2 = jwtUtils.createToken(content, 1, secret, SignatureAlgorithm.HS384);

            assertThat(token.getToken()).isNotEqualTo(token2.getToken());
        }

    }

    @Nested
    class GetContentOrNull {

        @Test
        void isNotNullWithGoodTokenAndSecretTest() {
            String content = "test";
            String secret = "test_secret";

            Token token = jwtUtils.createToken(content, 1000 * 60, secret, SignatureAlgorithm.HS256);
            String token_content = jwtUtils.getContentOrNull(token.getToken(), secret);

            assertThat(token_content).isEqualTo(content);
        }

        @Test
        void isNullWithBadSecretTest() {
            String content = "test";
            String secret = "test_secret";

            Token token = jwtUtils.createToken(content, 1000 * 60, secret, SignatureAlgorithm.HS256);
            String token_content = jwtUtils.getContentOrNull(token.getToken(), secret + secret);

            assertThat(token_content).isNull();
        }

        @Test
        void isNullWithBadTokenTest() {
            String content = "test";
            String secret = "test_secret";

            Token token = jwtUtils.createToken(content, 1000 * 60, secret, SignatureAlgorithm.HS256);
            String token_content = jwtUtils.getContentOrNull(token.getToken() + 'a', secret);

            assertThat(token_content).isNull();
        }

        @Test
        void isNullWithTokenExpiredTest() {
            String content = "test";
            String secret = "test_secret";

            Token token = jwtUtils.createToken(content, 0, secret, SignatureAlgorithm.HS256);
            String token_content = jwtUtils.getContentOrNull(token.getToken(), secret);

            assertThat(token_content).isNull();
        }
    }

    @Nested
    class GetContentOrThrow {

        @Test
        void noThrowWithGoodTokenAndSecretTest() {
            String content = "test";
            String secret = "test_secret";

            Token token = jwtUtils.createToken(content, 1000 * 60, secret, SignatureAlgorithm.HS256);
            String token_content = jwtUtils.getContentOrThrow(token.getToken(), secret);

            assertThat(token_content).isEqualTo(content);
        }

        @Test
        void throwWithBadSecretTest() {
            String content = "test";
            String secret = "test_secret";

            Token token = jwtUtils.createToken(content, 1000 * 60, secret, SignatureAlgorithm.HS256);
            Assertions.assertThrows(
                    SignatureException.class,
                    () -> jwtUtils.getContentOrThrow(token.getToken(), secret + secret)
            );
        }

        @Test
        void throwWithBadTokenTest() {
            String content = "test";
            String secret = "test_secret";

            Token token = jwtUtils.createToken(content, 1000 * 60, secret, SignatureAlgorithm.HS256);
            Assertions.assertThrows(
                    SignatureException.class,
                    () -> jwtUtils.getContentOrThrow(token.getToken() + 'a', secret)
            );
        }

        @Test
        void throwWithTokenExpiredTest() {
            String content = "test";
            String secret = "test_secret";

            Token token = jwtUtils.createToken(content, 0, secret, SignatureAlgorithm.HS256);
            Assertions.assertThrows(
                    ExpiredJwtException.class,
                    () -> jwtUtils.getContentOrThrow(token.getToken(), secret)
            );
        }

    }
}
