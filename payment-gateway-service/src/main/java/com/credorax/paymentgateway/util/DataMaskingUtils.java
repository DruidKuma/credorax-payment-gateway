package com.credorax.paymentgateway.util;

import lombok.experimental.UtilityClass;

import java.util.Collections;

/**
 * Created by Yurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 09.08.2021
 */
@UtilityClass
public class DataMaskingUtils {

    public static String maskAll(String input) {
        return String.join(input, Collections.nCopies(input.length(), "*"));
    }

    public static String maskAllButLastFour(String input) {
        if (input.length() <= 4) return input;

        char[] charsValue = input.toCharArray();
        for (int i = 0; i < charsValue.length - 4; i++) {
            charsValue[i] = '*';
        }

        return new String(charsValue);
    }
}
