package com.credorax.paymentgateway.validation;

import com.credorax.paymentgateway.controller.dto.CardDetailsDto;
import com.credorax.paymentgateway.controller.dto.CardholderDetailsDto;
import com.credorax.paymentgateway.controller.dto.PaymentRequestDto;
import com.credorax.paymentgateway.exception.PaymentValidationException;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.checkdigit.LuhnCheckDigit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Yurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 09.08.2021
 */
public class PaymentRequestValidator {

    public static final String INVOICE_EMPTY_MESSAGE = "Invoice number must be present";
    public static final String AMOUNT_EMPTY_MESSAGE = "Amount must be present";
    public static final String CURRENCY_EMPTY_MESSAGE = "Currency must be present";
    public static final String CARDHOLDER_EMPTY_MESSAGE = "Cardholder information must be present";
    public static final String CARDHOLDER_NAME_EMPTY_MESSAGE = "Cardholder name must be present";
    public static final String CARDHOLDER_EMAIL_EMPTY_MESSAGE = "Cardholder email must be present";
    public static final String CARD_EMPTY_MESSAGE = "Card information must be present";
    public static final String CARD_PAN_EMPTY_MESSAGE = "Card PAN must be present";
    public static final String CARD_EXPIRY_EMPTY_MESSAGE = "Card expiry date must be present";
    public static final String CARD_CVV_EMPTY_MESSAGE = "Card CVV number must be present";
    public static final String CARDHOLDER_EMAIL_INCORRECT_MESSAGE = "Cardholder email must be in a correct format";
    public static final String AMOUNT_NON_POSITIVE_MESSAGE = "Amount must be a positive number";
    public static final String PAN_INCORRECT_MESSAGE = "Card number is incorrect";
    public static final String PAN_INCORRECT_LENGTH_MESSAGE = "Card number length is incorrect";
    public static final String EXPIRY_DATE_INCORRECT_MESSAGE = "Card expiry date is incorrect";
    public static final String EXPIRY_DATE_OLD_MESSAGE = "Card expiry date must be after current day";

    public void validate(PaymentRequestDto paymentRequest) {
        Map<String, String> errors = validateRequestFields(paymentRequest);

        if (!errors.isEmpty()) {
            throw new PaymentValidationException(errors);
        }
    }

    public Map<String, String> validateRequestFields(PaymentRequestDto paymentRequest) {
        Map<String, String> errors = new HashMap<>();

        if (Objects.isNull(paymentRequest.getAmount())) errors.put("invoice", INVOICE_EMPTY_MESSAGE);

        if (Objects.isNull(paymentRequest.getAmount())) errors.put("amount", AMOUNT_EMPTY_MESSAGE);
        else if (paymentRequest.getAmount() <= 0) errors.put("amount", AMOUNT_NON_POSITIVE_MESSAGE);

        if (Objects.isNull(paymentRequest.getCurrency())) errors.put("currency", CURRENCY_EMPTY_MESSAGE);

        CardholderDetailsDto cardholder = paymentRequest.getCardholder();
        if (Objects.isNull(cardholder)) errors.put("cardholder", CARDHOLDER_EMPTY_MESSAGE);
        else {
            if (Objects.isNull(cardholder.getName())) errors.put("name", CARDHOLDER_NAME_EMPTY_MESSAGE);

            if (Objects.isNull(cardholder.getEmail())) errors.put("email", CARDHOLDER_EMAIL_EMPTY_MESSAGE);
            else if (!EmailValidator.getInstance().isValid(cardholder.getEmail())) errors.put("email", CARDHOLDER_EMAIL_INCORRECT_MESSAGE);
        }

        CardDetailsDto card = paymentRequest.getCard();
        if (Objects.isNull(card)) errors.put("card", CARD_EMPTY_MESSAGE);
        else {
            if (Objects.isNull(card.getPan())) errors.put("pan", CARD_PAN_EMPTY_MESSAGE);
            else if (card.getPan().length() != 16) errors.put("pan", PAN_INCORRECT_LENGTH_MESSAGE);
            else if (!LuhnCheckDigit.LUHN_CHECK_DIGIT.isValid(card.getPan())) errors.put("pan", PAN_INCORRECT_MESSAGE);

            if (Objects.isNull(card.getExpiry())) errors.put("expiry", CARD_EXPIRY_EMPTY_MESSAGE);
            else validateCardExpiryDate(card.getExpiry(), errors);

            if (Objects.isNull(card.getCvv())) errors.put("cvv", CARD_CVV_EMPTY_MESSAGE);
        }

        return errors;
    }

    private void validateCardExpiryDate(String expiryDate, Map<String, String> errors) {
        try {
            if (new SimpleDateFormat("MM/yy").parse(expiryDate).before(new Date()))
                errors.put("expiry", EXPIRY_DATE_OLD_MESSAGE);
        } catch (ParseException e) {
            errors.put("expiry", EXPIRY_DATE_INCORRECT_MESSAGE);
        }
    }
}
