package com.credorax.paymentgateway.configuration;

import com.credorax.paymentgateway.dao.PaymentTransactionRepository;
import com.credorax.paymentgateway.service.DefaultPaymentTransactionService;
import com.credorax.paymentgateway.service.PaymentTransactionService;
import com.credorax.paymentgateway.validation.PaymentRequestValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Yurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 09.08.2021
 */
@Configuration
public class PaymentGatewayServiceConfiguration {

    @Bean
    public PaymentRequestValidator paymentRequestValidator() {
        return new PaymentRequestValidator();
    }

    @Bean
    public PaymentTransactionService transactionService(PaymentTransactionRepository paymentTransactionRepository) {
        return new DefaultPaymentTransactionService(paymentTransactionRepository);
    }

}
