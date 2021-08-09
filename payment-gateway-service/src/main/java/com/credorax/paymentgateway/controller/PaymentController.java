package com.credorax.paymentgateway.controller;

import com.credorax.paymentgateway.controller.dto.PaymentRequestDto;
import com.credorax.paymentgateway.controller.dto.PaymentTransactionResponseDto;
import com.credorax.paymentgateway.converter.PaymentRequestDomainConverter;
import com.credorax.paymentgateway.service.PaymentTransactionService;
import com.credorax.paymentgateway.validation.PaymentRequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Yurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 09.08.2021
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentRequestValidator validator;
    private final PaymentTransactionService paymentTransactionService;

    @PostMapping("/submit")
    public PaymentTransactionResponseDto submitPayment(@RequestBody PaymentRequestDto paymentRequestDto) {
        validator.validate(paymentRequestDto);

        paymentTransactionService.acceptTransaction(PaymentRequestDomainConverter.toDomainModel(paymentRequestDto));

        return PaymentTransactionResponseDto.ok();
    }

    @GetMapping("/{invoice}")
    public PaymentRequestDto getPayment(@PathVariable String invoice) {
        return PaymentRequestDomainConverter.toRequestDto(paymentTransactionService.getTransaction(invoice));
    }
}
