package com.sg.kata.model.transaction;

public enum TransactionType {

    DEPOSIT("Deposit"), WITHDRAWAL("Withdrawal");

    private final String description;

    TransactionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
