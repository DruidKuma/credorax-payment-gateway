package com.credorax.paymentgateway.service.model;

import lombok.Value;

/**
 * Created by Yurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 09.08.2021
 */
@Value
public class UserTransactionDomainModel {
    private Long invoice;
    private Long amount;
    private String currency;
    private CardholderDomainInfo cardholderInfo;
    private UserCardDomainInfo userCardInfo;

    @Value
    public static class CardholderDomainInfo {
        private String name;
        private String email;
    }

    @Value
    public static class UserCardDomainInfo {
        private String pan;
        private String expiry;
        private String cvv;
    }
}
