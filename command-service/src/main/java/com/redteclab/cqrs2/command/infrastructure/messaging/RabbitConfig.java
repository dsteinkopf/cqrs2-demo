package com.redteclab.cqrs2.command.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String COMMANDS_EXCHANGE = "offer.commands";
    public static final String EVENTS_EXCHANGE = "offer.events";

    public static final String CREATE_QUEUE = "command-service.offer.create";
    public static final String UPDATE_QUEUE = "command-service.offer.update";
    public static final String DEACTIVATE_QUEUE = "command-service.offer.deactivate";

    @Bean
    TopicExchange commandsExchange() {
        return new TopicExchange(COMMANDS_EXCHANGE, true, false);
    }

    @Bean
    TopicExchange eventsExchange() {
        return new TopicExchange(EVENTS_EXCHANGE, true, false);
    }

    @Bean
    Queue createQueue() {
        return new Queue(CREATE_QUEUE, true);
    }

    @Bean
    Queue updateQueue() {
        return new Queue(UPDATE_QUEUE, true);
    }

    @Bean
    Queue deactivateQueue() {
        return new Queue(DEACTIVATE_QUEUE, true);
    }

    @Bean
    Binding createBinding(Queue createQueue, TopicExchange commandsExchange) {
        return BindingBuilder.bind(createQueue).to(commandsExchange).with("offer.create");
    }

    @Bean
    Binding updateBinding(Queue updateQueue, TopicExchange commandsExchange) {
        return BindingBuilder.bind(updateQueue).to(commandsExchange).with("offer.update");
    }

    @Bean
    Binding deactivateBinding(Queue deactivateQueue, TopicExchange commandsExchange) {
        return BindingBuilder.bind(deactivateQueue).to(commandsExchange).with("offer.deactivate");
    }

    @Bean
    MessageConverter messageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
