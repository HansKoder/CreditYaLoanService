package org.pragma.creditya.mongo;

import org.pragma.creditya.model.loanread.LoanRead;
import org.pragma.creditya.model.loanread.gateways.LoanReadRepository;
import org.pragma.creditya.mongo.collection.LoanReadCollection;
import org.pragma.creditya.mongo.helper.AdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class MongoRepositoryAdapter extends AdapterOperations<LoanRead, LoanReadCollection, String, MongoDBRepository>
implements LoanReadRepository
{

    private final Logger logger = LoggerFactory.getLogger(MongoRepositoryAdapter.class);

    public MongoRepositoryAdapter(MongoDBRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, LoanRead.class/* change for domain model */));
    }


    @Override
    public Mono<LoanRead> saveLoanRead(LoanRead read) {
        logger.info("[infra.mongodb] (saveLoanRead) payload: {}", read);
        return this.save(read)
                .log()
                .doOnSuccess(v -> logger.info("[infra.mongodb] (adapter.saveLoanRead) Loan Read was persisted with successful, payload: {}", v))
                .doOnError(e -> logger.error("[infra.mongodb] (adapter.saveLoanRead) Loan Read was not persisted with successful, payload: readModel:{}, errorMessage:{}", read, e.getMessage()));
    }

    @Override
    public Flux<LoanRead> getLoan() {
        return this.repository.findAll().map(this::toEntity);
    }
}
