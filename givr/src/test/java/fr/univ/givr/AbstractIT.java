package fr.univ.givr;

import fr.univ.givr.model.account.Account;
import fr.univ.givr.security.JWTRequestFilter;
import fr.univ.givr.utils.PasswordUtils;
import lombok.Getter;
import org.junit.jupiter.api.Assertions;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

@SpringBootTest(classes = GivRApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class AbstractIT {

    private static final Random RANDOM = new Random();

    @Value("${server.port:8080}")
    protected int port;

    @Value("${server.path:}")
    private String path;

    @Getter(lazy = true)
    private final WebTestClient webTestClient = WebTestClient
            .bindToServer()
            .baseUrl("http://localhost:" + port + path)
            .responseTimeout(Duration.ofSeconds(10))
            .build();

    @Autowired
    protected PasswordUtils passwordUtils;

    /**
     * Create a new instance of Account with the value "test" for firstname, lastname and address.
     * The mail and password are valid to interact with the server.
     *
     * @return A new instance of Account.
     */
    protected Account createValidVerifiedAccount() {
        return Account
                .builder()
                .firstname(generateRandomLetterString())
                .lastname(generateRandomLetterString())
                .address(generateRandomLetterString())
                .email(UUID.randomUUID() + "@test.com")
                .password(passwordUtils.generateValidPassword())
                .verified(true)
                .build();
    }


    private String generateRandomLetterString() {
        StringBuilder builder = new StringBuilder();
        for(int cpt = 0; cpt < 20; cpt++) {
            char c = (char) ('a' + RANDOM.nextInt('z' - 'a'));
            builder.append(c);
        }
        return builder.toString();
    }

    /**
     * Get a consumer to add Bearer token in the authorization header.
     * @param token Auth token.
     * @return A consumer that add authorization value.
     */
    protected Consumer<HttpHeaders> getHeaderAuthToken(String token) {
        return headers -> headers.add(JWTRequestFilter.HEADER_AUTHORIZATION, JWTRequestFilter.TYPE_AUTHORIZATION + " " + token);
    }

    /**
     * Verify if the values are in the {@link Publisher} with follow the order.
     *
     * @param publisher Publisher.
     * @param values    Values that should be in publisher.
     * @param <T>       Type of the value.
     */
    @SafeVarargs
    protected final <T> void verifyContentWithOrder(Publisher<T> publisher, T... values) {
        verifyStepWithOrder(StepVerifier.create(publisher), values);
    }

    /**
     * Verify if the values are in the {@link Publisher} with follow the order.
     *
     * @param step   Verifier.
     * @param values Values that should be in publisher.
     * @param <T>    Type of the value.
     */
    @SafeVarargs
    protected final <T> void verifyStepWithOrder(StepVerifier.Step<T> step, T... values) {
        step.expectNext(values).expectComplete().verify();
    }

    /**
     * Verify if the values are in the {@link Publisher} without follow the order.
     *
     * @param publisher Publisher.
     * @param values    Values that should be in publisher.
     * @param <T>       Type of the value.
     */
    @SafeVarargs
    protected final <T> void verifyContentWithoutOrder(Publisher<T> publisher, T... values) {
        verifyStepWithoutOrder(StepVerifier.create(publisher), values);
    }

    /**
     * Verify if the values are in the {@link Publisher} without follow the order.
     *
     * @param step   Verifier.
     * @param values Values that should be in publisher.
     * @param <T>    Type of the value.
     */
    @SafeVarargs
    protected final <T> void verifyStepWithoutOrder(StepVerifier.Step<T> step, T... values) {
        if (values.length == 0) {
            step.expectComplete()
                    .verify();
            return;
        }

        List<T> productsSet = new ArrayList<>(values.length);
        Collections.addAll(productsSet, values);

        for (int cpt = 0; cpt < values.length; cpt++) {
            step = step.expectNextMatches(productsSet::remove);
        }

        step.expectComplete()
                .verify();

        Assertions.assertTrue(productsSet.isEmpty());
    }

    /**
     * Verify if the value in the mono respect the predicate condition.
     *
     * @param publisher Mono with value that will execute the predicate.
     * @param predicate Predicate to check if the value in mono is valid or not.
     * @param <T>       Type of the value.
     */
    protected final <T> void verifyByPredicate(Mono<T> publisher, Predicate<T> predicate) {
        StepVerifier.create(publisher)
                .expectNextMatches(predicate)
                .verifyComplete();
    }

}
