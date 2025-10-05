package org.pragma.creditya.jobs.outbox.strategy;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.usecase.outbox.LoanOutboxMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@Component
public class OutboxStrategyDispatcher {

    private final List<OutboxProcessStrategy> strategies;
    private final Logger logger = LoggerFactory.getLogger(OutboxStrategyDispatcher.class);

    public Mono<Void> dispatch(LoanOutboxMessage outboxMessage) {
        logger.info("[infra.entrypoint.scheduler.dispatcher] (01) Received outbox message=[ {} ]", outboxMessage);

        return Flux.fromIterable(strategies)
                .flatMap(strategy -> strategy.apply(outboxMessage)
                        .filter(Boolean::booleanValue)
                        .flatMap(result -> {
                            logger.info("[infra.entrypoint.scheduler.dispatcher] (02) Strategy [{}] selected", strategy.getClass().getSimpleName());
                            return strategy.execute(outboxMessage);
                        })
                )
                .next()
                .switchIfEmpty(Mono.fromRunnable(() ->
                        logger.warn("[infra.entrypoint.scheduler.dispatcher] (03) No strategy matched for outbox type=[ {} ]", outboxMessage.getType())
                ));
    }

}
