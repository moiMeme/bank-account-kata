package com.sg.kata.model.account.factory;

import com.sg.kata.model.account.Account;
import com.sg.kata.model.account.AccountId;
import com.sg.kata.model.account.AccountType;
import com.sg.kata.model.common.Util;
import com.sg.kata.model.customer.CustomerId;
import com.sg.kata.model.transaction.TransactionType;

import java.math.BigDecimal;
import java.util.Currency;

public interface AccountFactory {

    Account create(CustomerId customerId, Currency currency, AccountType accountType);

    Account create(String accountNumber, CustomerId customerId, Currency currency, AccountType accountType);

    Account create(Currency currency, TransactionType type);

}
