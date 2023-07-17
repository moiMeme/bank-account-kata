package com.sg.kata.model.account;

import com.sg.kata.model.common.validation.NotNull;
import com.sg.kata.model.common.validation.Valid;
import com.sg.kata.model.common.validation.Validator;
import com.sg.kata.model.customer.CustomerId;

import java.math.BigDecimal;
import java.util.Currency;

public class Account extends Validator {
    @NotNull
    @Valid(handler = AccountNumberValidation.class)
    private AccountId accountNumber;
    @NotNull
    private CustomerId customerId;
    @NotNull
    private BigDecimal balance;
    @NotNull
    private Currency currency;
    @NotNull
    private AccountType accountType;

    /*public synchronized void addTransaction(Transaction...transactions) {
        Arrays.stream(transactions).parallel().forEach(this::addTransaction);
    }

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
    }*/

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

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
}
