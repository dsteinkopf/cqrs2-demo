package com.redteclab.cqrs2.command.infrastructure.messaging;

import com.redteclab.cqrs2.command.application.CreateOfferCommand;
import com.redteclab.cqrs2.command.application.DeactivateOfferCommand;
import com.redteclab.cqrs2.command.application.OfferCommandHandler;
import com.redteclab.cqrs2.command.application.UpdateOfferCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OfferCommandConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(OfferCommandConsumer.class);

    private final OfferCommandHandler commandHandler;

    public OfferCommandConsumer(OfferCommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @RabbitListener(queues = RabbitConfig.CREATE_QUEUE)
    public void onCreateOffer(CreateOfferCommand command) {
        LOG.info("Received CreateOfferCommand: title={}, price={}", command.title(), command.price());
        commandHandler.handle(command);
    }

    @RabbitListener(queues = RabbitConfig.UPDATE_QUEUE)
    public void onUpdateOffer(UpdateOfferCommand command) {
        LOG.info("Received UpdateOfferCommand: offerId={}", command.offerId());
        commandHandler.handle(command);
    }

    @RabbitListener(queues = RabbitConfig.DEACTIVATE_QUEUE)
    public void onDeactivateOffer(DeactivateOfferCommand command) {
        LOG.info("Received DeactivateOfferCommand: offerId={}", command.offerId());
        commandHandler.handle(command);
    }
}
