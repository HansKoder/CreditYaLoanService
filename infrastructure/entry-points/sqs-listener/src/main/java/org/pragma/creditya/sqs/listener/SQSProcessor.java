package org.pragma.creditya.sqs.listener;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.sqs.listener.helper.SQSSerializerHelper;
import org.pragma.creditya.usecase.command.ResolveApplicationLoanCommand;
import org.pragma.creditya.usecase.service.ILoanApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class SQSProcessor implements Function<Message, Mono<Void>> {
    private final ILoanApplicationService service;
    private final SQSSerializerHelper serializerHelper;
    private final Logger logger = LoggerFactory.getLogger(SQSProcessor.class);

    @Override
    public Mono<Void> apply(Message message) {
        logger.info("[infra.entrypoint.sqs-listener] (apply) payload=[ message.body:{} ]", message.body());
        ResolveApplicationLoanCommand command = serializerHelper.deserialize(message.body());
        return service.resolutionApplicationLoan(command)
                .doOnSuccess(response -> logger.info("[infra.entrypoint.sqs-listener] (apply) success process resolution application, response=[ loan:{} ]", response))
                .doOnError(err -> logger.info("[infra.entrypoint.sqs-listener] (apply) error process resolution application, response=[ errorMessage:{} ]", err.getMessage()))
                .then();
    }
}
