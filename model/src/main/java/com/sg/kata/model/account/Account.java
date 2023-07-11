package com.sg.kata.model.account;

import com.sg.kata.model.customer.CustomerId;
import com.sg.kata.model.soa.StatementOfAccount;

import java.util.Currency;
import java.util.Set;
import java.util.TreeSet;

public class Account {

    private AccountId accountNumber;
    private CustomerId customerId;
    private Double balance;
    private Currency currency;
    private final Set<StatementOfAccount> soa = new TreeSet<>();

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

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
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
