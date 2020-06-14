package com.assessment.authentication.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;

public class PasswordUtil {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int ITERATIONS = 512;
    private static final int KEY_LENGTH = 512;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA512";

    /**
     * generates a random String(salt) for the given length
     *
     * @param length
     * @return random String
     */
    public static String generateSalt(final int length) {
        byte[] salt = new byte[length];
        RANDOM.nextBytes(salt);

        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * hash the password. Uses the technique of salt and pepper for hashing.
     * Algorithm used is "PBKDF2WithHmacSHA512".
     *
     * @param password
     * @param salt
     * @param pepper
     * @return hashed String
     */
    public static String hashPassword(String password, String salt, String pepper) {

        char[] chars = password.toCharArray();
        byte[] bytes = (salt + pepper).getBytes();
        byte[] securePassword = new byte[1];

        PBEKeySpec spec = new PBEKeySpec(chars, bytes, ITERATIONS, KEY_LENGTH);

        Arrays.fill(chars, Character.MIN_VALUE);

        try {
            SecretKeyFactory fac = SecretKeyFactory.getInstance(ALGORITHM);
            securePassword = fac.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(securePassword);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            System.err.println("Exception encountered in hashPassword()");
        } finally {
            spec.clearPassword();
        }
        return Base64.getEncoder().encodeToString(securePassword);
    }

}
