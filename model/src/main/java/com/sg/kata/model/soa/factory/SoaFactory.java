package com.sg.kata.model.soa.factory;

import com.sg.kata.model.account.AccountId;
import com.sg.kata.model.soa.Soa;
import com.sg.kata.model.transaction.Transaction;

import java.util.Set;

public interface SoaFactory {
    Set<Soa> create(AccountId accountId, Set<Transaction> transactions);
    Soa create(int lineNo, AccountId accountId, Transaction transaction);

}
