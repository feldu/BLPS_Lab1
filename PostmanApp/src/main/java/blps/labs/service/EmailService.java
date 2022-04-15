package blps.labs.service;

import blps.labs.message.model.AddReviewMessage;
import blps.labs.message.model.CheckReviewMessage;
import blps.labs.message.model.SpamMessage;
import blps.labs.message.model.SpamMessageUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(AddReviewMessage reviewMessage) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(reviewMessage.getEmail());
            msg.setSubject("Отзыв Wrooom.ru-feldme");
            String text = String.format("Здравствуйте, %s!\n\nВаш отзыв №%d на машину %s успешно добавлен.\n" +
                            "Он будет виден другим пользователям после того, как пройдёт модерацию.\n\n" +
                            "С уважением, команда Wroom.ru-feldme.",
                    reviewMessage.getName(), reviewMessage.getReviewId(), reviewMessage.getCarModel());
            msg.setText(text);
            javaMailSender.send(msg);
            log.info("Message to {} sent.", reviewMessage.getEmail());
        } catch (Exception ignored) {
            log.error("Sending message error");
        }
    }

    public void sendEmail(CheckReviewMessage reviewMessage) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(reviewMessage.getEmail());
            msg.setSubject("Отзыв Wrooom.ru-feldme");
            String firstPart = String.format("Здравствуйте, %s!\n\nВаш отзыв №%d на машину %s проверен модератором.\n",
                    reviewMessage.getName(), reviewMessage.getReviewId(), reviewMessage.getCarModel());
            String conclusion = reviewMessage.isApproved() ? "Теперь он опубликован и может быть виден другим пользователям сообщества.\n\n"
                    : "К сожалению, он не удовлетворяет правилам сообщества, попробуйте его отредактировать перед следующей отправкой.\n\n";
            String moderMessage = reviewMessage.getMessage() != null ? String.format("Сообщение от модератора:\n%s.\n\n", reviewMessage.getMessage()) : "";
            String footer = "С уважением, команда Wroom.ru-feldme.";
            String text = firstPart + conclusion + moderMessage + footer;
            msg.setText(text);
            javaMailSender.send(msg);
            log.info("Message to {} sent.", reviewMessage.getEmail());
        } catch (Exception ignored) {
            log.error("Sending message error");
        }
    }

    public void sendEmail(SpamMessage spamMessage) {
        try {
            for (String email : spamMessage.getSubscribersEmails()) {
                SimpleMailMessage msg = new SimpleMailMessage();
                msg.setTo(email);
                msg.setSubject("Отзыв Wrooom.ru-feldme");
                String firstPart = "Доброго времени суток!\n\nПредставляем пятёрку новейших отзывов с сайта wroom.ru.\n\n";
                StringBuilder reviews = new StringBuilder();
                for (SpamMessageUnit messageUnit : spamMessage.getSpam()) {
                    reviews.append(String.format("Отзыв №%d от автора %s: автомобиль марки %s", messageUnit.getId(), messageUnit.getAuthor(), messageUnit.getCar())).append("\n");
                }
                String secondPart = "\nОзнакомиться с этими и многими другими отзывами можно на нашем сайте www.wroom.ru/story\n\n";
                String footer = "С уважением, команда Wroom.ru-feldme.";
                String ps = "\n\n\n\n* Для того, чтобы отписаться от рассылки удалите учетную запись";
                String text = firstPart + reviews + secondPart + footer + ps;
                msg.setText(text);
                javaMailSender.send(msg);
            }
            log.info("Spam sent to {} users", spamMessage.getSubscribersEmails().size());
        } catch (Exception ignored) {
            log.error("Sending message error");
        }
    }
}
