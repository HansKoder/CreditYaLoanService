package org.pragma.creditya.usecase.query.handler.report;

import reactor.core.publisher.Mono;

public interface IReportQuery {
    Mono<Void> updateReport ();
}
