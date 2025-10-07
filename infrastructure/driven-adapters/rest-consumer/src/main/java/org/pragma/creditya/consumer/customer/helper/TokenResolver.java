package org.pragma.creditya.consumer.customer.helper;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.consumer.machine.adapter.MachineRestConsumerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TokenResolver {

    private final MachineRestConsumerAdapter machineRestConsumerAdapter;

    private final Logger logger = LoggerFactory.getLogger(TokenResolver.class);

    public Mono<String> resolveToken() {
        return Mono.deferContextual(ctx -> {
            if (ctx.hasKey("token")) {
                String token = ctx.get("token").toString();
                logger.info("[TokenResolver] Token found in context");
                return Mono.just(token);
            }

            logger.info("[TokenResolver] No token found in context, generating machine token...");
            return machineRestConsumerAdapter.authenticationMachine();
        });
    }

}
