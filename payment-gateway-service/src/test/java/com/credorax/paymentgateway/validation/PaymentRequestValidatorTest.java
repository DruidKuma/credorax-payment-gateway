package com.credorax.paymentgateway.validation;

import com.credorax.paymentgateway.controller.dto.CardDetailsDto;
import com.credorax.paymentgateway.controller.dto.CardholderDetailsDto;
import com.credorax.paymentgateway.controller.dto.PaymentRequestDto;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.credorax.paymentgateway.validation.PaymentRequestValidator.AMOUNT_EMPTY_MESSAGE;
import static com.credorax.paymentgateway.validation.PaymentRequestValidator.AMOUNT_NON_POSITIVE_MESSAGE;
import static com.credorax.paymentgateway.validation.PaymentRequestValidator.CARDHOLDER_EMAIL_EMPTY_MESSAGE;
import static com.credorax.paymentgateway.validation.PaymentRequestValidator.CARDHOLDER_EMAIL_INCORRECT_MESSAGE;
import static com.credorax.paymentgateway.validation.PaymentRequestValidator.CARDHOLDER_NAME_EMPTY_MESSAGE;
import static com.credorax.paymentgateway.validation.PaymentRequestValidator.CARD_CVV_EMPTY_MESSAGE;
import static com.credorax.paymentgateway.validation.PaymentRequestValidator.CARD_EXPIRY_EMPTY_MESSAGE;
import static com.credorax.paymentgateway.validation.PaymentRequestValidator.CARD_PAN_EMPTY_MESSAGE;
import static com.credorax.paymentgateway.validation.PaymentRequestValidator.CURRENCY_EMPTY_MESSAGE;
import static com.credorax.paymentgateway.validation.PaymentRequestValidator.EXPIRY_DATE_INCORRECT_MESSAGE;
import static com.credorax.paymentgateway.validation.PaymentRequestValidator.EXPIRY_DATE_OLD_MESSAGE;
import static com.credorax.paymentgateway.validation.PaymentRequestValidator.INVOICE_EMPTY_MESSAGE;
import static com.credorax.paymentgateway.validation.PaymentRequestValidator.PAN_INCORRECT_LENGTH_MESSAGE;
import static com.credorax.paymentgateway.validation.PaymentRequestValidator.PAN_INCORRECT_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Yurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 09.08.2021
 */
class PaymentRequestValidatorTest {

    private static final Long INVOICE = 123L;
    private static final Long AMOUNT = 100000L;
    private static final String CURRENCY = "EUR";
    private static final String NAME = "John Smith";
    private static final String EMAIL = "jsmith@test.com";
    private static final String CARD_NUMBER = "5105105105105100";
    private static final String CARD_EXPIRY = "12/29";
    private static final String CARD_CVV = "111";

    private PaymentRequestValidator underTest = new PaymentRequestValidator();

    @Test
    void testCorrectDataDoesNotContainAnyValidationErrors() {
        PaymentRequestDto request = constructRequest(INVOICE, AMOUNT, CURRENCY, NAME, EMAIL, CARD_NUMBER, CARD_EXPIRY, CARD_CVV);

        Map<String, String> actualResult = underTest.validateRequestFields(request);

        assertThat(actualResult).isEmpty();
    }

    @Test
    void testResultErrorsContainsAllMessagesAboutAbsentFields() {
        PaymentRequestDto request = constructRequest(null, null, null, null, null, null, null, null);
        Map<String, String> expectedErrorMap = Map.of(
                "invoice", INVOICE_EMPTY_MESSAGE,
                "amount", AMOUNT_EMPTY_MESSAGE,
                "currency", CURRENCY_EMPTY_MESSAGE,
                "name", CARDHOLDER_NAME_EMPTY_MESSAGE,
                "email", CARDHOLDER_EMAIL_EMPTY_MESSAGE,
                "pan", CARD_PAN_EMPTY_MESSAGE,
                "expiry", CARD_EXPIRY_EMPTY_MESSAGE,
                "cvv", CARD_CVV_EMPTY_MESSAGE
        );

        Map<String, String> actualResult = underTest.validateRequestFields(request);

        assertThat(actualResult).containsExactlyInAnyOrderEntriesOf(expectedErrorMap);
    }

    @Test
    void testNonPositiveAmountFailsValidation() {
        PaymentRequestDto request = constructRequest(INVOICE, 0L, CURRENCY, NAME, EMAIL, CARD_NUMBER, CARD_EXPIRY, CARD_CVV);

        Map<String, String> actualResult = underTest.validateRequestFields(request);

        assertThat(actualResult).containsEntry("amount", AMOUNT_NON_POSITIVE_MESSAGE);
    }

    @Test
    void testResultContainsFailedEmailValidation() {
        PaymentRequestDto request = constructRequest(INVOICE, AMOUNT, CURRENCY, NAME, "jsmith@test", CARD_NUMBER, CARD_EXPIRY, CARD_CVV);

        Map<String, String> actualResult = underTest.validateRequestFields(request);

        assertThat(actualResult).containsEntry("email", CARDHOLDER_EMAIL_INCORRECT_MESSAGE);
    }

    @Test
    void testExpiryDateInIncorrectFormatFailsValidation() {
        PaymentRequestDto request = constructRequest(INVOICE, AMOUNT, CURRENCY, NAME, EMAIL, CARD_NUMBER, "0924", CARD_CVV);

        Map<String, String> actualResult = underTest.validateRequestFields(request);

        assertThat(actualResult).containsEntry("expiry", EXPIRY_DATE_INCORRECT_MESSAGE);
    }

    @Test
    void testOldExpiryDateFailsValidation() {
        PaymentRequestDto request = constructRequest(INVOICE, AMOUNT, CURRENCY, NAME, EMAIL, CARD_NUMBER, "09/18", CARD_CVV);

        Map<String, String> actualResult = underTest.validateRequestFields(request);

        assertThat(actualResult).containsEntry("expiry", EXPIRY_DATE_OLD_MESSAGE);
    }

    @Test
    void testShortCardNumberFailsValidation() {
        PaymentRequestDto request = constructRequest(INVOICE, AMOUNT, CURRENCY, NAME, EMAIL, "42424242", CARD_EXPIRY, CARD_CVV);

        Map<String, String> actualResult = underTest.validateRequestFields(request);

        assertThat(actualResult).containsEntry("pan", PAN_INCORRECT_LENGTH_MESSAGE);
    }

    @Test
    void testIncorrectCardNumberFailsValidation() {
        PaymentRequestDto request = constructRequest(INVOICE, AMOUNT, CURRENCY, NAME, EMAIL, "0000000000000000", CARD_EXPIRY, CARD_CVV);

        Map<String, String> actualResult = underTest.validateRequestFields(request);

        assertThat(actualResult).containsEntry("pan", PAN_INCORRECT_MESSAGE);
    }

    private PaymentRequestDto constructRequest(Long invoice, Long amount, String currency, String name, String email,
                                               String pan, String expiry, String cvv) {
        return PaymentRequestDto.builder()
                .invoice(invoice)
                .amount(amount)
                .currency(currency)
                .cardholder(CardholderDetailsDto.builder()
                        .name(name)
                        .email(email)
                        .build())
                .card(CardDetailsDto.builder()
                        .pan(pan)
                        .expiry(expiry)
                        .cvv(cvv)
                        .build())
                .build();
    }

}
