package com.sg.kata.application.service.soa;

import com.sg.kata.model.account.Account;
import com.sg.kata.model.account.AccountType;
import com.sg.kata.model.customer.CustomerId;
import com.sg.kata.model.transaction.Transaction;
import com.sg.kata.model.transaction.TransactionType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static com.sg.kata.model.account.AccountFactoryTest.createAccountForCustomer;
import static com.sg.kata.model.transaction.TransactionFactoryTest.createTransaction;
import static org.junit.jupiter.api.Assertions.*;

class GetStatementOfAccountServiceTest {

    private final static Currency EUR = Currency.getInstance("EUR");

    @Test
    void givenAndAccountAndTwoTransaction_soaIsCreated() {
        Account account = createAccountForCustomer(new CustomerId(12345), EUR, AccountType.CLASSIC);
        Transaction transaction1 = createTransaction(account.getAccountNumber(), new BigDecimal(150), TransactionType.DEPOSIT, EUR);
        Transaction transaction2 = createTransaction(account.getAccountNumber(), new BigDecimal(100), TransactionType.WITHDRAWAL, EUR);
        account.addTransaction(transaction1, transaction2);



    }
}
