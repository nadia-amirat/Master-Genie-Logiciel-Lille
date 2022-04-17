package fr.univ.givr.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.NotNull;

/**
 * Service to manage mail easily.
 */
@Slf4j
@Service
public class EmailService {

    /**
     * Sender's username to send the mail.
     */
    private final String senderUsername;

    /**
     * Mail sender.
     */
    private final JavaMailSender mailSender;

    public EmailService(@Value("${spring.mail.username}") String senderUsername, JavaMailSender mailSender) {
        this.senderUsername = senderUsername;
        this.mailSender = mailSender;
    }

    /**
     * Send a mail with a html body.
     *
     * @param to      Target who will receive the mail.
     * @param subject Topic of the mail.
     * @param body    Content of the mail.
     */
    public void sendHtmlMail(@NotNull String to, @NonNull String subject, @NonNull String body) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setFrom(senderUsername);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);
        mailSender.send(mimeMessage);
    }
}
