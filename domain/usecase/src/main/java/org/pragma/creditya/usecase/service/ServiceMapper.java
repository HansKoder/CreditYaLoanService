package org.pragma.creditya.usecase.service;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.loan.gateways.UserInfoRepository;
import org.pragma.creditya.model.loantype.valueobject.ResolutionType;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ServiceMapper {

    private final UserInfoRepository userInfoRepository;

    public Mono<String> getDecidedBy (ResolutionType resolutionType) {
        if (!resolutionType.equals(ResolutionType.SELF_DECISION))
            return userInfoRepository.getUsername();

        return Mono.just("SELF-DECISION");
    }

}
