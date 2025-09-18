package org.pragma.creditya.jobs;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Component
public class OutboxJob {

    public OutboxJob() {
        startJob();
    }

    private void startJob() {

    }

}
