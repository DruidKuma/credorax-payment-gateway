package com.credorax.paymentgateway.controller;

import com.credorax.paymentgateway.controller.dto.PaymentGatewayErrorDto;
import com.credorax.paymentgateway.controller.dto.PaymentTransactionResponseDto;
import com.credorax.paymentgateway.exception.PaymentValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

/**
 * Created by Yurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 09.08.2021
 */
@Slf4j
@RestControllerAdvice(assignableTypes = {
        PaymentController.class
})
public class PaymentExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public PaymentGatewayErrorDto handleException(Exception exception) {
        return new PaymentGatewayErrorDto(exception.getMessage(), Instant.now().toEpochMilli());
    }

    @ExceptionHandler(PaymentValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public PaymentTransactionResponseDto handleValidationException(PaymentValidationException exception) {
        return PaymentTransactionResponseDto.error(exception.getErrors());
    }
}
