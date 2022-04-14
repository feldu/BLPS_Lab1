package blps.labs.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.add-review-queue}")
    private String addReviewQueueName;
    @Value("${rabbitmq.check-review-queue}")
    private String checkReviewQueueName;
    @Value("${rabbitmq.app-exchange}")
    private String exchange;
    @Value("${rabbitmq.add-review-routingKey}")
    private String addReviewRoutingKey;
    @Value("${rabbitmq.check-review-routingKey}")
    private String checkReviewRoutingKey;

    @Bean
    DirectExchange appExchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    Queue addReviewQueue() {
        return new Queue(addReviewQueueName);
    }

    @Bean
    Queue checkReviewQueue() {
        return new Queue(checkReviewQueueName);
    }

    @Bean
    Binding declareBindingAddReview() {
        return BindingBuilder.bind(addReviewQueue()).to(appExchange()).with(addReviewRoutingKey);
    }

    @Bean
    Binding declareBindingCheckReview() {
        return BindingBuilder.bind(checkReviewQueue()).to(appExchange()).with(checkReviewRoutingKey);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}