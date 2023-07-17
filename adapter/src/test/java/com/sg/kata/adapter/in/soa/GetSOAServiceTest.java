package com.sg.kata.adapter.in.soa;

import com.sg.kata.adapter.in.config.ApplicationConfig;
import com.sg.kata.application.port.in.account.AddAccountUseCase;
import com.sg.kata.application.port.in.soa.GetSOAUseCase;
import com.sg.kata.application.port.in.transaction.CreateTransactionUseCase;
import com.sg.kata.model.account.Account;
import com.sg.kata.model.account.AccountId;
import com.sg.kata.model.account.AccountType;
import com.sg.kata.model.common.Util;
import com.sg.kata.model.common.exception.CommonException;
import com.sg.kata.model.common.exception.ErrorCode;
import com.sg.kata.model.soa.Soa;
import com.sg.kata.model.transaction.TransactionType;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Currency;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class GetSOAServiceTest {
    private static final Currency EUR = Currency.getInstance("EUR");
    private static AccountId ACCOUNT_NUMBER;
    private static CreateTransactionUseCase createTransactionUseCase;

    @BeforeAll
    static void initTest() {
        ApplicationConfig.createInternalAccount();
        createTransactionUseCase = ApplicationConfig.getCreateTransactionUseCase();
        AddAccountUseCase addAccountUseCase = ApplicationConfig.getAddAccountUseCase();
        Account account = addAccountUseCase.addAccount(12345, EUR, AccountType.SAVING);
        ACCOUNT_NUMBER = account.getAccountNumber();
    }

    @Test
    void testGetByAccountAndDate() {
        GetSOAUseCase getSOAUseCase = ApplicationConfig.getGetSOAUseCase();

        createTransactionUseCase.newTransaction(ACCOUNT_NUMBER.value(), TransactionType.DEPOSIT, new BigDecimal(150), EUR, LocalDateTime.of(2019, 1, 1, 0, 0));
        createTransactionUseCase.newTransaction(ACCOUNT_NUMBER.value(), TransactionType.DEPOSIT, new BigDecimal(200), EUR, LocalDateTime.of(2021, 1, 1, 0, 0));
        createTransactionUseCase.newTransaction(ACCOUNT_NUMBER.value(), TransactionType.WITHDRAWAL, new BigDecimal(300), EUR, LocalDateTime.of(2021, 2, 1, 0, 0));
        createTransactionUseCase.newTransaction(ACCOUNT_NUMBER.value(), TransactionType.DEPOSIT, new BigDecimal(75), EUR, LocalDateTime.of(2021, 2, 2, 0, 0));
        createTransactionUseCase.newTransaction(ACCOUNT_NUMBER.value(), TransactionType.WITHDRAWAL, new BigDecimal(100), EUR, LocalDateTime.of(2021, 3, 2, 0, 0));

        LocalDateTime fromDate = LocalDateTime.of(2021, 1, 1, 0, 0);
        LocalDateTime toDate = LocalDateTime.of(2021, 2, 28, 23, 59);

        Set<Soa> result = getSOAUseCase.getByAccountAndDate(ACCOUNT_NUMBER, fromDate, toDate);
        assertThat(result).hasSize(3);
    }

    @Test
    void testGetByAccountAndDateWithException() {
        GetSOAUseCase getSOAUseCase = ApplicationConfig.getGetSOAUseCase();

        LocalDateTime fromDate = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime toDate = LocalDateTime.of(2021, 12, 31, 23, 59);
        ThrowableAssert.ThrowingCallable invocation = () -> getSOAUseCase.getByAccountAndDate(ACCOUNT_NUMBER, fromDate, toDate);
        String error = Util.formatErrorMessage(ErrorCode.EXP006, fromDate.format(DateTimeFormatter.ISO_DATE_TIME), toDate.format(DateTimeFormatter.ISO_DATE_TIME));
        assertThatExceptionOfType(CommonException.class).isThrownBy(invocation).withMessage(error);
    }


}
