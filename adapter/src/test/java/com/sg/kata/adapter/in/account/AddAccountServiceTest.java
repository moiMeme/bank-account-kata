package com.sg.kata.adapter.in.account;

import com.sg.kata.adapter.in.config.ApplicationConfig;
import com.sg.kata.application.port.in.account.AddAccountUseCase;
import com.sg.kata.model.account.Account;
import com.sg.kata.model.account.AccountType;
import com.sg.kata.model.common.Util;
import com.sg.kata.model.common.exception.CommonException;
import com.sg.kata.model.common.exception.ErrorCode;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Currency;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class AddAccountServiceTest {

    private final static Currency EUR = Currency.getInstance("EUR");
    private final static Currency TND = Currency.getInstance("TND");

    public static Stream<Arguments> addAccountParameterTest() {
        return Stream.of(
                Arguments.of("12345", EUR, AccountType.CLASSIC, "00000012345EUR123456"),
                Arguments.of("54321", TND, AccountType.CLASSIC, "00000054321TND123456"),
                Arguments.of("651", EUR, AccountType.SAVING, "00000000651EUR654321"));
    }

    @ParameterizedTest
    @MethodSource("addAccountParameterTest")
    void addAccountTest(Integer customerId, Currency currency, AccountType accountType, String expectedAccountNumber) {
        AddAccountUseCase addAccountUseCase = ApplicationConfig.getAddAccountUseCase();
        Account account = addAccountUseCase.addAccount(customerId, currency, accountType);

        assertThat(account.getAccountNumber().value()).isEqualTo(expectedAccountNumber);
        assertThat(account.getCurrency().getCurrencyCode()).isEqualTo(currency.getCurrencyCode());
    }

    public static Stream<Arguments> addAccountWithExceptionParameterTest() {
        String customerIdError = Util.formatErrorMessage(ErrorCode.EXP005, "customerId");
        String currencyError = Util.formatErrorMessage(ErrorCode.EXP005, "currency");
        String accountTypeError = Util.formatErrorMessage(ErrorCode.EXP005, "accountType");

        return Stream.of(
                Arguments.of(null, EUR, AccountType.CLASSIC, customerIdError),
                Arguments.of(1236, null, AccountType.SAVING, currencyError),
                Arguments.of(5487, EUR, null, accountTypeError)
        );
    }

    @ParameterizedTest
    @MethodSource("addAccountWithExceptionParameterTest")
    void addAccountWithExceptionTest(Integer customerId, Currency currency, AccountType accountType, String error) {
        AddAccountUseCase addAccountUseCase = ApplicationConfig.getAddAccountUseCase();

        ThrowableAssert.ThrowingCallable invocation = () -> addAccountUseCase.addAccount(customerId, currency, accountType);
        assertThatExceptionOfType(CommonException.class).isThrownBy(invocation).withMessage(error);

    }

}
