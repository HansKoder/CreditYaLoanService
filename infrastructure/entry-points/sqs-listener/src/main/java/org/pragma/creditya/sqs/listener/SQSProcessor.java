package org.pragma.creditya.sqs.listener;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class SQSProcessor implements Function<Message, Mono<Void>> {
    // private final MyUseCase myUseCase;

    private final Logger logger = LoggerFactory.getLogger(SQSProcessor.class);

    @Override
    public Mono<Void> apply(Message message) {
        logger.info("[infra.entrypoint.sqs-listener] (apply) payload=[ message.body:{} ]", message.body());
        return Mono.empty();
        // return myUseCase.doAny(message.body());
    }
}
