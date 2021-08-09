package com.credorax.paymentgateway.controller.dto;

import lombok.Builder;
import lombok.Value;

/**
 * Created by Yurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 09.08.2021
 */
@Value
@Builder(toBuilder = true)
public class CardDetailsDto {
    private String pan;
    private String expiry;
    private String cvv;
}
