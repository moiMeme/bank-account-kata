package com.sg.kata.model.transaction;

import com.sg.kata.model.account.AccountId;
import com.sg.kata.model.common.exception.ErrorCode;
import com.sg.kata.model.common.validation.Errors;
import com.sg.kata.model.common.validation.ValidatorHandler;

import java.util.Currency;
import java.util.Objects;

public class TransactionCurrencyValidation implements ValidatorHandler {

    @Override
    public Errors validate(Object object) {
        Errors errors = new Errors();
        if (object instanceof Transaction transaction) {
            Currency currency = transaction.getCurrency();
            if (Objects.nonNull(currency)) {
                AccountId creditAccount = transaction.getCreditAccount();
                AccountId debitAccount = transaction.getDebitAccount();
                if (!isValidAccordingToTheTransactionCurrency(creditAccount, currency) || !isValidAccordingToTheTransactionCurrency(debitAccount, currency)) {
                    errors.add(ErrorCode.EXP004);
                }
            }
        }
        return errors;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean isValidAccordingToTheTransactionCurrency(AccountId accountId, Currency currency) {
        if (Objects.isNull(accountId)) {
            return true;
        }
        String accountNumber = accountId.value();
        return accountNumber.contains(currency.getCurrencyCode());
    }

}
