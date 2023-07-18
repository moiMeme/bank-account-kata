package com.sg.kata.application.service.transaction;

import com.sg.kata.application.port.out.account.AccountDSGateway;
import com.sg.kata.application.port.out.transaction.TransactionDSGateway;
import com.sg.kata.model.account.Account;
import com.sg.kata.model.account.AccountId;
import com.sg.kata.model.account.AccountType;
import com.sg.kata.model.account.factory.AccountFactory;
import com.sg.kata.model.account.factory.CommonAccountFactory;
import com.sg.kata.model.common.Util;
import com.sg.kata.model.common.exception.CommonException;
import com.sg.kata.model.common.exception.ErrorCode;
import com.sg.kata.model.customer.CustomerId;
import com.sg.kata.model.transaction.Transaction;
import com.sg.kata.model.transaction.TransactionType;
import com.sg.kata.model.transaction.factory.CommonTransactionFactory;
import com.sg.kata.model.transaction.factory.TransactionFactory;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateTransactionServiceTest {
    private static final Currency EUR = Currency.getInstance("EUR");
    private static final CustomerId TEST_CUSTOMER_ID = new CustomerId(61157);
    private static final AccountId TEST_ACCOUNT_ID = new AccountId("00000061157EUR123456");
    private static final AccountFactory accountFactory = new CommonAccountFactory();
    private static final TransactionFactory transactionFactory = new CommonTransactionFactory();
    private static final Account TEST_ACCOUNT = accountFactory.create(TEST_CUSTOMER_ID, EUR, AccountType.CLASSIC);
    private static final Account TEST_INTERNAL_ACCOUNT_FOR_DEPOSIT = accountFactory.create(EUR, TransactionType.DEPOSIT);
    private static final Account TEST_INTERNAL_ACCOUNT_FOR_WITHDRAWAL = accountFactory.create(EUR, TransactionType.WITHDRAWAL);
    private final AccountDSGateway accountDSGateway = mock(AccountDSGateway.class);
    private final TransactionDSGateway transactionDSGateway = mock(TransactionDSGateway.class);
    private final CreateTransactionService createTransactionService = new CreateTransactionService(accountDSGateway, transactionDSGateway, transactionFactory);
    private static final Transaction TEST_DEPOSIT_TRANSACTION = transactionFactory.create(TEST_ACCOUNT_ID, new BigDecimal(150), TransactionType.DEPOSIT, EUR);


    @BeforeEach
    void initTest() {
        when(accountDSGateway.getById(TEST_ACCOUNT_ID)).thenReturn(Optional.of(TEST_ACCOUNT));
        when(accountDSGateway.getById(TEST_INTERNAL_ACCOUNT_FOR_DEPOSIT.getAccountNumber())).thenReturn(Optional.of(TEST_INTERNAL_ACCOUNT_FOR_DEPOSIT));
        when(accountDSGateway.getById(TEST_INTERNAL_ACCOUNT_FOR_WITHDRAWAL.getAccountNumber())).thenReturn(Optional.of(TEST_INTERNAL_ACCOUNT_FOR_WITHDRAWAL));
        when(transactionDSGateway.save(any())).thenReturn(TEST_DEPOSIT_TRANSACTION);
    }

    @Test
    void givenNewTransaction_transactionIsSavedAndReturned() {
        Transaction transaction = createTransactionService.newTransaction(TEST_ACCOUNT_ID.value(), TransactionType.DEPOSIT, new BigDecimal(150), Currency.getInstance("EUR"));

        verify(accountDSGateway, times(2)).getById(any());
        verify(accountDSGateway).getById(TEST_ACCOUNT_ID);
        verify(accountDSGateway).getById(TEST_INTERNAL_ACCOUNT_FOR_DEPOSIT.getAccountNumber());
        verify(transactionDSGateway, times(1)).save(any());


        assertThat(transaction.getTransactionId()).isNotNull();
    }

    @ParameterizedTest
    @MethodSource("transactionTestParameters")
    void givenNewTransaction_AccountBalanceIsModified(TransactionType type, BigDecimal balance, BigDecimal amount, BigDecimal newBalance) {

        Account TEST_ACCOUNT = accountFactory.create(TEST_CUSTOMER_ID, EUR, AccountType.CLASSIC);
        TEST_ACCOUNT.setBalance(balance);
        when(accountDSGateway.getById(TEST_ACCOUNT.getAccountNumber())).thenReturn(Optional.of(TEST_ACCOUNT));

        if (TransactionType.DEPOSIT.equals(type)) {
            Transaction TEST_DEPOSIT_TRANSACTION = transactionFactory.create(TEST_ACCOUNT_ID, amount, TransactionType.DEPOSIT, EUR);
            when(transactionDSGateway.save(any())).thenReturn(TEST_DEPOSIT_TRANSACTION);
        } else {
            if (TransactionType.WITHDRAWAL.equals(type)) {
                Transaction TEST_WITHDRAWAL_TRANSACTION = transactionFactory.create(TEST_ACCOUNT_ID, amount, TransactionType.WITHDRAWAL, EUR);
                when(transactionDSGateway.save(any())).thenReturn(TEST_WITHDRAWAL_TRANSACTION);
            }
        }

        Transaction transaction = createTransactionService.newTransaction(TEST_ACCOUNT_ID.value(), type, amount, EUR);

        verify(accountDSGateway, times(2)).getById(any());
        verify(accountDSGateway).getById(TEST_ACCOUNT_ID);
        verify(accountDSGateway).getById(type.getInternalAccount());
        verify(transactionDSGateway, times(1)).save(any());

        assertThat(transaction.getTransactionId()).isNotNull();
        assertThat(TEST_ACCOUNT.getBalance()).isEqualTo(newBalance);
    }

    @Test
    void givenNewTransactionInTNDForEurAccount_ExceptionIsThrown() {

        ThrowableAssert.ThrowingCallable invocation = () -> createTransactionService.newTransaction(TEST_ACCOUNT_ID.value(), TransactionType.DEPOSIT, new BigDecimal(150), Currency.getInstance("TND"));
        String error = Util.formatErrorMessage(ErrorCode.EXP007, TEST_ACCOUNT_ID.value());
        assertThatExceptionOfType(CommonException.class).isThrownBy(invocation).withMessage(error);
        verify(accountDSGateway, times(1)).getById(TEST_ACCOUNT_ID);
        verify(transactionDSGateway, never()).save(any());
    }

    @Test
    void givenNewTransactionForAnAccountWithBalanceEqualToZeroAndTransactionTypeEqualToWithdrawal_ExceptionIsThrown() {

        ThrowableAssert.ThrowingCallable invocation = () -> createTransactionService.newTransaction(TEST_ACCOUNT_ID.value(), TransactionType.WITHDRAWAL, new BigDecimal(150), EUR);
        assertThatExceptionOfType(CommonException.class).isThrownBy(invocation).withMessage(Util.formatErrorMessage(ErrorCode.EXP009));
        verify(accountDSGateway, times(1)).getById(TEST_ACCOUNT_ID);
        verify(transactionDSGateway, never()).save(any());
    }


    @ParameterizedTest
    @MethodSource("createTransactionTestParameters")
    void givenAnUnknownCustomerId_addAccount_throwsException(String accountNumber, TransactionType trsType, BigDecimal amount, Currency currency, String error) {
        ThrowableAssert.ThrowingCallable invocation = () -> createTransactionService.newTransaction(accountNumber, trsType, amount, currency);
        assertThatExceptionOfType(CommonException.class).isThrownBy(invocation).withMessage(error);
        verify(accountDSGateway, never()).save(any());
    }

    public static Stream<Arguments> createTransactionTestParameters() {
        String accountNumberError = Util.formatErrorMessage(ErrorCode.EXP005, "accountNumber");
        String currencyError = Util.formatErrorMessage(ErrorCode.EXP005, "currency");
        String trsTypeError = Util.formatErrorMessage(ErrorCode.EXP005, "trsType");
        String amountError = Util.formatErrorMessage(ErrorCode.EXP005, "amount");

        return Stream.of(
                Arguments.of(null, TransactionType.DEPOSIT, new BigDecimal(150), EUR, accountNumberError),
                Arguments.of(TEST_ACCOUNT_ID.value(), null, new BigDecimal(150), EUR, trsTypeError),
                Arguments.of(TEST_ACCOUNT_ID.value(), TransactionType.DEPOSIT, null, EUR, amountError),
                Arguments.of(TEST_ACCOUNT_ID.value(), TransactionType.DEPOSIT, new BigDecimal(150), null, currencyError)
        );


    }


    public static Stream<Arguments> transactionTestParameters() {
        return Stream.of(
                Arguments.of(TransactionType.DEPOSIT, BigDecimal.valueOf(150), BigDecimal.valueOf(150), BigDecimal.valueOf(300)),
                Arguments.of(TransactionType.WITHDRAWAL, BigDecimal.valueOf(250), BigDecimal.valueOf(200), BigDecimal.valueOf(50))
        );
    }

}
