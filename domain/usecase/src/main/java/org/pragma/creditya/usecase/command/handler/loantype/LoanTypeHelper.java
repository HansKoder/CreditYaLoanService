package org.pragma.creditya.usecase.command.handler.loantype;

import org.pragma.creditya.model.loan.exception.LoanDomainException;
import org.pragma.creditya.model.loantype.exception.LoanTypeDomainException;
import org.pragma.creditya.model.loantype.valueobject.ResolutionType;
import reactor.core.publisher.Mono;

public class LoanTypeHelper {

    public Mono<ResolutionType> checkResolutionType (String resolution) {
        if (resolution == null || resolution.isBlank())
            return Mono.error(new LoanTypeDomainException("Resolution must be mandatory"));

        if (ResolutionOptionsSingleton.getInstance().contains(ResolutionType.valueOf(resolution)))
            return Mono.just(ResolutionType.valueOf(resolution));

        return Mono.error(new LoanDomainException("Unknown resolution type"));
    }

}
