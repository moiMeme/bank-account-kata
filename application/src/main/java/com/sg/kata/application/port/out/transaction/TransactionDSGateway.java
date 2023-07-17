package com.sg.kata.application.port.out.transaction;

import com.sg.kata.application.port.out.common.DataSourceGatewayPort;
import com.sg.kata.model.transaction.Transaction;

import java.time.LocalDateTime;
import java.util.Set;

public interface TransactionDSGateway extends DataSourceGatewayPort<Transaction, Integer> {
    Set<Transaction> findByAccountAndDate(String accountNumber, LocalDateTime fromDate, LocalDateTime toDate);
}
