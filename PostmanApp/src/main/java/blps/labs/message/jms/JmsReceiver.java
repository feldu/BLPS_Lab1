package blps.labs.message.jms;

import blps.labs.message.model.AddReviewMessage;
import blps.labs.message.model.CheckReviewMessage;
import blps.labs.message.model.SpamMessage;
import blps.labs.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JmsReceiver {
    private final EmailService emailService;

    @Autowired
    public JmsReceiver(EmailService emailService) {
        this.emailService = emailService;
    }

    @JmsListener(destination = "addReviewQueue")
    public void receiveMessage(@Payload AddReviewMessage message) {
        log.info("Message {} received", message);
        emailService.sendEmail(message);
    }

    @JmsListener(destination = "checkReviewQueue")
    public void receiveMessage(@Payload CheckReviewMessage message) {
        log.info("Message {} received", message);
        emailService.sendEmail(message);
    }

    @JmsListener(destination = "sendSpamQueue")
    public void receiveMessage(@Payload SpamMessage message) {
        log.info("Message {} received", message);
        emailService.sendEmail(message);
    }
}
