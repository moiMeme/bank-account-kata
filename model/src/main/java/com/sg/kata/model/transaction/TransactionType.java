package com.sg.kata.model.transaction;

import com.sg.kata.model.account.AccountId;
import com.sg.kata.model.account.AccountType;

import java.util.function.Supplier;

public enum TransactionType {

    DEPOSIT("Deposit", "C", AccountType.INTERNAL_DEPOSIT, () -> new AccountId("00000010000EUR111111")),
    WITHDRAWAL("Withdrawal", "D", AccountType.INTERNAL_WITHDRAWAL, () -> new AccountId("00000010000EUR222222"));

    private final String description;
    private final Supplier<AccountId> accountSupplier;
    private final String signe;
    private final AccountType type;

    TransactionType(String description, String signe, AccountType type, Supplier<AccountId> accountSupplier) {
        this.description = description;
        this.signe = signe;
        this.type = type;
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

    public AccountType getAccountType() {
        return this.type;
    }
}
