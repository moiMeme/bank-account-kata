package com.sg.kata.model.account;


import com.sg.kata.model.customer.CustomerId;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.concurrent.ThreadLocalRandom;

public class AccountFactoryTest {

  private static final String ALPHABET = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";

  public static AccountId randomAccountId() {
    ThreadLocalRandom random = ThreadLocalRandom.current();
    char[] chars = new char[AccountId.ACCOUNT_NUMBER_LENGTH];
    for (int i = 0; i < AccountId.ACCOUNT_NUMBER_LENGTH; i++) {
      chars[i] = ALPHABET.charAt(random.nextInt(ALPHABET.length()));
    }
    return new AccountId(new String(chars));
  }

  public static Account createAccountForRandomCustomer() {
    Account account = new Account();
    account.setAccountNumber(randomAccountId());
    account.setCustomerId(new CustomerId(ThreadLocalRandom.current().nextInt(1_000_000)));
    account.setBalance(new BigDecimal(0));
    account.setCurrency(Currency.getInstance("EUR"));
    return account;
  }

}
