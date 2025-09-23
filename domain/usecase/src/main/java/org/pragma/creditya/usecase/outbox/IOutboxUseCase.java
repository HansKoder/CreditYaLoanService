package org.pragma.creditya.usecase.outbox;

import org.pragma.creditya.model.loan.event.LoanEvent;
import reactor.core.publisher.Mono;

public interface IOutboxUseCase {

    Mono<Void> execute (LoanEvent event);

}
