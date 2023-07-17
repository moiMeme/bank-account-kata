package com.sg.kata.application.port.in.transaction;

import com.sg.kata.model.transaction.Transaction;
import com.sg.kata.model.transaction.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

public interface CreateTransactionUseCase {

    Transaction newTransaction(String accountNumber, TransactionType trsType, BigDecimal amount, Currency currency);

    Transaction newTransaction(String accountNumber, TransactionType trsType, BigDecimal amount, Currency currency, LocalDateTime trsDate);

}
