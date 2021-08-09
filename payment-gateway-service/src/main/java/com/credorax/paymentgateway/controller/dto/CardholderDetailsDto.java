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
public class CardholderDetailsDto {
    private String name;
    private String email;
}
