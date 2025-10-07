package org.pragma.creditya.asynceventbus.projection.strategy;

import org.pragma.creditya.model.loan.event.LoanEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProjectionStrategyFactory {

    private final List<ProjectionStrategy> strategies;

    public ProjectionStrategyFactory(List<ProjectionStrategy> strategies) {
        System.out.println("[infra.bus.projection] (strategyFactory) construct, payload=[ strategies:{" + strategies + "} ]");
        this.strategies = List.copyOf(strategies);
    }

    public Optional<ProjectionStrategy> getStrategy(LoanEvent event) {
        System.out.println("[infra.bus.projection] (strategyFactory) getStrategy, payload=[ event:{" + event + "} ]");
        return strategies.stream()
                .filter(s -> s.shouldBeExecuted(event).equals(Boolean.TRUE))
                .findFirst();
    }

}
