package com.credorax.paymentgateway.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Yurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 09.08.2021
 */
@Entity
@Table(name = "user_transactions")
@Data
@Builder(toBuilder = true)
public class UserTransactionPersistenceModel {

    @Id
    @Column(name = "ut_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "ut_invoice")
    private Long invoice;

    @Column(name = "ut_amount")
    private Long amount;

    @Column(name = "ut_currency")
    private String currency;

    @Column(name = "ut_cardholder_name")
    private String cardholderName;

    @Column(name = "ut_cardholder_email")
    private String cardholderEmail;

    @Column(name = "ut_card_pan")
    private String pan;

    @Column(name = "ut_card_expiry")
    private String expiry;
}
