package com.sg.kata.application.service.account;

import com.sg.kata.application.port.in.account.AddAccountUseCase;
import com.sg.kata.application.port.out.account.AccountDSGateway;
import com.sg.kata.model.account.Account;
import com.sg.kata.model.account.AccountId;
import com.sg.kata.model.account.AccountType;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class AddAccountService implements AddAccountUseCase {

    private final AccountDSGateway accountDSGateway;

    public AddAccountService(AccountDSGateway accountDSGateway) {
        this.accountDSGateway = accountDSGateway;
    }

    @Override
    public Account addAccount(Integer customerId, Currency currency, AccountType accountType) {
        Objects.requireNonNull(customerId, "'customerId' must not be null");
        Objects.requireNonNull(currency, "'currency' must not be null");
        Objects.requireNonNull(accountType, "'accountType' must not be null");
        String accountNumber = String.format("%" + 11 + "s", customerId).replace(" ", "0") + currency.getCurrencyCode() + accountType.getType();
        Account account = new Account();
        account.setAccountNumber(new AccountId(accountNumber));
        account.setCurrency(currency);
        account.setBalance(new BigDecimal(0));
        return accountDSGateway.save(account);
    }

}
