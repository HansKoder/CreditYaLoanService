package org.pragma.creditya.usecase.query.report;

import reactor.core.publisher.Mono;

public interface IReportUseCase {
    Mono<Void> updateReport ();
}
