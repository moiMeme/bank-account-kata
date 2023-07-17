package com.sg.kata.model.account.factory;

import com.sg.kata.model.account.Account;
import com.sg.kata.model.account.AccountId;
import com.sg.kata.model.account.AccountType;
import com.sg.kata.model.common.Util;
import com.sg.kata.model.customer.CustomerId;
import com.sg.kata.model.transaction.TransactionType;

import java.math.BigDecimal;
import java.util.Currency;

public class CommonAccountFactory implements AccountFactory {

    @Override
    public Account create(CustomerId customerId, Currency currency, AccountType accountType) {
        String accountNumber = Util.accountNumber(customerId.value(), currency, accountType);
        return create(accountNumber, customerId, currency, accountType);
    }

    @Override
    public Account create(String accountNumber, CustomerId customerId, Currency currency, AccountType accountType) {
        Account account = new Account();
        account.setCustomerId(customerId);
        account.setBalance(new BigDecimal(0));
        account.setCurrency(currency);
        account.setAccountNumber(new AccountId(accountNumber));
        account.setAccountType(accountType);
        return account;
    }

    @Override
    public Account create(Currency currency, TransactionType type) {
        Account account = new Account();
        account.setCustomerId(new CustomerId(10000));
        account.setBalance(new BigDecimal(0));
        account.setCurrency(currency);
        account.setAccountNumber(type.getInternalAccount());
        account.setAccountType(type.getAccountType());
        return account;
    }
}
