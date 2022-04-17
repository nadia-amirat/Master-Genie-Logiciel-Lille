package fr.univ.givr.utils;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;

import javax.mail.internet.MimeMessage;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class MailUtils {

    public static GreenMailExtension loadGreenMail() {
        return new GreenMailExtension(ServerSetupTest.SMTP)
                .withConfiguration(GreenMailConfiguration.aConfig().withUser("test", "test"))
                .withPerMethodLifecycle(false);
    }

    public static MimeMessage waitNewMessage(int initialCountMail, GreenMailExtension greenMail) throws InterruptedException {
        MimeMessage[] messages = greenMail.getReceivedMessages();
        if(initialCountMail > messages.length) {
            throw new IllegalArgumentException("The initial count is greater the messages array");
        }
        else if (initialCountMail < messages.length) {
            return messages[initialCountMail];
        }

        CountDownLatch countDownLatch = new CountDownLatch(10);
        while(!countDownLatch.await(500, TimeUnit.MILLISECONDS)) {
            messages = greenMail.getReceivedMessages();
            if(initialCountMail < messages.length) {
                return messages[initialCountMail];
            }
            countDownLatch.countDown();
        }

        return null;
    }
}
