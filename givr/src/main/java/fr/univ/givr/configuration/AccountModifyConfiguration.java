package fr.univ.givr.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

/**
 * Configuration for the account system.
 */
@Data
@Configuration
@ConfigurationProperties("account.modify")
@Validated
public class AccountModifyConfiguration {

    /**
     * Configuration about the register mail system.
     */
    @Data
    @Validated
    public static class MailConfiguration {

        /**
         * Mail's topic.
         */
        @NotNull
        @NotEmpty
        private String subject;

        /**
         * Mail's body.
         */
        @NotNull
        @NotEmpty
        private String content;

        /**
         * Tag findable in the content.
         * Allows to place the URI to valid the account into the content.
         */
        @NotNull
        @NotEmpty
        private String tagLink;

        /**
         * Tag findable in the content.
         * Allows to place the new email to valid the account into the content.
         */
        @NotEmpty
        private String tagEmail;

    }

    /**
     * Configuration about the account token system.
     */
    @Data
    @Validated
    public static class TokenConfiguration {

        /**
         * Secret to generate token.
         */
        @Size(min = 1)
        @NotNull
        private String secret;

        /**
         * Validity of the token in millisecond, cannot be less or equals than 0.
         */
        @Positive
        private long validity = 86400000;

    }

    /**
     * Mail configuration.
     */
    @NotNull
    private MailConfiguration mail;

    /**
     * Token configuration.
     */
    @NotNull
    private TokenConfiguration token;

    /**
     * Verify if the tag link is present into the content to build an email.
     * If the tag is not found, throw an exception.
     */
    @PostConstruct
    private void verifyMail() {
        checkMailContent("link", mail.tagLink);

        String tagEmail = mail.tagEmail;
        if(tagEmail != null) {
            checkMailContent("email", tagEmail);
        }
    }

    /**
     * Verify if the tag is present into the content.
     * @param name Name of the tag.
     * @param tag Value of the tag.
     */
    private void checkMailContent(String name, String tag) {
        if (!mail.content.contains(tag)) {
            throw new IllegalStateException(
                    "The content [" + mail.content + "] doesn't have the tag " + name + " [" + tag + "]");
        }
    }
}
