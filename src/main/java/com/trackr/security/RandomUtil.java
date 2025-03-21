package com.trackr.security;

import java.security.SecureRandom;
import org.apache.commons.lang3.RandomStringUtils;

public class RandomUtil {
    private static final int DEF_COUNT = 20;

    private static final SecureRandom SECURE_RANDOM;

    static {
        SECURE_RANDOM = new SecureRandom();
        SECURE_RANDOM.nextBytes(new byte[64]);
    }

    private RandomUtil() {}

    public static String generateRandomAlphanumericString() {
        return RandomStringUtils.random(DEF_COUNT, 0, 0, true, true, null, SECURE_RANDOM);
    }

    /**
     * Generate a password.
     *
     * @return the generated password.
     */
    public static String generatePassword() {
        return generateRandomAlphanumericString();
    }

    /**
     * Generate an activation key.
     *
     * @return the generated activation key.
     */
    public static String generateActivationKey() {
        return generateRandomAlphanumericString();
    }

    /**
     * Generate a reset key.
     *
     * @return the generated reset key.
     */
    public static String generateResetKey() {
        return generateRandomAlphanumericString();
    }

    /**
     * Generate alphanumeric string with defined limit
     *
     * @return the generated alphanumeric string
     */
    public static String generateRandomString(int limit) {
        return RandomStringUtils.random(limit, 0, 0, true, true, null, SECURE_RANDOM);
    }
}
