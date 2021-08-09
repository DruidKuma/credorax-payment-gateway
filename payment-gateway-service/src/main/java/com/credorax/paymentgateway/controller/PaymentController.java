package com.credorax.paymentgateway.controller;

import com.credorax.paymentgateway.controller.dto.PaymentGatewayErrorDto;
import com.credorax.paymentgateway.controller.dto.PaymentRequestDto;
import com.credorax.paymentgateway.controller.dto.PaymentTransactionResponseDto;
import com.credorax.paymentgateway.converter.PaymentRequestDomainConverter;
import com.credorax.paymentgateway.service.PaymentTransactionService;
import com.credorax.paymentgateway.validation.PaymentRequestValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Submit a new payment transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction was submitted successfully", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentTransactionResponseDto.class)) }),
            @ApiResponse(responseCode = "400", description = "There was a validation exception during request processing", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentTransactionResponseDto.class)) }),
            @ApiResponse(responseCode = "500", description = "There was a server error during request processing", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentGatewayErrorDto.class)) }),
    })
    @PostMapping("/submit")
    public PaymentTransactionResponseDto submitPayment(@RequestBody PaymentRequestDto paymentRequestDto) {
        validator.validate(paymentRequestDto);

        paymentTransactionService.acceptTransaction(PaymentRequestDomainConverter.toDomainModel(paymentRequestDto));

        return PaymentTransactionResponseDto.ok();
    }

    @Operation(summary = "Get past transaction by invoice")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction was successfully found", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentRequestDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Transaction was not found", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentGatewayErrorDto.class)) }),
            @ApiResponse(responseCode = "500", description = "There was a server error during request processing", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentGatewayErrorDto.class)) }),
    })
    @GetMapping("/{invoice}")
    public PaymentRequestDto getPayment(@Parameter(description = "Invoice to find") @PathVariable Long invoice) {
        return PaymentRequestDomainConverter.toRequestDto(paymentTransactionService.getTransaction(invoice));
    }
}
