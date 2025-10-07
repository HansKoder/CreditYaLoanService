package org.pragma.creditya.usecase.command;

public record ResolveApplicationLoanCommand(
        String loanId,
        String decision,
        String reason,
        String resolutionType
) { }
