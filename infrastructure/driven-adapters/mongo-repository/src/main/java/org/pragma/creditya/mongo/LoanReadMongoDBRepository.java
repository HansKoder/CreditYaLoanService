package org.pragma.creditya.mongo;

import org.pragma.creditya.mongo.collection.LoanReadCollection;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;

public interface LoanReadMongoDBRepository extends ReactiveMongoRepository<LoanReadCollection, String>, ReactiveQueryByExampleExecutor<LoanReadCollection> {
}
