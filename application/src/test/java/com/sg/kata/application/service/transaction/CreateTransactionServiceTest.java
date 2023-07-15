package com.sg.kata.application.service.transaction;

import com.sg.kata.application.port.out.account.AccountDSGateway;
import com.sg.kata.application.port.out.transaction.TransactionDSGateway;
import com.sg.kata.model.account.Account;
import com.sg.kata.model.account.AccountId;
import com.sg.kata.model.account.AccountType;
import com.sg.kata.model.customer.CustomerId;
import com.sg.kata.model.soa.StatementOfAccount;
import com.sg.kata.model.transaction.Transaction;
import com.sg.kata.model.transaction.TransactionType;
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

import static com.sg.kata.model.account.AccountFactoryTest.createAccountForCustomer;
import static com.sg.kata.model.account.AccountFactoryTest.createInternalAccount;
import static com.sg.kata.model.transaction.TransactionFactoryTest.createTransaction;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateTransactionServiceTest {
    private static final Currency EUR = Currency.getInstance("EUR");
    private static final CustomerId TEST_CUSTOMER_ID = new CustomerId(61157);
    private static final AccountId TEST_ACCOUNT_ID = new AccountId("00000061157EUR123456");
    private static final Account TEST_ACCOUNT = createAccountForCustomer(TEST_CUSTOMER_ID, EUR, AccountType.CLASSIC);
    private static final Account TEST_INTERNAL_ACCOUNT_FOR_DEPOSIT = createInternalAccount(EUR, TransactionType.DEPOSIT);
    private static final Account TEST_INTERNAL_ACCOUNT_FOR_WITHDRAWAL = createInternalAccount(EUR, TransactionType.WITHDRAWAL);
    private final AccountDSGateway accountDSGateway = mock(AccountDSGateway.class);
    private final TransactionDSGateway transactionDSGateway = mock(TransactionDSGateway.class);
    private final CreateTransactionService createTransactionService = new CreateTransactionService(accountDSGateway, transactionDSGateway);
    private static final Transaction TEST_DEPOSIT_TRANSACTION = createTransaction(TEST_ACCOUNT_ID, new BigDecimal(150), TransactionType.DEPOSIT, EUR);


    @BeforeEach
    void initTest() {
        when(accountDSGateway.getById(TEST_ACCOUNT_ID.value())).thenReturn(Optional.of(TEST_ACCOUNT));
        when(accountDSGateway.getById(TEST_INTERNAL_ACCOUNT_FOR_DEPOSIT.getAccountNumber().value())).thenReturn(Optional.of(TEST_INTERNAL_ACCOUNT_FOR_DEPOSIT));
        when(accountDSGateway.getById(TEST_INTERNAL_ACCOUNT_FOR_WITHDRAWAL.getAccountNumber().value())).thenReturn(Optional.of(TEST_INTERNAL_ACCOUNT_FOR_WITHDRAWAL));
        when(transactionDSGateway.save(any())).thenReturn(TEST_DEPOSIT_TRANSACTION);
    }

    @Test
    void givenNewTransaction_transactionIsSavedAndReturned() {
        Transaction transaction = createTransactionService.newTransaction(TEST_ACCOUNT_ID.value(), TransactionType.DEPOSIT, new BigDecimal(150), Currency.getInstance("EUR"));

        verify(accountDSGateway, times(2)).getById(any());
        verify(accountDSGateway).getById(TEST_ACCOUNT_ID.value());
        verify(accountDSGateway).getById(TEST_INTERNAL_ACCOUNT_FOR_DEPOSIT.getAccountNumber().value());
        verify(transactionDSGateway, times(1)).save(any());


        assertThat(transaction.getTransactionId()).isNotNull();
    }

    @Test
    void givenNewTransaction_soaIsSavedAndReturned() {
        Transaction transaction = createTransactionService.newTransaction(TEST_ACCOUNT_ID.value(), TransactionType.DEPOSIT, new BigDecimal(150), EUR);

        verify(accountDSGateway, times(2)).getById(any());
        verify(accountDSGateway).getById(TEST_ACCOUNT_ID.value());
        verify(accountDSGateway).getById(TEST_INTERNAL_ACCOUNT_FOR_DEPOSIT.getAccountNumber().value());
        verify(transactionDSGateway, times(1)).save(any());

        assertThat(transaction.getTransactionId()).isNotNull();
        assertThat(TEST_ACCOUNT.getSoa()).hasSize(1);
    }

    @ParameterizedTest
    @MethodSource("transactionTestParameters")
    void givenNewTransaction_AccountBalanceIsModified(TransactionType type, BigDecimal balance, BigDecimal newBalance) {

        Account TEST_ACCOUNT = createAccountForCustomer(TEST_CUSTOMER_ID, EUR, AccountType.CLASSIC);
        when(accountDSGateway.getById(TEST_ACCOUNT.getAccountNumber().value())).thenReturn(Optional.of(TEST_ACCOUNT));

        if (TransactionType.DEPOSIT.equals(type)) {
            Transaction TEST_DEPOSIT_TRANSACTION = createTransaction(TEST_ACCOUNT_ID, balance, TransactionType.DEPOSIT, EUR);
            when(transactionDSGateway.save(any())).thenReturn(TEST_DEPOSIT_TRANSACTION);
        } else {
            if (TransactionType.WITHDRAWAL.equals(type)) {
                Transaction TEST_WITHDRAWAL_TRANSACTION = createTransaction(TEST_ACCOUNT_ID, balance, TransactionType.WITHDRAWAL, EUR);
                when(transactionDSGateway.save(any())).thenReturn(TEST_WITHDRAWAL_TRANSACTION);
            }
        }

        Transaction transaction = createTransactionService.newTransaction(TEST_ACCOUNT_ID.value(), type, balance, EUR);

        verify(accountDSGateway, times(2)).getById(any());
        verify(accountDSGateway).getById(TEST_ACCOUNT_ID.value());
        verify(accountDSGateway).getById(type.getInternalAccount().value());
        verify(transactionDSGateway, times(1)).save(any());

        StatementOfAccount soa = new StatementOfAccount(TEST_ACCOUNT_ID, transaction.getTrsDate(), transaction.getTrsType().getDescription(), transaction.getAmount());
        assertThat(transaction.getTransactionId()).isNotNull();
        assertThat(TEST_ACCOUNT.getSoa()).contains(soa);
        assertThat(TEST_ACCOUNT.getBalance()).isEqualTo(newBalance);
    }

    @Test
    void givenNewTransactionInTNDForEurAccount_ExceptionIsThrown() {

        ThrowableAssert.ThrowingCallable invocation = () -> createTransactionService.newTransaction(TEST_ACCOUNT_ID.value(), TransactionType.DEPOSIT, new BigDecimal(150), Currency.getInstance("TND"));
        assertThatIllegalArgumentException().isThrownBy(invocation);
        verify(accountDSGateway, times(1)).getById(TEST_ACCOUNT_ID.value());
        verify(transactionDSGateway, never()).save(any());
    }

    public static Stream<Arguments> transactionTestParameters() {
        return Stream.of(
                Arguments.of(TransactionType.DEPOSIT, BigDecimal.valueOf(150), BigDecimal.valueOf(150)),
                Arguments.of(TransactionType.WITHDRAWAL, BigDecimal.valueOf(200), BigDecimal.valueOf(-200))
        );
    }

}
