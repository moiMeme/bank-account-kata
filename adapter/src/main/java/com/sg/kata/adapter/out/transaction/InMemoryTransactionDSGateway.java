package com.sg.kata.adapter.out.transaction;

import com.sg.kata.application.port.out.transaction.TransactionDSGateway;
import com.sg.kata.model.account.AccountId;
import com.sg.kata.model.transaction.Transaction;
import com.sg.kata.model.transaction.TransactionId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryTransactionDSGateway implements TransactionDSGateway {

    private final Map<TransactionId, Transaction> transactions = new ConcurrentHashMap<>(50);
    private int size;

    @Override
    public Transaction save(Transaction data) {
        TransactionId transactionId = data.getTransactionId();
        size++;
        if (Objects.isNull(transactionId)) {
            data.setTransactionId(new TransactionId(size));
        }
        transactions.put(data.getTransactionId(), data);
        return data;
    }

    @Override
    public Optional<Transaction> getById(TransactionId transactionId) {
        return Optional.ofNullable(transactions.get(transactionId));
    }

    @Override
    public Set<Transaction> findByAccountAndDate(String accountNumber, LocalDateTime fromDate, LocalDateTime toDate) {
        return transactions.values().parallelStream()
                .filter(transaction -> filter(transaction, new AccountId(accountNumber), fromDate, toDate))
                .collect(Collectors.toCollection(TreeSet::new));
    }

    private boolean filter(Transaction transaction, AccountId accountId, LocalDateTime fromDate, LocalDateTime toDate) {
        if (transaction.getCreditAccount().equals(accountId) || transaction.getDebitAccount().equals(accountId)) {
            return !transaction.getTrsDate().isBefore(fromDate) && !transaction.getTrsDate().isAfter(toDate);
        }
        return false;
    }

    @Override
    public BigDecimal findBalanceByAccountAndDate(AccountId accountId, LocalDateTime beforeThisDate, boolean inclusive) {
        return transactions.values().parallelStream()
                .filter(transaction -> filter(transaction, accountId, beforeThisDate, inclusive))
                .map(this::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private boolean filter(Transaction transaction, AccountId accountId, LocalDateTime beforeThisDate, boolean inclusive) {
        if (transaction.getCreditAccount().equals(accountId) || transaction.getDebitAccount().equals(accountId)) {
            if (inclusive) {
                return !transaction.getTrsDate().isAfter(beforeThisDate);
            }
            return transaction.getTrsDate().isBefore(beforeThisDate);
        }
        return false;
    }

    private BigDecimal amount(Transaction transaction) {
        String signe = transaction.getTrsType().getSigne();
        if ("D".equals(signe)) {
            return transaction.getAmount().negate();
        } else {
            return transaction.getAmount();
        }
    }

}
