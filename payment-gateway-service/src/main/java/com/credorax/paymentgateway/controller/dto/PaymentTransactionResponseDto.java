package com.credorax.paymentgateway.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

import java.util.Map;

/**
 * Created by Yurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 09.08.2021
 */
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentTransactionResponseDto {
    private boolean approved;
    private Map<String, String> errors;

    public static PaymentTransactionResponseDto error(Map<String, String> errors) {
        return new PaymentTransactionResponseDto(false, errors);
    }

    public static PaymentTransactionResponseDto ok() {
        return new PaymentTransactionResponseDto(true, null);
    }
}
