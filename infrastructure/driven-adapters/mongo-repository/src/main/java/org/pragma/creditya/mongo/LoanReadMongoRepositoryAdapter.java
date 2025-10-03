package org.pragma.creditya.mongo;

import org.pragma.creditya.model.customer.valueobject.Document;
import org.pragma.creditya.model.query.LoanRead;
import org.pragma.creditya.model.query.gateways.LoanReadRepository;
import org.pragma.creditya.model.query.valueobject.LoanQuery;
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

import java.util.UUID;

@Repository
public class LoanReadMongoRepositoryAdapter extends AdapterOperations<LoanRead, LoanReadCollection, String, LoanReadMongoDBRepository>
implements LoanReadRepository
{

    private final Logger logger = LoggerFactory.getLogger(LoanReadMongoRepositoryAdapter.class);

    public LoanReadMongoRepositoryAdapter(LoanReadMongoDBRepository repository, LoanReadCustomMapper mapper) {
        super(repository, mapper, mapper::toEntity);
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
        logger.info("[infra.mongodb] (getLoan) payload: [ query:{} ]", query);
        LoanReadCollection probe = LoanReadCollection.builder()
                .document(query.document())
                .status(query.status())
                .build();

        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<LoanReadCollection> example = Example.of(probe, matcher);

        Pageable pageable = PageRequest.of(query.pagination().page(), query.pagination().size());

        return repository.findBy(example, q -> q.page(pageable))
                .flatMapMany(pageResult -> Flux.fromIterable(pageResult.getContent()))
                .map(this::toEntity);
    }

    @Override
    public Mono<LoanRead> getLoanByAggregateId(UUID aggregateId) {
        logger.info("[infra.mongodb] (getLoanByAggregateId) (step 01) payload: [ aggregateId:{} ]", aggregateId.toString());

        LoanReadCollection example = LoanReadCollection.builder()
                .loanId(aggregateId)
                .build();

        return repository.findOne(Example.of(example))
                .map(this::toEntity)
                .doOnError(er -> logger.error("[infra.mongodb] (getLoanByAggregateId) unexpected error, Error=[ message:{}]", er.getMessage()))
                .doOnSuccess(response -> logger.info("[infra.mongodb] (getLoanByAggregateId) success get loan read from mongo replication, payload [ aggregateId={}, response={}]", aggregateId, response));
    }

    @Override
    public Flux<LoanRead> getActiveDebts(Document document) {
        logger.info("[infra.mongodb] (getActiveDebts) (step 01) payload: [ document:{} ]", document);

        LoanReadCollection probe = LoanReadCollection.builder()
                .document(document.getValue())
                .status("APPROVED")
                .build();

        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<LoanReadCollection> example = Example.of(probe, matcher);

        return repository.findAll(example)
                .map(this::toEntity);
    }
}

