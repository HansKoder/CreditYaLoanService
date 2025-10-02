package org.pragma.creditya.outbox.handler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.event.AggregateType;
import org.pragma.creditya.model.loan.event.LoanApplicationSubmittedEvent;
import org.pragma.creditya.model.loan.gateways.OutboxRepository;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;
import org.pragma.creditya.model.outbox.LoanOutboxMessage;
import org.pragma.creditya.usecase.outbox.handler.OutboxHandler;
import org.pragma.creditya.usecase.outbox.payload.NotificationOutboxPayload;
import org.pragma.creditya.usecase.outbox.strategy.OutboxStrategy;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OutboxHandlerTest {

    @InjectMocks
    OutboxHandler outboxHandler;

    @Mock
    OutboxRepository outboxRepository;

    private final String AGGREGATE_ID = "c4fc5172-1cd3-4dfa-aa71-456b50bc9089";

    @Test
    void shouldBeExecutedWithSuccess_outboxProcess () {

        var submittedEventMock = LoanApplicationSubmittedEvent.SubmittedBuilder
                .aSubmittedEvent()
                .aggregateId(UUID.fromString(AGGREGATE_ID))
                .status(LoanStatus.APPROVED)
                .aggregateType(AggregateType.AGGREGATE_LOAN)
                .build();

        var domainMock = Mockito.mock(Loan.class);

        when(domainMock.getUncommittedEvents())
                .thenReturn(List.of(submittedEventMock));

        var matchingStrategy = Mockito.mock(OutboxStrategy.class);
        when(matchingStrategy.apply(any()))
                .thenReturn(Boolean.TRUE);

        var payload = NotificationOutboxPayload.builder()
                .type("EMAIL")
                .subject("Loan Notification")
                .destination("doe@gmail.com")
                .message("Message..")
                .build();

        when(matchingStrategy.handler(any(), any()))
                .thenReturn(Mono.just(payload));

        var noMatchingStrategy = Mockito.mock(OutboxStrategy.class);
        when(noMatchingStrategy.apply(any()))
                .thenReturn(Boolean.FALSE);

        when(outboxRepository.saveOutboxMessage(any(), any()))
                .thenReturn(Mono.empty());

        outboxHandler = new OutboxHandler(outboxRepository, Arrays.asList(noMatchingStrategy, matchingStrategy));

        // Act + Assert con StepVerifier
        StepVerifier.create(outboxHandler.execute(domainMock))
                .verifyComplete();

        verify(matchingStrategy).apply(submittedEventMock);
        verify(matchingStrategy).handler(domainMock, submittedEventMock);

        verify(noMatchingStrategy).apply(submittedEventMock);
        verify(noMatchingStrategy, never()).handler(domainMock, submittedEventMock);

        verify(outboxRepository).saveOutboxMessage(any(LoanOutboxMessage.class), eq(payload));
    }

}
