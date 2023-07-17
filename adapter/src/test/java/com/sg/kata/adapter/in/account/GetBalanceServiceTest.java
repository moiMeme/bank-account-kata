package com.sg.kata.adapter.in.account;

import com.sg.kata.adapter.in.config.ApplicationConfig;
import com.sg.kata.application.port.in.account.AddAccountUseCase;
import com.sg.kata.application.port.in.account.GetBalanceUserCase;
import com.sg.kata.application.port.in.transaction.CreateTransactionUseCase;
import com.sg.kata.model.account.Account;
import com.sg.kata.model.account.AccountId;
import com.sg.kata.model.account.AccountType;
import com.sg.kata.model.transaction.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;

class GetBalanceServiceTest {

    private static final Currency EUR = Currency.getInstance("EUR");
    private static AccountId ACCOUNT_NUMBER;
    private CreateTransactionUseCase createTransactionUseCase;

    @BeforeEach
    void initTest() {
        ApplicationConfig.createInternalAccount();
        createTransactionUseCase = ApplicationConfig.getCreateTransactionUseCase();
        AddAccountUseCase addAccountUseCase = ApplicationConfig.getAddAccountUseCase();
        Account account = addAccountUseCase.addAccount(12345, EUR, AccountType.SAVING);
        ACCOUNT_NUMBER = account.getAccountNumber();
    }


    @Test
    void testGetBalance() {
        GetBalanceUserCase getBalanceUserCase = ApplicationConfig.getGetBalanceUserCase();

        createTransactionUseCase.newTransaction(ACCOUNT_NUMBER.value(), TransactionType.DEPOSIT, new BigDecimal(150), EUR, LocalDateTime.of(2019, 1, 1, 0, 0));
        createTransactionUseCase.newTransaction(ACCOUNT_NUMBER.value(), TransactionType.DEPOSIT, new BigDecimal(200), EUR, LocalDateTime.of(2021, 1, 1, 0, 0));
        createTransactionUseCase.newTransaction(ACCOUNT_NUMBER.value(), TransactionType.WITHDRAWAL, new BigDecimal(300), EUR, LocalDateTime.of(2021, 2, 1, 0, 0));
        createTransactionUseCase.newTransaction(ACCOUNT_NUMBER.value(), TransactionType.DEPOSIT, new BigDecimal(75), EUR, LocalDateTime.of(2021, 2, 2, 0, 0));
        createTransactionUseCase.newTransaction(ACCOUNT_NUMBER.value(), TransactionType.WITHDRAWAL, new BigDecimal(100), EUR, LocalDateTime.of(2021, 3, 2, 0, 0));

        LocalDateTime beforeThisDate1 = LocalDateTime.of(2021, 1, 1, 0, 0);
        boolean inclusive1 = true;

        BigDecimal balance1 = getBalanceUserCase.getBalance(ACCOUNT_NUMBER, beforeThisDate1, inclusive1);
        assertThat(balance1).isEqualTo("350");

        LocalDateTime beforeThisDate2 = LocalDateTime.of(2021, 1, 1, 0, 0);
        boolean inclusive2 = false;

        BigDecimal balance2 = getBalanceUserCase.getBalance(ACCOUNT_NUMBER, beforeThisDate2, inclusive2);
        assertThat(balance2).isEqualTo("150");

    }

}
