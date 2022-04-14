package blps.labs.message.service;

import blps.labs.entity.Review;
import blps.labs.entity.User;
import blps.labs.message.model.AddReviewMessage;
import blps.labs.message.model.CheckReviewMessage;
import blps.labs.message.rabbitmq.RabbitMQSender;
import blps.labs.service.ReviewService;
import blps.labs.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageService {
    private final RabbitMQSender rabbitMQSender;
    private final UserService userService;
    private final ReviewService reviewService;

    @Autowired
    public MessageService(RabbitMQSender rabbitMQSender, UserService userService, ReviewService reviewService) {
        this.rabbitMQSender = rabbitMQSender;
        this.userService = userService;
        this.reviewService = reviewService;
    }

    public void sendMessageWhenReviewAdd(Review review) {
        log.info("Send message about added review to postman");
        try {
            User user = userService.findUserByUsername(review.getAuthorName());
            if (user.getEmail() == null) {
                log.warn("Review belongs to user without email address: message is not sent");
                return;
            }
            rabbitMQSender.send(new AddReviewMessage(review.getId(), user.getEmail(), user.getName(), review.getCar().getCarModel()));
        } catch (UsernameNotFoundException ignored) {
            log.warn("Review belongs to not auth user: message is not sent");
        }
    }

    public void sendMessageWhenReviewChecked(Long id, String message, Boolean approved) {
        log.info("Send message about checked review to postman");
        try {
            Review review = reviewService.findReviewById(id);
            User user = userService.findUserByUsername(review.getAuthorName());
            if (user.getEmail() == null) {
                log.warn("Review belongs to user without email address: message is not sent");
                return;
            }
            rabbitMQSender.send(new CheckReviewMessage(review.getId(), user.getEmail(), user.getName(), review.getCar().getCarModel(), approved, message));
        } catch (UsernameNotFoundException ignored) {
            log.warn("Review belongs to not auth user: message is not sent");
        }
    }
}
