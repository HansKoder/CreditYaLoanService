package org.pragma.creditya.mongo.helper;

import org.pragma.creditya.mongo.mapper.CustomMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import static org.springframework.data.domain.Example.of;

public abstract class AdapterOperations<E, D, I, R extends ReactiveCrudRepository<D, I> & ReactiveQueryByExampleExecutor<D>> {

    protected R repository;
    protected CustomMapper<E, D> mapper;
    private final Function<D, E> toEntityFn;

    private final Logger logger = LoggerFactory.getLogger(AdapterOperations.class.getName());

    @SuppressWarnings("unchecked")
    protected AdapterOperations(R repository, CustomMapper<E, D> mapper, Function<D, E> toEntityFn) {
        this.repository = repository;
        this.mapper = mapper;
        this.toEntityFn = toEntityFn;
    }

    public Mono<E> save(E entity) {
        return Mono.just(entity)
                .doOnSuccess(e -> logger.info("[infra.mongodb.helper] (save) before map to data, Entity: {}", e))
                .map(this::toData)
                .doOnSuccess(e -> logger.info("[infra.mongodb.helper] (save) after map to data, Data: {}", e))
                .flatMap(this::saveData)
                .map(this::toEntity);
    }

    public Flux<E> saveAll(Flux<E> entities) {
        return doQueryMany(repository.saveAll(entities.map(this::toData)));
    }

    public Mono<E> findById(I id) {
        return doQuery(repository.findById(id));
    }

    public Flux<E> findByExample(E entity) {
        return doQueryMany(repository.findAll(of(toData(entity))));
    }

    public Mono<Void> deleteById(I id) {
        return repository.deleteById(id);
    }

    public Flux<E> findAll() {
        return doQueryMany(repository.findAll());
    }

    protected Mono<E> doQuery(Mono<D> query) {
        return query.map(this::toEntity);
    }

    protected Flux<E> doQueryMany(Flux<D> query) {
        return query.map(this::toEntity);
    }

    protected Mono<D> saveData(D data) {
        return repository.save(data);
    }

    protected D toData(E entity) {
        logger.info("[infra.mongodb.helper] (toData) payload: {}", entity);
        return mapper.toData(entity);
    }

    protected E toEntity(D data) {
        logger.info("[infra.mongodb.helper] (toEntity) payload: {}", data);
        return toEntityFn.apply(data);
    }

}
