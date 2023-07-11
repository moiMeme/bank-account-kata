package com.sg.kata.model.transaction;

import com.sg.kata.model.account.AccountId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.ThreadLocalRandom;

public class TransactionFactoryTest {

    public static Transaction createTransaction(AccountId account, BigDecimal amount, TransactionType type) {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(new TransactionId(ThreadLocalRandom.current().nextInt(1_000_000)));
        transaction.setTrsType(type);
        transaction.setTrsDate(generateRandomDateTime());
        transaction.setAmount(amount);
        if ("D".equals(type.getSigne())) {
            transaction.setCreditAccount(type.getInternalAccount());
            transaction.setDebitAccount(account);
        } else {
            transaction.setCreditAccount(account);
            transaction.setDebitAccount(type.getInternalAccount());
        }

        return transaction;
    }

    public static LocalDateTime generateRandomDateTime() {
        LocalDateTime startDateTime = LocalDateTime.of(2000, 1, 1, 0, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.now();
        long startEpochSeconds = startDateTime.toEpochSecond(ZoneOffset.UTC);
        long endEpochSeconds = endDateTime.toEpochSecond(ZoneOffset.UTC);

        long randomEpochSeconds = ThreadLocalRandom.current().nextLong(startEpochSeconds, endEpochSeconds);

        return LocalDateTime.ofEpochSecond(randomEpochSeconds, 0, ZoneOffset.UTC);
    }
}
