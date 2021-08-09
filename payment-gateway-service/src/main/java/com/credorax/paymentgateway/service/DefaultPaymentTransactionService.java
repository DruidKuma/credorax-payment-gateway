package com.credorax.paymentgateway.service;

import com.credorax.paymentgateway.converter.TransactionDomainPersistenceModelConverter;
import com.credorax.paymentgateway.dao.PaymentTransactionRepository;
import com.credorax.paymentgateway.exception.TransactionNotFoundException;
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

    private final PaymentTransactionRepository paymentTransactionRepository;

    @Override
    public void acceptTransaction(UserTransactionDomainModel transaction) {
        paymentTransactionRepository.save(TransactionDomainPersistenceModelConverter.toPersistenceModel(transaction));
    }

    @Override
    public UserTransactionDomainModel getTransaction(Long invoice) {
        return paymentTransactionRepository.findByInvoice(invoice)
                .map(TransactionDomainPersistenceModelConverter::toDomainModel)
                .orElseThrow(() -> new TransactionNotFoundException(invoice));
    }
}
