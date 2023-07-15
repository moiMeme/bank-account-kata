package com.sg.kata.application.port.in.soa;

import com.sg.kata.model.soa.StatementOfAccount;

import java.time.LocalDateTime;
import java.util.Set;

public interface GetStatementOfAccountUseCase {

    Set<StatementOfAccount> statementOfAccount(String accountNumber, LocalDateTime fromDate, LocalDateTime toDate);
}
