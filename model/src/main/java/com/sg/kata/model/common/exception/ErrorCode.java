package com.sg.kata.model.common.exception;

public enum ErrorCode implements CommonError {

    EXP001("Filed {0} of {1} is null"),
    EXP002("Filed {0} of {1} is empty"),
    EXP003("Field {0} of {1} is not valid"),
    EXP004("Transaction currency is not valid"),
    EXP005("'{}' must not be null"),
    EXP006("Statement of account can't be generated fromDate[{0}] must be before toDate[{1}]"),
    EXP007("the transaction currency is not allowed for this Account Number {0}"),
    EXP008("Account not valid to create a statement of account"),
    EXP009("Debit transaction not allowed"),
    EXP010("No {0} for id {1}"),
    EXP999("internal Exception {0}");

    private final String description;

    ErrorCode(String description) {
        this.description = description;
    }


    @Override
    public String getDescription() {
        return description;
    }
}
