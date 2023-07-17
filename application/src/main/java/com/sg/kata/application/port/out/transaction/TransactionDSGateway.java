package com.sg.kata.application.port.out.transaction;

import com.sg.kata.application.port.out.common.DataSourceGatewayPort;
import com.sg.kata.model.account.AccountId;
import com.sg.kata.model.transaction.Transaction;
import com.sg.kata.model.transaction.TransactionId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public interface TransactionDSGateway extends DataSourceGatewayPort<Transaction, TransactionId> {
    Set<Transaction> findByAccountAndDate(String accountNumber, LocalDateTime fromDate, LocalDateTime toDate);

    BigDecimal findBalanceByAccountAndDate(AccountId accountId, LocalDateTime beforeThisDate, boolean inclusive);
}
