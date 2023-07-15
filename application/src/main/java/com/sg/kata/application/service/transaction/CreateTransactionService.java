package com.sg.kata.application.service.transaction;

import com.sg.kata.application.port.in.transaction.CreateTransactionUseCase;
import com.sg.kata.application.port.out.account.AccountDSGateway;
import com.sg.kata.application.port.out.transaction.TransactionDSGateway;
import com.sg.kata.model.account.Account;
import com.sg.kata.model.account.AccountId;
import com.sg.kata.model.transaction.Transaction;
import com.sg.kata.model.transaction.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Objects;

public class CreateTransactionService implements CreateTransactionUseCase {

    private final AccountDSGateway accountDSGateway;
    private final TransactionDSGateway transactionDSGateway;

    public CreateTransactionService(AccountDSGateway accountDSGateway, TransactionDSGateway transactionDSGateway) {
        this.accountDSGateway = accountDSGateway;
        this.transactionDSGateway = transactionDSGateway;
    }

    @Override
    public Transaction newTransaction(String accountNumber, TransactionType trsType, BigDecimal amount, Currency currency) {
        Objects.requireNonNull(accountNumber, "'accountNumber' must not be null");
        Objects.requireNonNull(trsType, "'trsType' must not be null");
        Objects.requireNonNull(amount, "'amount' must not be null");
        Objects.requireNonNull(currency, "'currency' must not be null");

        Account account = accountDSGateway.getById(accountNumber).orElseThrow(AccountNotFoundException::new);
        if (!account.getCurrency().equals(currency)) {
            throw new IllegalArgumentException("the currency is not allowed for this Account Number " + accountNumber);
        }

        Account internalAccount = accountDSGateway.getById(trsType.getInternalAccount().value()).orElseThrow(AccountNotFoundException::new);

        Transaction transaction = new Transaction();
        transaction.setCurrency(currency);
        transaction.setTrsDate(LocalDateTime.now());
        transaction.setTrsType(trsType);
        transaction.setAmount(amount);
        AccountId debitAccountNumber;
        AccountId creditAccountNumber;
        if ("D".equals(trsType.getSigne())) {
            debitAccountNumber = new AccountId(accountNumber);
            creditAccountNumber = trsType.getInternalAccount();
        } else {
            debitAccountNumber = trsType.getInternalAccount();
            creditAccountNumber = new AccountId(accountNumber);
        }
        transaction.setDebitAccount(debitAccountNumber);
        transaction.setCreditAccount(creditAccountNumber);

        Transaction createdTransaction = transactionDSGateway.save(transaction);

        account.addTransaction(createdTransaction);
        internalAccount.addTransaction(createdTransaction);

        accountDSGateway.save(account);
        accountDSGateway.save(internalAccount);

        return createdTransaction;
    }
}
