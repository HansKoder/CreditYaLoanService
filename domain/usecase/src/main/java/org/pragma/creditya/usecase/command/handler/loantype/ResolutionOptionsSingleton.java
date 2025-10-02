package org.pragma.creditya.usecase.command.handler.loantype;

import org.pragma.creditya.model.loantype.valueobject.ResolutionType;

import java.util.Set;

public class ResolutionOptionsSingleton {

    private static final Set<ResolutionType> RESOLUTION_OPTIONS;

    static {
        RESOLUTION_OPTIONS = Set.of(ResolutionType.MANUAL_DECISION, ResolutionType.SELF_DECISION);
    }

    private ResolutionOptionsSingleton() {
    }

    public static Set<ResolutionType> getInstance() {
        return RESOLUTION_OPTIONS;
    }


}
