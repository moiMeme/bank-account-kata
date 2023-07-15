package com.sg.kata.application.port.in.account;

import com.sg.kata.model.account.Account;
import com.sg.kata.model.account.AccountType;

import java.util.Currency;

public interface AddAccountUseCase {

    Account addAccount(Integer customerId, Currency currency,  AccountType accountType);
}
