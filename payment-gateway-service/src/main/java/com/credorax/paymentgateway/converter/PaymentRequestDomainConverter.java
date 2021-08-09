package com.credorax.paymentgateway.converter;

import com.credorax.paymentgateway.controller.dto.CardDetailsDto;
import com.credorax.paymentgateway.controller.dto.CardholderDetailsDto;
import com.credorax.paymentgateway.controller.dto.PaymentRequestDto;
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
public class PaymentRequestDomainConverter {

    public static UserTransactionDomainModel toDomainModel(PaymentRequestDto requestDto) {
        return UserTransactionDomainModel.builder()
                .invoice(requestDto.getInvoice())
                .amount(requestDto.getAmount())
                .currency(requestDto.getCurrency())
                .cardholderInfo(UserTransactionDomainModel.CardholderDomainInfo.builder()
                        .email(requestDto.getCardholder().getEmail())
                        .name(requestDto.getCardholder().getName())
                        .build())
                .userCardInfo(UserTransactionDomainModel.UserCardDomainInfo.builder()
                        .pan(requestDto.getCard().getPan())
                        .expiry(requestDto.getCard().getExpiry())
                        .cvv(requestDto.getCard().getCvv())
                        .build())
                .build();
    }

    public static PaymentRequestDto toRequestDto(UserTransactionDomainModel domainModel) {
        return PaymentRequestDto.builder()
                .invoice(domainModel.getInvoice())
                .amount(domainModel.getAmount())
                .currency(domainModel.getCurrency())
                .cardholder(CardholderDetailsDto.builder()
                        .name(DataMaskingUtils.maskAll(domainModel.getCardholderInfo().getName()))
                        .email(domainModel.getCardholderInfo().getEmail())
                        .build())
                .card(CardDetailsDto.builder()
                        .pan(DataMaskingUtils.maskAllButLastFour(domainModel.getUserCardInfo().getPan()))
                        .expiry(DataMaskingUtils.maskAll(domainModel.getUserCardInfo().getExpiry()))
                        .build())
                .build();
    }
}
