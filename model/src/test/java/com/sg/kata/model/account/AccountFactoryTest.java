package com.sg.kata.model.account;


import com.sg.kata.model.customer.CustomerId;
import com.sg.kata.model.transaction.TransactionType;

import java.math.BigDecimal;
import java.util.Currency;

public class AccountFactoryTest {

  public static Account createAccountForCustomer(CustomerId customerId, Currency currency, AccountType accountType) {
    Account account = new Account();
    account.setCustomerId(customerId);
    account.setBalance(new BigDecimal(0));
    account.setCurrency(currency);
    String accountNumber = String.format("%" + 11 + "s", customerId.value()).replace(" ", "0") + currency.getCurrencyCode() + accountType.getType();
    account.setAccountNumber(new AccountId(accountNumber));

    return account;
  }

  public static Account createInternalAccount(Currency currency, TransactionType type) {
    Account account = new Account();
    account.setCustomerId(new CustomerId(10000));
    account.setBalance(new BigDecimal(0));
    account.setCurrency(currency);
    account.setAccountNumber(type.getInternalAccount());
    return account;
  }

}
