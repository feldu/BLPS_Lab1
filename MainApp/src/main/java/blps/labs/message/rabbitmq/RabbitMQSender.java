package blps.labs.message.rabbitmq;

import blps.labs.message.model.AddReviewMessage;
import blps.labs.message.model.CheckReviewMessage;
import blps.labs.message.model.SpamMessageUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RabbitMQSender {
    private final RabbitTemplate rabbitTemplate;
    private final Exchange appExchange;
    @Value("${rabbitmq.add-review-routingKey}")
    private String addReviewRoutingKey;
    @Value("${rabbitmq.check-review-routingKey}")
    private String checkReviewRoutingKey;
    @Value("${rabbitmq.send-spam-routingKey}")
    private String sendSpamRoutingKey;

    public RabbitMQSender(RabbitTemplate rabbitTemplate, Exchange appExchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.appExchange = appExchange;
    }


    public void send(AddReviewMessage message) {
        rabbitTemplate.convertAndSend(appExchange.getName(), addReviewRoutingKey, message);
        log.info("Message: {} sent", message);
    }

    public void send(CheckReviewMessage message) {
        rabbitTemplate.convertAndSend(appExchange.getName(), checkReviewRoutingKey, message);
        log.info("Message: {} sent", message);
    }

    public void send(List<SpamMessageUnit> message) {
        rabbitTemplate.convertAndSend(appExchange.getName(), sendSpamRoutingKey, message);
        log.info("Message: {} sent", message);
    }
}
