package com.redteclab.cqrs2.command.application;

import com.redteclab.cqrs2.shared.command.Command;
import java.math.BigDecimal;

public record UpdateOfferCommand(
        String offerId,
        String title,
        BigDecimal price
) implements Command {
}
