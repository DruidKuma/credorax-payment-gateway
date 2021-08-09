package com.credorax.paymentgateway.controller.dto;

import lombok.Value;

/**
 * Created by Yurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 09.08.2021
 */
@Value
public class PaymentRequestDto {
    private Long invoice;
    private Long amount;
    private String currency;
    private CardholderDetailsDto cardholder;
    private CardDetailsDto card;
}
