package org.pragma.creditya.model.loan.gateways;

import reactor.core.publisher.Mono;

public interface UserInfoRepository {

    Mono<String> getUsername();

}
