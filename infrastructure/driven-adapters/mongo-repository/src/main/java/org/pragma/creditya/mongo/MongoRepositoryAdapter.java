package org.pragma.creditya.mongo;

import org.pragma.creditya.model.loanread.LoanRead;
import org.pragma.creditya.model.loanread.gateways.LoanReadRepository;
import org.pragma.creditya.mongo.collection.LoanReadCollection;
import org.pragma.creditya.mongo.helper.AdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class MongoRepositoryAdapter extends AdapterOperations<LoanRead, LoanReadCollection, String, MongoDBRepository>
implements LoanReadRepository
{

    public MongoRepositoryAdapter(MongoDBRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, LoanRead.class/* change for domain model */));
    }


    @Override
    public Mono<Void> saveLoanRead(LoanRead read) {
        return repository.save(this.toData(read))
                .then();
    }

    @Override
    public Flux<LoanRead> getLoan() {
        return null;
    }
}
