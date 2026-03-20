package com.redteclab.cqrs2.command.application;

import com.redteclab.cqrs2.shared.command.Command;

public record DeactivateOfferCommand(
        String offerId
) implements Command {
}
