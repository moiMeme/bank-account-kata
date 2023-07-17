package com.sg.kata.adapter.in.transaction;

import com.sg.kata.adapter.in.config.ApplicationConfig;
import com.sg.kata.application.port.in.account.AddAccountUseCase;
import com.sg.kata.application.port.in.transaction.CreateTransactionUseCase;
import com.sg.kata.model.account.Account;
import com.sg.kata.model.account.AccountId;
import com.sg.kata.model.account.AccountType;
import com.sg.kata.model.common.Util;
import com.sg.kata.model.common.exception.CommonException;
import com.sg.kata.model.common.exception.ErrorCode;
import com.sg.kata.model.transaction.Transaction;
import com.sg.kata.model.transaction.TransactionType;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class CreateTransactionServiceTest {
    private static final Currency EUR = Currency.getInstance("EUR");
    private static final Currency TND = Currency.getInstance("TND");

    private static AccountId ACCOUNT_NUMBER = new AccountId("00000012345EUR654321");
    private static Account account;
    private static CreateTransactionUseCase createTransactionUseCase;

    @BeforeAll
    static void initTests() {
        ApplicationConfig.createInternalAccount();
        createTransactionUseCase = ApplicationConfig.getCreateTransactionUseCase();
        AddAccountUseCase addAccountUseCase = ApplicationConfig.getAddAccountUseCase();
        account = addAccountUseCase.addAccount(12345, EUR, AccountType.SAVING);
        ACCOUNT_NUMBER = account.getAccountNumber();
    }

    public static Stream<Arguments> createTransactionTestParameters() {
        return Stream.of(
                Arguments.of(TransactionType.DEPOSIT, BigDecimal.valueOf(300), EUR),
                Arguments.of(TransactionType.WITHDRAWAL, BigDecimal.valueOf(250), EUR)
        );
    }

    @ParameterizedTest
    @MethodSource("createTransactionTestParameters")
    void createTransactionService(TransactionType type, BigDecimal amount, Currency currency) {
        Transaction transaction = createTransactionUseCase.newTransaction(ACCOUNT_NUMBER.value(), type, amount, currency);
        assertThat(transaction.getTransactionId()).isNotNull();
        assertThat(ApplicationConfig.getTransactionDSGateway().getById(transaction.getTransactionId()).isPresent()).isTrue();

    }

    @Test
    void createTransactionAndVerifyAccountBalance() {
        Transaction trx1 = createTransactionUseCase.newTransaction(ACCOUNT_NUMBER.value(), TransactionType.DEPOSIT, new BigDecimal(150), EUR);
        Transaction trx2 = createTransactionUseCase.newTransaction(ACCOUNT_NUMBER.value(), TransactionType.DEPOSIT, new BigDecimal(200), EUR);
        Transaction trx3 = createTransactionUseCase.newTransaction(ACCOUNT_NUMBER.value(), TransactionType.WITHDRAWAL, new BigDecimal(100), EUR);
        assertThat(ApplicationConfig.getTransactionDSGateway().getById(trx1.getTransactionId()).isPresent()).isTrue();
        assertThat(ApplicationConfig.getTransactionDSGateway().getById(trx2.getTransactionId()).isPresent()).isTrue();
        assertThat(ApplicationConfig.getTransactionDSGateway().getById(trx3.getTransactionId()).isPresent()).isTrue();

        assertThat(account.getBalance()).isEqualTo(new BigDecimal(250));
    }

    public static Stream<Arguments> createTransactionTestParametersWithException() {
        String accountNumberError = Util.formatErrorMessage(ErrorCode.EXP005, "accountNumber");
        String currencyError = Util.formatErrorMessage(ErrorCode.EXP005, "currency");
        String trsTypeError = Util.formatErrorMessage(ErrorCode.EXP005, "trsType");
        String amountError = Util.formatErrorMessage(ErrorCode.EXP005, "amount");
        String currencyNotAllowedError = Util.formatErrorMessage(ErrorCode.EXP007, ACCOUNT_NUMBER.value());
        String transactionNotAllowedError = Util.formatErrorMessage(ErrorCode.EXP009);

        return Stream.of(
                Arguments.of(null, TransactionType.DEPOSIT, new BigDecimal(150), EUR, accountNumberError),
                Arguments.of(ACCOUNT_NUMBER.value(), null, new BigDecimal(150), EUR, trsTypeError),
                Arguments.of(ACCOUNT_NUMBER.value(), TransactionType.DEPOSIT, null, EUR, amountError),
                Arguments.of(ACCOUNT_NUMBER.value(), TransactionType.DEPOSIT, new BigDecimal(150), null, currencyError),
                Arguments.of(ACCOUNT_NUMBER.value(), TransactionType.DEPOSIT, new BigDecimal(150), TND, currencyNotAllowedError),
                Arguments.of(ACCOUNT_NUMBER.value(), TransactionType.WITHDRAWAL, new BigDecimal(1500), EUR, transactionNotAllowedError)
        );
    }

    @ParameterizedTest
    @MethodSource("createTransactionTestParametersWithException")
    void givenAnUnknownCustomerId_addAccount_throwsException(String accountNumber, TransactionType trsType, BigDecimal amount, Currency currency, String error) {
        ThrowableAssert.ThrowingCallable invocation = () -> createTransactionUseCase.newTransaction(accountNumber, trsType, amount, currency);
        assertThatExceptionOfType(CommonException.class).isThrownBy(invocation).withMessage(error);
    }

}
