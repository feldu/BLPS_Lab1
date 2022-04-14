package blps.labs.message.jms;

import blps.labs.message.model.AddReviewMessage;
import blps.labs.message.model.CheckReviewMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JmsReceiver {
    @JmsListener(destination = "addReviewQueue")
    public void receiveMessage(@Payload AddReviewMessage message) {
        log.info("Message {} received", message);
    }

    @JmsListener(destination = "checkReviewQueue")
    public void receiveMessage(@Payload CheckReviewMessage message) {
        log.info("Message {} received", message);
    }
}
