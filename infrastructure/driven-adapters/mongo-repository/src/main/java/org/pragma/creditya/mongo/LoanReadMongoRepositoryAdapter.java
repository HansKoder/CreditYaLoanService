package org.pragma.creditya.mongo;

import org.pragma.creditya.model.loanread.LoanRead;
import org.pragma.creditya.model.loanread.query.LoanQuery;
import org.pragma.creditya.mongo.collection.LoanReadCollection;
import org.pragma.creditya.mongo.helper.AdapterOperations;
import org.pragma.creditya.mongo.mapper.LoanReadCustomMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class LoanReadMongoRepositoryAdapter extends AdapterOperations<LoanRead, LoanReadCollection, String, LoanReadMongoDBRepository>
implements org.pragma.creditya.model.loanread.gateways.LoanReadRepository
{

    private final Logger logger = LoggerFactory.getLogger(LoanReadMongoRepositoryAdapter.class);

    public LoanReadMongoRepositoryAdapter(LoanReadMongoDBRepository repository, LoanReadCustomMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, mapper::toEntity/* change for domain model */);
    }


    @Override
    public Mono<LoanRead> saveLoanRead(LoanRead read) {
        logger.info("[infra.mongodb] (saveLoanRead) payload: {}", read);
        return this.save(read)
                .doOnSuccess(v -> logger.info("[infra.mongodb] (adapter.saveLoanRead) Loan Read was persisted with successful, payload: {}", v))
                .doOnError(e -> logger.error("[infra.mongodb] (adapter.saveLoanRead) Loan Read was not persisted with successful, payload: readModel:{}, errorMessage:{}", read, e.getMessage()));
    }

    @Override
    public Flux<LoanRead> getLoan(LoanQuery query) {
        LoanReadCollection probe = LoanReadCollection.builder()
                .document(query.document())
                .status(query.status())
                .build();

        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<LoanReadCollection> example = Example.of(probe, matcher);

        Pageable pageable = PageRequest.of(query.pagination().page(), query.pagination().size());

        // return repository.findAll().map(this::toEntity);

        return repository.findBy(example, q -> q.page(pageable))
                .flatMapMany(pageResult -> Flux.fromIterable(pageResult.getContent()))
                .map(this::toEntity);
    }
}
