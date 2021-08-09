package com.credorax.paymentgateway.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by Yurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 09.08.2021
 */
class DataMaskingUtilsTest {

    @Test
    void testMaskAllCorrectlyMaskingAllCharacters() {
        String testValue = "Test Value";
        String expectedValue = "**********";

        String actualValue = DataMaskingUtils.maskAll(testValue);

        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    void testMaskAllButLastFourCorrectlyDoesntMaskShortInput() {
        String testValue = "1234";

        String actualValue = DataMaskingUtils.maskAllButLastFour(testValue);

        assertThat(actualValue).isEqualTo(testValue);
    }

    @Test
    void testMaskAllButLastFourCorrectlyMasksInput() {
        String testValue = "1234567890";
        String expectedValue = "******7890";

        String actualValue = DataMaskingUtils.maskAllButLastFour(testValue);

        assertThat(actualValue).isEqualTo(expectedValue);
    }
}
