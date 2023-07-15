package com.sg.kata.application.service.account;

import com.sg.kata.application.port.out.account.AccountDSGateway;
import com.sg.kata.model.account.Account;
import com.sg.kata.model.account.AccountType;
import com.sg.kata.model.customer.CustomerId;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Currency;

import static com.sg.kata.model.account.AccountFactoryTest.createAccountForCustomer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AddAccountServiceTest {

    private static final Currency EUR = Currency.getInstance("EUR");
    private static final CustomerId TEST_CUSTOMER_ID = new CustomerId(61157);
    private final AccountDSGateway accountDSGateway = mock(AccountDSGateway.class);
    private final AddAccountService addAccountService = new AddAccountService(accountDSGateway);
    private static final Account TEST_ACCOUNT = createAccountForCustomer(TEST_CUSTOMER_ID, EUR, AccountType.CLASSIC);

    @BeforeEach
    void initTest() {
        when(accountDSGateway.save(any())).thenReturn(TEST_ACCOUNT);
    }

    @Test
    void givenNewAccount_accountIsSavedAndReturned() {

        Account account = addAccountService.addAccount(TEST_CUSTOMER_ID.value(), Currency.getInstance("EUR"), AccountType.CLASSIC);

        verify(accountDSGateway, times(1)).save(any());

        assertThat(account.getAccountNumber()).isNotNull();
        assertThat(account.getAccountNumber().value()).contains("61157EUR");
    }


    @Test
    void givenAnUnknownCustomerId_addAccount_throwsException() {
        ThrowableAssert.ThrowingCallable invocation = () -> addAccountService.addAccount(null, EUR, AccountType.SAVING);
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(invocation);
        verify(accountDSGateway, never()).save(any());
    }


}
