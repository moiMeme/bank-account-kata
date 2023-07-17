package com.sg.kata.model.account;

import com.sg.kata.model.common.exception.ErrorCode;
import com.sg.kata.model.common.validation.Errors;
import com.sg.kata.model.common.validation.ValidatorHandler;
import com.sg.kata.model.customer.CustomerId;

import java.util.Currency;
import java.util.Optional;

public class AccountNumberValidation implements ValidatorHandler {
    @Override
    public Errors validate(Object object) {
        Errors errors = new Errors();
        if (object instanceof Account account) {
            String accountNumber = Optional.ofNullable(account.getAccountNumber()).map(AccountId::value).orElse("");
            int customerId = Optional.ofNullable(account.getCustomerId()).map(CustomerId::value).orElse(0);
            String currency = Optional.ofNullable(account.getCurrency()).map(Currency::getCurrencyCode).orElse("");
            int accountType = Optional.ofNullable(account.getAccountType()).map(AccountType::getType).orElse(0);
            if (!accountNumber.contains(customerId + currency + accountType)) {
               errors.add(ErrorCode.EXP003, "accountNumber", object.getClass().getSimpleName());
            }
        }
        return errors;
    }
}
