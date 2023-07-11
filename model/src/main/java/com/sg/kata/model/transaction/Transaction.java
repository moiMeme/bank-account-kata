package com.sg.kata.model.transaction;

import com.sg.kata.model.account.AccountId;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction implements Comparable<Transaction> {
    private TransactionId transactionId;
    private AccountId creditAccount;
    private AccountId debitAccount;
    private TransactionType trsType;
    private BigDecimal amount;
    private LocalDateTime trsDate;

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

    @Override
    public int compareTo(Transaction transaction) {
        return transaction.getTrsDate().compareTo(this.trsDate);
    }
}
