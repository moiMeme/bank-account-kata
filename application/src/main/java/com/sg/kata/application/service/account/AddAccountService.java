package com.sg.kata.application.service.account;

import com.sg.kata.application.port.in.account.AddAccountUseCase;
import com.sg.kata.application.port.out.account.AccountDSGateway;
import com.sg.kata.model.account.Account;
import com.sg.kata.model.account.AccountId;
import com.sg.kata.model.account.AccountType;
import com.sg.kata.model.account.factory.AccountFactory;
import com.sg.kata.model.common.Util;
import com.sg.kata.model.customer.CustomerId;

import java.math.BigDecimal;
import java.util.Currency;

public class AddAccountService implements AddAccountUseCase {

    private final AccountDSGateway accountDSGateway;
    private final AccountFactory accountFactory;

    public AddAccountService(AccountDSGateway accountDSGateway, AccountFactory accountFactory) {
        this.accountDSGateway = accountDSGateway;
        this.accountFactory = accountFactory;
    }

    @Override
    public Account addAccount(Integer customerId, Currency currency, AccountType accountType) {
        Util.requireNonNull(customerId, "customerId");
        Util.requireNonNull(currency, "currency");
        Util.requireNonNull(accountType, "accountType");
        Account account = accountFactory.create(new CustomerId(customerId), currency, accountType);
        return accountDSGateway.save(account);
    }

}
