package org.pragma.creditya.asynceventbus.projection;

import org.pragma.creditya.asynceventbus.projection.strategy.ProjectionStrategy;
import org.pragma.creditya.asynceventbus.projection.strategy.ProjectionStrategyFactory;
import org.pragma.creditya.model.loan.bus.EventBus;
import org.pragma.creditya.model.loan.bus.EventHandler;
import org.pragma.creditya.model.loan.event.LoanEvent;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;

import java.util.Optional;

@Component
public class LoanEventHandler implements EventHandler {

    private final ProjectionStrategyFactory strategyFactory;

    public LoanEventHandler(ProjectionStrategyFactory strategyFactory, EventBus eventBus) {
        this.strategyFactory = strategyFactory;

        eventBus.subscribe(LoanEvent.class)
                .publishOn(Schedulers.parallel())
                .subscribe(this::onEvent);
    }

    @Override
    public void onEvent(LoanEvent event) {
        // implement strategy using event as parameter
        Optional<ProjectionStrategy> projectionStrategy = strategyFactory.getStrategy(event);
        if (projectionStrategy.isEmpty())
            return;

        projectionStrategy
                .get()
                .replicate(event);
    }

}
