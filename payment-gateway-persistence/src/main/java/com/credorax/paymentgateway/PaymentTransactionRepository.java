package com.credorax.paymentgateway;

import com.credorax.paymentgateway.entity.UserTransactionPersistenceModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by Yurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 09.08.2021
 */
public interface PaymentTransactionRepository extends JpaRepository<UserTransactionPersistenceModel, Long> {
    Optional<UserTransactionPersistenceModel> findByInvoice(String invoice);
}
