package com.credorax.paymentgateway.service;

import com.credorax.paymentgateway.service.model.UserTransactionDomainModel;


/**
 * Created by Yurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 09.08.2021
 */
public interface PaymentTransactionService {
    void acceptTransaction(UserTransactionDomainModel transaction);
    UserTransactionDomainModel getTransaction(Long invoice);
}
