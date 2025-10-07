package org.pragma.creditya.consumer.machine.adapter;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.consumer.machine.config.MachineRestConsumerProperties;
import org.pragma.creditya.consumer.machine.rest.MachineRestConsumer;
import org.pragma.creditya.consumer.machine.rest.payload.AuthenticationMachinePayload;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MachineRestConsumerAdapter  {

    private final MachineRestConsumer restConsumer;
    private final MachineRestConsumerProperties properties;

    public Mono<String> authenticationMachine() {
        return restConsumer
                .authenticationMachine(new AuthenticationMachinePayload(properties.id(), properties.secret()));
    }

}
