package com.sg.kata.model.account;

import com.sg.kata.model.customer.CustomerId;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class AccountIdTest {

    @ParameterizedTest
    @ValueSource(strings = {"1234", "FFFSFSDFSFSDFSDFSDFSDFSDFSDFSD", "0"})
    void givenAValueLengthLessThanConfiguredLength_newAccountId_throwsException(String value) {
        ThrowableAssert.ThrowingCallable invocation = () -> new AccountId(value);

        assertThatIllegalArgumentException().isThrownBy(invocation);
    }

    @ParameterizedTest
    @ValueSource(strings = {"00000012345EUR123456","00000012345EUR654321"})
    void givenAValueLengthEqualsToConfiguredLength_newAccountId_succeeds(String value) {
        AccountId accountId = new AccountId(value);

        assertThat(accountId.value()).isEqualTo(value);
    }

}
