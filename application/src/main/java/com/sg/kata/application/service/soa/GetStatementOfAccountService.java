package com.sg.kata.application.service.soa;

import com.sg.kata.application.port.in.soa.GetStatementOfAccountUseCase;
import com.sg.kata.application.port.out.soa.SoaDSGateway;
import com.sg.kata.model.soa.StatementOfAccount;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

public class GetStatementOfAccountService implements GetStatementOfAccountUseCase {

    private final SoaDSGateway soaDSGateway;

    public GetStatementOfAccountService(SoaDSGateway soaDSGateway) {
        this.soaDSGateway = soaDSGateway;
    }

    @Override
    public Set<StatementOfAccount> statementOfAccount(String accountNumber, LocalDateTime fromDate, LocalDateTime toDate) {
        Objects.requireNonNull(accountNumber, "'accountNumber' must not be null");
        Objects.requireNonNull(fromDate, "'fromDate' must not be null");
        Objects.requireNonNull(toDate, "'toDate' must not be null");

        return soaDSGateway.findByAccountAndDate(accountNumber, fromDate, toDate);

    }
}
