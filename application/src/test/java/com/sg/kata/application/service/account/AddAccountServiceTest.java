package com.sg.kata.application.service.account;

import com.sg.kata.application.port.out.account.AccountDSGateway;
import com.sg.kata.model.account.Account;
import com.sg.kata.model.account.AccountType;
import com.sg.kata.model.account.factory.AccountFactory;
import com.sg.kata.model.account.factory.CommonAccountFactory;
import com.sg.kata.model.common.Util;
import com.sg.kata.model.common.exception.CommonException;
import com.sg.kata.model.common.exception.ErrorCode;
import com.sg.kata.model.customer.CustomerId;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Currency;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

class AddAccountServiceTest {

    private static final Currency EUR = Currency.getInstance("EUR");
    private static final CustomerId TEST_CUSTOMER_ID = new CustomerId(61157);
    private static final AccountFactory accountFactory = new CommonAccountFactory();
    private final AccountDSGateway accountDSGateway = mock(AccountDSGateway.class);
    private final AddAccountService addAccountService = new AddAccountService(accountDSGateway, accountFactory);
    private static final Account TEST_ACCOUNT = accountFactory.create(TEST_CUSTOMER_ID, EUR, AccountType.CLASSIC);

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


    @ParameterizedTest
    @MethodSource("createAccountTestParameters")
    void givenAnUnknownCustomerId_addAccount_throwsException(Integer customerId, Currency currency, AccountType accountType, String error) {
        ThrowableAssert.ThrowingCallable invocation = () -> addAccountService.addAccount(customerId, currency, accountType);
        assertThatExceptionOfType(CommonException.class).isThrownBy(invocation).withMessage(error);
        verify(accountDSGateway, never()).save(any());
    }

    public static Stream<Arguments> createAccountTestParameters() {
        String customerIdError = Util.formatErrorMessage(ErrorCode.EXP005, "customerId");
        String currencyError = Util.formatErrorMessage(ErrorCode.EXP005, "currency");
        String accountTypeError = Util.formatErrorMessage(ErrorCode.EXP005, "accountType");

        return Stream.of(
                Arguments.of(null, EUR, AccountType.CLASSIC, customerIdError),
                Arguments.of(TEST_CUSTOMER_ID.value(), null, AccountType.SAVING, currencyError),
                Arguments.of(TEST_CUSTOMER_ID.value(), EUR, null, accountTypeError)
        );
    }

}
