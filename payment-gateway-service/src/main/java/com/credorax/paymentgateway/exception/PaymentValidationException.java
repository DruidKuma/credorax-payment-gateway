package com.credorax.paymentgateway.exception;

import lombok.Getter;

import java.util.Map;

/**
 * Created by Yurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 09.08.2021
 */
@Getter
public class PaymentValidationException extends RuntimeException {

    private Map<String, String> errors;

    public PaymentValidationException(Map<String, String> errors) {
        this.errors = errors;
    }
}
