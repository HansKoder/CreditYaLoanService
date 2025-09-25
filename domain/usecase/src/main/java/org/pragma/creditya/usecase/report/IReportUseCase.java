package org.pragma.creditya.usecase.report;

import reactor.core.publisher.Mono;

public interface IReportUseCase {
    Mono<Void> updateReport ();
}
