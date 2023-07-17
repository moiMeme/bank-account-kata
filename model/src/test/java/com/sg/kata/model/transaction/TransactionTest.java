package com.sg.kata.model.transaction;

import com.sg.kata.model.account.AccountId;
import com.sg.kata.model.common.Util;
import com.sg.kata.model.common.exception.ErrorCode;
import com.sg.kata.model.common.validation.Errors;
import com.sg.kata.model.transaction.factory.CommonTransactionFactory;
import com.sg.kata.model.transaction.factory.TransactionFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class TransactionTest {

    private final static TransactionId transactionId = new TransactionId(12345);
    private final static AccountId savingAccountId = new AccountId("00000012345EUR654321");
    private final static Currency EUR = Currency.getInstance("EUR");

    private final TransactionFactory transactionFactory = new CommonTransactionFactory();

    @ParameterizedTest
    @CsvSource({"123456, 00000012345EUR654321, DEPOSIT, 1.25, EUR",
            "5, 00000012345EUR654321, WITHDRAWAL, 25.22, EUR"})
    void givenANewTransaction_whenAllFieldAreSet_thenTransactionMustBeValid(int transactionId, String accountId,String trsType,String amount, String currencyCode) {
        TransactionId id = new TransactionId(transactionId);
        Currency currency = Currency.getInstance(currencyCode);
        TransactionType type = TransactionType.valueOf(trsType);
        BigDecimal money = new BigDecimal(amount);
        AccountId accountNumber = new AccountId(accountId);
        Transaction transaction = transactionFactory.create(id, accountNumber, money, type, currency, LocalDateTime.now());
        Errors errors = transaction.validate();
        assertThat(errors).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("transactionCreationTestParameters")
    void givenANewAccount_whenFieldISNotSet_thenAccountIsNotValid(TransactionId transactionId, AccountId accountId,TransactionType trsType,BigDecimal amount, Currency currency, String[] errors) {
        Transaction transaction = transactionFactory.create(transactionId, accountId, amount, trsType, currency, LocalDateTime.now());
        Errors errorList = transaction.validate();
        assertThat(errorList).containsExactly(errors);
    }

    public static Stream<Arguments> transactionCreationTestParameters() {

        String creditAccountIdError = Util.formatErrorMessage(ErrorCode.EXP001, "creditAccount", "Transaction");
        String debitAccountIdError = Util.formatErrorMessage(ErrorCode.EXP001, "debitAccount", "Transaction");
        String trsTypeError = Util.formatErrorMessage(ErrorCode.EXP001, "trsType", "Transaction");
        String currencyError = Util.formatErrorMessage(ErrorCode.EXP001, "currency", "Transaction");
        String currencyValidationError = Util.formatErrorMessage(ErrorCode.EXP004);

        return Stream.of(
                Arguments.of(transactionId, null, TransactionType.DEPOSIT, new BigDecimal(150), EUR, new String[]{creditAccountIdError}),
                Arguments.of(transactionId, null, TransactionType.WITHDRAWAL, new BigDecimal(150), EUR, new String[]{debitAccountIdError}),
                Arguments.of(transactionId, savingAccountId, null, new BigDecimal(150), EUR, new String[]{trsTypeError}),
                Arguments.of(transactionId, savingAccountId, TransactionType.DEPOSIT, new BigDecimal(150), null, new String[]{currencyError}),
                Arguments.of(transactionId, savingAccountId, TransactionType.DEPOSIT, new BigDecimal(150), Currency.getInstance("TND"), new String[]{currencyValidationError})
        );
    }

}
