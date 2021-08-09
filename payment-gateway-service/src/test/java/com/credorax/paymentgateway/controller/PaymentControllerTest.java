package com.credorax.paymentgateway.controller;

import com.credorax.paymentgateway.controller.dto.CardDetailsDto;
import com.credorax.paymentgateway.controller.dto.CardholderDetailsDto;
import com.credorax.paymentgateway.controller.dto.PaymentGatewayErrorDto;
import com.credorax.paymentgateway.controller.dto.PaymentRequestDto;
import com.credorax.paymentgateway.controller.dto.PaymentTransactionResponseDto;
import com.credorax.paymentgateway.exception.PaymentValidationException;
import com.credorax.paymentgateway.exception.TransactionNotFoundException;
import com.credorax.paymentgateway.service.PaymentTransactionService;
import com.credorax.paymentgateway.service.model.UserTransactionDomainModel;
import com.credorax.paymentgateway.validation.PaymentRequestValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Map;

import static com.credorax.paymentgateway.validation.PaymentRequestValidator.CARDHOLDER_EMAIL_INCORRECT_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Yurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 09.08.2021
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest
class PaymentControllerTest {

    private static final String SUBMIT_TRANSACTION_URL = "/api/payments/submit";
    private static final String GET_TRANSACTION_URL = "/api/payments/{invoice}";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PaymentTransactionService paymentTransactionService;

    @MockBean
    private PaymentRequestValidator paymentRequestValidator;

    @Test
    void testCorrectDataSubmitsSuccessfullyAndReturnsCorrectResponse() throws Exception {
        PaymentRequestDto request = PaymentRequestDto.builder()
                .invoice(123L)
                .amount(100000L)
                .currency("EUR")
                .cardholder(CardholderDetailsDto.builder()
                        .name("John Smith")
                        .email("jsmith@test.com")
                        .build())
                .card(CardDetailsDto.builder()
                        .pan("5105105105105100")
                        .expiry("12/29")
                        .cvv("111")
                        .build())
                .build();
        PaymentTransactionResponseDto expectedResponse = PaymentTransactionResponseDto.ok();

        doNothing().when(paymentTransactionService).acceptTransaction(any(UserTransactionDomainModel.class));

        mockMvc.perform(post(SUBMIT_TRANSACTION_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonBodyEqualTo(expectedResponse));
    }

    @Test
    void testIncorrectDataReturnsCorrectStatusAndErrorInResponse() throws Exception {
        PaymentRequestDto request = PaymentRequestDto.builder()
                .invoice(123L)
                .amount(100000L)
                .currency("EUR")
                .cardholder(CardholderDetailsDto.builder()
                        .name("John Smith")
                        .email("jsmith@test")
                        .build())
                .card(CardDetailsDto.builder()
                        .pan("5105105105105100")
                        .expiry("12/29")
                        .cvv("111")
                        .build())
                .build();
        PaymentTransactionResponseDto expectedResponse = PaymentTransactionResponseDto.error(Map.of("email", CARDHOLDER_EMAIL_INCORRECT_MESSAGE));

        doThrow(new PaymentValidationException(Map.of("email", CARDHOLDER_EMAIL_INCORRECT_MESSAGE))).when(paymentRequestValidator).validate(any(PaymentRequestDto.class));

        mockMvc.perform(post(SUBMIT_TRANSACTION_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonBodyEqualTo(expectedResponse));
    }

    @Test
    void testInternalServerErrorResponseReturnedCorrectlyWhenThereIsAnUnhandledError() throws Exception {
        PaymentRequestDto request = PaymentRequestDto.builder()
                .invoice(123L)
                .amount(100000L)
                .currency("EUR")
                .cardholder(CardholderDetailsDto.builder()
                        .name("John Smith")
                        .email("jsmith@test.com")
                        .build())
                .card(CardDetailsDto.builder()
                        .pan("5105105105105100")
                        .expiry("12/29")
                        .cvv("111")
                        .build())
                .build();

        doThrow(new RuntimeException("Internal error")).when(paymentTransactionService).acceptTransaction(any(UserTransactionDomainModel.class));

        mockMvc.perform(post(SUBMIT_TRANSACTION_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message", Matchers.is("Internal error")));
    }

    @Test
    void testTransactionReturnsCorrectlyWhenFound() throws Exception {
        PaymentRequestDto expectedRequest = PaymentRequestDto.builder()
                .invoice(123L)
                .amount(100000L)
                .currency("EUR")
                .cardholder(CardholderDetailsDto.builder()
                        .name("**********")
                        .email("jsmith@test.com")
                        .build())
                .card(CardDetailsDto.builder()
                        .pan("************5100")
                        .expiry("*****")
                        .build())
                .build();

        UserTransactionDomainModel domainModel = UserTransactionDomainModel.builder()
                .cardholderInfo(UserTransactionDomainModel.CardholderDomainInfo.builder()
                        .name("**********")
                        .email("jsmith@test.com")
                        .build())
                .userCardInfo(UserTransactionDomainModel.UserCardDomainInfo.builder()
                        .pan("************5100")
                        .expiry("*****")
                        .build())
                .amount(100000L)
                .currency("EUR")
                .invoice(123L)
                .build();

        when(paymentTransactionService.getTransaction(123L)).thenReturn(domainModel);

        mockMvc.perform(get(GET_TRANSACTION_URL, "123"))
                .andExpect(status().isOk())
                .andExpect(jsonBodyEqualTo(expectedRequest));
    }

    @Test
    void testNotFoundResponseIsCorrectWhenTransactionIsNotFound() throws Exception {
        doThrow(new TransactionNotFoundException(123L)).when(paymentTransactionService).getTransaction(123L);

        mockMvc.perform(get(GET_TRANSACTION_URL, "123"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", Matchers.is("Transaction with provided invoice (123) can not be found")));
    }


    private ResultMatcher jsonBodyEqualTo(Object expectedObject) {
        return mvcResult -> {
            String responseString = mvcResult.getResponse().getContentAsString();
            Object actualResponse = objectMapper.readValue(responseString, expectedObject.getClass());
            assertThat(actualResponse).isEqualTo(expectedObject);
        };
    }
}
