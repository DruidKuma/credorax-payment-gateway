package com.credorax.paymentgateway.service;

import com.credorax.paymentgateway.service.model.UserTransactionDomainModel;
import lombok.RequiredArgsConstructor;

/**
 * Created by Yurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 09.08.2021
 */
@RequiredArgsConstructor
public class DefaultPaymentTransactionService implements PaymentTransactionService {

    @Override
    public void acceptTransaction(UserTransactionDomainModel transaction) {

    }

    @Override
    public UserTransactionDomainModel getTransaction(String invoice) {
        return null;
    }
}
