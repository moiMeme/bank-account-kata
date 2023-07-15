package com.sg.kata.model.transaction;

import com.sg.kata.model.account.AccountId;

import java.util.function.Supplier;

public enum TransactionType {

    DEPOSIT("Deposit", "C", "111111", () -> new AccountId("10000EUR111111ABCDEF")),
    WITHDRAWAL("Withdrawal", "D", "222222", () -> new AccountId("10000EUR222222ABCDEF"));

    private final String description;
    private final Supplier<AccountId> accountSupplier;
    private final String signe;

    private final String gl;

    TransactionType(String description, String signe, String gl, Supplier<AccountId> accountSupplier) {
        this.description = description;
        this.signe = signe;
        this.gl = gl;
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

    public String getGl() {
        return gl;
    }
}
