package com.sg.kata.model.account;

public enum AccountType {

    CLASSIC(123456), SAVING(654321), INTERNAL_DEPOSIT(111111), INTERNAL_WITHDRAWAL(222222);

    private final int type;

    AccountType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
