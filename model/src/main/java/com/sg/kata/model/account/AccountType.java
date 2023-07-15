package com.sg.kata.model.account;

public enum AccountType {

    CLASSIC(123456), SAVING(654321);

    private final int type;

    AccountType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
