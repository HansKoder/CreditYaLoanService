package org.pragma.creditya.usecase.command;

public record DecisionLoanCommand(
        String loanId,
        String decision,
        String reason,
        Boolean isSelfDecision
) { }
