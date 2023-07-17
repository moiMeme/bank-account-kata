package com.sg.kata.model.account;

import com.sg.kata.model.account.factory.AccountFactory;
import com.sg.kata.model.account.factory.CommonAccountFactory;
import com.sg.kata.model.common.Util;
import com.sg.kata.model.common.exception.ErrorCode;
import com.sg.kata.model.common.validation.Errors;
import com.sg.kata.model.customer.CustomerId;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Currency;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class AccountTest {

    private final static CustomerId customerId = new CustomerId(12345);
    private final static Currency EUR = Currency.getInstance("EUR");

    private final AccountFactory accountFactory = new CommonAccountFactory();
    @ParameterizedTest
    @CsvSource({"00000012345EUR123456, 12345, EUR, CLASSIC",
            "00000012345TND654321, 12345, TND, SAVING"})
    void givenANewAccount_whenAllFieldAreSet_thenAccountMustBeValid(String accountNumber, int customerId, String currencyCode, String accountType) {
        CustomerId customer = new CustomerId(customerId);
        Currency currency = Currency.getInstance(currencyCode);
        AccountType type = AccountType.valueOf(accountType);
        Account account = accountFactory.create(accountNumber, customer, currency, type);
        Errors errors = account.validate();
        assertThat(errors).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("accountCreationTestParameters")
    void givenANewAccount_whenFieldISNotSet_thenAccountIsNotValid(String accountNumber, CustomerId customerId, Currency currency, AccountType accountType, String[] errors) {
        Account account = accountFactory.create(accountNumber, customerId, currency, accountType);
        Errors errorList = account.validate();
        assertThat(errorList).containsExactly(errors);
    }

    public static Stream<Arguments> accountCreationTestParameters() {

        String customerIdError = Util.formatErrorMessage(ErrorCode.EXP001, "customerId", "Account");
        String currencyError = Util.formatErrorMessage(ErrorCode.EXP001, "currency", "Account");
        String accountTypeError = Util.formatErrorMessage(ErrorCode.EXP001, "accountType", "Account");
        String accountNumberError = Util.formatErrorMessage(ErrorCode.EXP003, "accountNumber", "Account");

        return Stream.of(
                Arguments.of("00000012345EUR123456", null, EUR, AccountType.CLASSIC, new String[]{customerIdError, accountNumberError}),
                Arguments.of("00000012345EUR123456", customerId, null, AccountType.SAVING, new String[]{currencyError, accountNumberError}),
                Arguments.of("00000012345EUR123456", customerId, EUR, null, new String[]{accountTypeError, accountNumberError}),
                Arguments.of("00000012345EUR123456", customerId, null, null, new String[]{currencyError, accountTypeError, accountNumberError}),
                Arguments.of("00000012345TND123456", customerId, EUR, AccountType.CLASSIC, new String[]{accountNumberError}),
                Arguments.of("00000012345EUR123456", customerId, EUR, AccountType.SAVING, new String[]{accountNumberError})
        );
    }

}
