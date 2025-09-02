package org.pragma.creditya.r2dbc.persistence.loan.repository;

import org.pragma.creditya.r2dbc.persistence.loan.entity.LoanEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface LoanReactiveRepository extends ReactiveCrudRepository<LoanEntity, UUID>, ReactiveQueryByExampleExecutor<LoanEntity> {
}
