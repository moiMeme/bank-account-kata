package com.sg.kata.model.transaction.factory;

import com.sg.kata.model.account.AccountId;
import com.sg.kata.model.transaction.Transaction;
import com.sg.kata.model.transaction.TransactionId;
import com.sg.kata.model.transaction.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Currency;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class CommonTransactionFactory implements TransactionFactory {

    @Override
    public Transaction create(AccountId account, BigDecimal amount, TransactionType type, Currency currency) {
        TransactionId transactionId = new TransactionId(ThreadLocalRandom.current().nextInt(1_000_000));
        return create(transactionId, account, amount, type, currency, LocalDateTime.now(ZoneOffset.UTC));
    }

    @Override
    public Transaction create(AccountId account, BigDecimal amount, TransactionType type, Currency currency, LocalDateTime trsDate) {
        TransactionId transactionId = new TransactionId(ThreadLocalRandom.current().nextInt(1_000_000));
        return create(transactionId, account, amount, type, currency, trsDate);
    }

    @Override
    public Transaction create(TransactionId transactionId, AccountId account, BigDecimal amount, TransactionType type, Currency currency, LocalDateTime trsDate) {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(transactionId);
        transaction.setTrsType(type);
        transaction.setTrsDate(trsDate);
        transaction.setAmount(amount);
        transaction.setCurrency(currency);
        AccountId internalAccount  = Optional.ofNullable(type).map(TransactionType::getInternalAccount).orElse(account);
        String signe = Optional.ofNullable(type).map(TransactionType::getSigne).orElse("C");
        if ("D".equals(signe)) {
            transaction.setCreditAccount(internalAccount);
            transaction.setDebitAccount(account);
        } else {
            transaction.setCreditAccount(account);
            transaction.setDebitAccount(internalAccount);
        }

        return transaction;
    }

}
