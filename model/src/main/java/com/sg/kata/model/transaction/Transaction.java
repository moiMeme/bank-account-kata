package com.sg.kata.model.transaction;

import com.sg.kata.model.account.AccountId;
import com.sg.kata.model.common.validation.NotNull;
import com.sg.kata.model.common.validation.Valid;
import com.sg.kata.model.common.validation.Validator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

public class Transaction extends Validator implements Comparable<Transaction> {
    @NotNull
    private TransactionId transactionId;
    @NotNull
    private AccountId creditAccount;
    @NotNull
    private AccountId debitAccount;
    @NotNull
    private TransactionType trsType;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private LocalDateTime trsDate;
    @Valid(handler = TransactionCurrencyValidation.class)
    private Currency currency;

    public TransactionId getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(TransactionId transactionId) {
        this.transactionId = transactionId;
    }

    public AccountId getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(AccountId creditAccount) {
        this.creditAccount = creditAccount;
    }

    public AccountId getDebitAccount() {
        return debitAccount;
    }

    public void setDebitAccount(AccountId debitAccount) {
        this.debitAccount = debitAccount;
    }

    public TransactionType getTrsType() {
        return trsType;
    }

    public void setTrsType(TransactionType trsType) {
        this.trsType = trsType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getTrsDate() {
        return trsDate;
    }

    public void setTrsDate(LocalDateTime trsDate) {
        this.trsDate = trsDate;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public int compareTo(Transaction transaction) {
        return transaction.getTrsDate().compareTo(this.trsDate);
    }
}
