package com.credorax.paymentgateway.converter;

import com.credorax.paymentgateway.entity.UserTransactionPersistenceModel;
import com.credorax.paymentgateway.service.model.UserTransactionDomainModel;
import com.credorax.paymentgateway.util.DataMaskingUtils;
import lombok.experimental.UtilityClass;

/**
 * Created by Yurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 09.08.2021
 */
@UtilityClass
public class TransactionDomainPersistenceModelConverter {

    public static UserTransactionDomainModel toDomainModel(UserTransactionPersistenceModel persistenceModel) {
        return UserTransactionDomainModel.builder()
                .invoice(persistenceModel.getInvoice())
                .currency(persistenceModel.getCurrency())
                .amount(persistenceModel.getAmount())
                .userCardInfo(UserTransactionDomainModel.UserCardDomainInfo.builder()
                        .pan(persistenceModel.getPan())
                        .expiry(persistenceModel.getExpiry())
                        .build())
                .cardholderInfo(UserTransactionDomainModel.CardholderDomainInfo.builder()
                        .name(persistenceModel.getCardholderName())
                        .email(persistenceModel.getCardholderEmail())
                        .build())
                .build();
    }

    public static UserTransactionPersistenceModel toPersistenceModel(UserTransactionDomainModel domainModel) {
        return UserTransactionPersistenceModel.builder()
                .invoice(domainModel.getInvoice())
                .amount(domainModel.getAmount())
                .currency(domainModel.getCurrency())
                .cardholderName(DataMaskingUtils.maskAll(domainModel.getCardholderInfo().getName()))
                .cardholderEmail(domainModel.getCardholderInfo().getEmail())
                .pan(DataMaskingUtils.maskAllButLastFour(domainModel.getUserCardInfo().getPan()))
                .expiry(DataMaskingUtils.maskAll(domainModel.getUserCardInfo().getExpiry()))
                .build();
    }
}
