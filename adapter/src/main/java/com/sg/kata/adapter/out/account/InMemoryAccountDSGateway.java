package com.sg.kata.adapter.out.account;

import com.sg.kata.application.port.out.account.AccountDSGateway;
import com.sg.kata.model.account.Account;
import com.sg.kata.model.account.AccountId;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAccountDSGateway implements AccountDSGateway {

    private final Map<AccountId, Account> accounts = new ConcurrentHashMap<>(10);

    @Override
    public Account save(Account data) {
        accounts.put(data.getAccountNumber(), data);
        return data;
    }

    @Override
    public Optional<Account> getById(AccountId accountId) {
        return Optional.ofNullable(accounts.get(accountId));
    }

}
