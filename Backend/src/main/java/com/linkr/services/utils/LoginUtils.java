package com.linkr.services.utils;

import com.linkr.models.Credentials;
import org.apache.commons.codec.digest.DigestUtils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * The type Login utils.
 * @author Team Linkr
 * @version 1.0
 */
public class LoginUtils {

    /**
     * Compares given password to valid salted and hashed password.
     *
     * @param salt        salt to use on password to check before comparison.
     * @param validPass   .
     * @param passToCheck .
     * @return true if password matches after salted and hashed
     */
    public static boolean validatePassword(String salt, String validPass,
                                           String passToCheck) {
        System.out.println("Valid Pass: " + validPass);
        System.out.println("Salt: " + salt);
        System.out.println("Password To Check: " + passToCheck);

        String hashedPass = saltAndHashPass(passToCheck, salt);

        System.out.println("Password To Check SH: " + hashedPass);

        return validPass.equals(hashedPass);
    }

    /**
     * Salts and hashes password using SHA-256 hash function.
     *
     * @param password .
     * @param salt     .
     * @return salted and hashed password (SHA-256)
     */
    public static String saltAndHashPass(String password, String salt) {
        MessageDigest md;

        try {
            //Create Message Digest Object and Add salt offset
            md = MessageDigest.getInstance("SHA-256");
            md.update(DigestUtils.sha256(salt));

            //Perform digest
            byte[] hashedBytes = md.digest(DigestUtils.sha256(password));

            return DigestUtils.sha256Hex(hashedBytes);

        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * Creates a random salt.
     *
     * @return byte array to use as salt.
     */
    public static byte[] createSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    /**
     * Gets salt as string.
     *
     * @param salt the salt
     * @return salt salt string
     */
    public static String getSaltString(byte[] salt) {
        return DigestUtils.sha256Hex(salt);
    }


    /**
     * Salts and Hashed password given credential object.
     *
     * @param credentials .
     * @param pass        .
     */
    public static void setSaltAndHashedPass(Credentials credentials,
                                            String pass) {
        String salt = getSaltString(createSalt());
        String hashedPassAndSalt = saltAndHashPass(pass, salt);

        credentials.setPassword(hashedPassAndSalt);
        credentials.setSalt(salt);
    }


}
