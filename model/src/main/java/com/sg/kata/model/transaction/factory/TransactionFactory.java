package com.sg.kata.model.transaction.factory;

import com.sg.kata.model.account.AccountId;
import com.sg.kata.model.transaction.Transaction;
import com.sg.kata.model.transaction.TransactionId;
import com.sg.kata.model.transaction.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

public interface TransactionFactory {

    Transaction create(AccountId account, BigDecimal amount, TransactionType type, Currency currency);
    Transaction create(AccountId account, BigDecimal amount, TransactionType type, Currency currency, LocalDateTime trsDate);
    Transaction create(TransactionId transactionId, AccountId account, BigDecimal amount, TransactionType type, Currency currency, LocalDateTime trsDate);

}
