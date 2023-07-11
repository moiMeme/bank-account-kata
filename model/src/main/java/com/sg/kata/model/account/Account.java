package com.sg.kata.model.account;

import com.sg.kata.model.customer.CustomerId;
import com.sg.kata.model.soa.StatementOfAccount;
import com.sg.kata.model.transaction.Transaction;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class Account {

    private AccountId accountNumber;
    private CustomerId customerId;
    private BigDecimal balance;
    private Currency currency;
    private final Set<StatementOfAccount> soa = new TreeSet<>();

    public synchronized void addTransaction(Transaction transaction) {
        AccountId creditAccountNumber = transaction.getCreditAccount();
        AccountId debitAccountNumber = transaction.getDebitAccount();
        if (Objects.equals(this.accountNumber, creditAccountNumber)) {
            balance = balance.add(transaction.getAmount());
        } else {
            if (Objects.equals(this.accountNumber, debitAccountNumber)) {
                balance = balance.subtract(transaction.getAmount());
            } else {
                throw new IllegalArgumentException("Adding transaction not allowed for " + transaction.getTransactionId().value());
            }
        }
        soa.add(new StatementOfAccount(this.accountNumber, transaction.getTrsDate(), transaction.getTrsType().getDescription(), transaction.getAmount()));
    }

    public AccountId getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(AccountId accountNumber) {
        this.accountNumber = accountNumber;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public void setCustomerId(CustomerId customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Set<StatementOfAccount> getSoa() {
        return soa;
    }
}
