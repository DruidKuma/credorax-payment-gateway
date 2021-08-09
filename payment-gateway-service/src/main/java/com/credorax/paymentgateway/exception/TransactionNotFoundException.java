package com.credorax.paymentgateway.exception;

import lombok.Getter;

/**
 * Created by Yurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 09.08.2021
 */
@Getter
public class TransactionNotFoundException extends RuntimeException {

    private final Long invoice;

    public TransactionNotFoundException(Long invoice) {
        super(String.format("Transaction with provided invoice (%d) can not be found", invoice));
        this.invoice = invoice;

    }
}
