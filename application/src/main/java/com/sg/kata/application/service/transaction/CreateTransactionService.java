package com.sg.kata.application.service.transaction;

import com.sg.kata.application.port.in.transaction.CreateTransactionUseCase;
import com.sg.kata.application.port.out.account.AccountDSGateway;
import com.sg.kata.application.port.out.transaction.TransactionDSGateway;
import com.sg.kata.model.account.Account;
import com.sg.kata.model.common.Util;
import com.sg.kata.model.common.exception.CommonException;
import com.sg.kata.model.common.exception.ErrorCode;
import com.sg.kata.model.transaction.Transaction;
import com.sg.kata.model.transaction.TransactionType;
import com.sg.kata.model.transaction.factory.TransactionFactory;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Optional;

public class CreateTransactionService implements CreateTransactionUseCase {

    private final AccountDSGateway accountDSGateway;
    private final TransactionDSGateway transactionDSGateway;
    private final TransactionFactory transactionFactory;

    public CreateTransactionService(AccountDSGateway accountDSGateway, TransactionDSGateway transactionDSGateway, TransactionFactory transactionFactory) {
        this.accountDSGateway = accountDSGateway;
        this.transactionDSGateway = transactionDSGateway;
        this.transactionFactory = transactionFactory;
    }

    @Override
    public Transaction newTransaction(String accountNumber, TransactionType trsType, BigDecimal amount, Currency currency) {
        Util.requireNonNull(accountNumber, "accountNumber");
        Util.requireNonNull(trsType, "trsType");
        Util.requireNonNull(amount, "amount");
        Util.requireNonNull(currency, "currency");

        Account account = accountDSGateway.getById(accountNumber).orElseThrow(() -> new AccountNotFoundException(accountNumber));
        if (account.getBalance().compareTo(amount) < 0 && "D".equals(trsType.getSigne())) {
            throw new CommonException(ErrorCode.EXP009);
        }
        if (!account.getCurrency().equals(currency)) {
            throw new CommonException(ErrorCode.EXP007, accountNumber);
        }
        Account internalAccount = accountDSGateway.getById(trsType.getInternalAccount().value()).orElseThrow(() -> new AccountNotFoundException(trsType.getInternalAccount().value()));

        Transaction transaction = transactionFactory.create(account.getAccountNumber(), amount, trsType, currency);
        Transaction createdTransaction = transactionDSGateway.save(transaction);

        updateAccount(account, createdTransaction);
        updateAccount(internalAccount, createdTransaction);

        accountDSGateway.save(account);
        accountDSGateway.save(internalAccount);

        return createdTransaction;
    }

    private void updateAccount(Account account, Transaction transaction) {
        String signe = Optional.ofNullable(transaction.getTrsType().getSigne()).orElse("C");
        if ("D".equals(signe)) {
            account.setBalance(account.getBalance().subtract(transaction.getAmount()));
        } else {
            account.setBalance(account.getBalance().add(transaction.getAmount()));
        }
    }
}
