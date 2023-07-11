package com.sg.kata.model.transaction;

import com.sg.kata.model.account.AccountId;

import java.util.function.Supplier;

public enum TransactionType {

    DEPOSIT("Deposit", "C", () -> new AccountId("1234000000")),
    WITHDRAWAL("Withdrawal", "D", () -> new AccountId("4321000000"));

    private final String description;
    private final Supplier<AccountId> accountSupplier;
    private final String signe;

    TransactionType(String description, String signe, Supplier<AccountId> accountSupplier) {
        this.description = description;
        this.signe = signe;
        this.accountSupplier = accountSupplier;
    }

    public String getDescription() {
        return description;
    }

    public AccountId getInternalAccount() {
        return this.accountSupplier.get();
    }

    public String getSigne() {
        return signe;
    }
}
