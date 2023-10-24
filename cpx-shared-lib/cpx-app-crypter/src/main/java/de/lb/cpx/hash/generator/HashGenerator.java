/*
 * Copyright (c) 2016 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner Health Care Consulting GmbH and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner Health Care Consulting GmbH
 * http://www.lohmann-birkner.de
 *
 * Contributors:
 *    2016  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.hash.generator;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Password encoding/decoding mechanism for CPX.
 *
 * @author Dirk Niemeier
 */
public class HashGenerator {

    private static final Logger LOG = Logger.getLogger(HashGenerator.class.getName());

    /**
     * instance
     */
    private static HashGenerator instance;

    /**
     * Singleton Factory
     *
     * @return instance
     */
    public static synchronized HashGenerator getInstance() {
        if (instance == null) {
            instance = new HashGenerator();
        }
        return instance;
    }

    /**
     * Generates a new hashed password with its salt
     *
     * @param pPlaintextValue plaintext value
     * @return Array[0] =&gt; Salted Hash Password, Array[1] =&gt; Salt (store
     * both!)
     */
    public String[] createSaltedHashPassword(final String pPlaintextValue) {
        final String plaintextValue = (pPlaintextValue == null ? "" : pPlaintextValue.trim());
        if (plaintextValue.isEmpty()) {
            throw new IllegalArgumentException("Plaintext value is null or empty!");
        }
        final String salt = generateSalt();
        final String hashedValue = hash(plaintextValue);
        final String saltedHashValue = hashSalted(hashedValue, salt);
        return new String[]{saltedHashValue, salt};
    }

    /**
     * Compares if a hashed password from login is identical to the salted hash
     * value stored in database
     *
     * @param pHashedValue Hashed password
     * @param pSalt Matching Salt for this password
     * @param pSaltedHashValue Salted hash password for comparison
     * @return true if the user password is correct
     */
    public boolean checkHashedPassword(final String pHashedValue, final String pSalt, final String pSaltedHashValue) {
        final String hashedValue = (pHashedValue == null ? "" : pHashedValue.trim());
        final String salt = (pSalt == null ? "" : pSalt.trim());
        final String saltedHashValue = (pSaltedHashValue == null ? "" : pSaltedHashValue.trim());
        if (hashedValue.isEmpty()) {
            throw new IllegalArgumentException("Hashed value is null or empty!");
        }
        if (salt.isEmpty()) {
            throw new IllegalArgumentException("Salt is null or empty!");
        }
        if (saltedHashValue.isEmpty()) {
            throw new IllegalArgumentException("Salted hash value is null or empty!");
        }

        final String saltedHashValue2 = hashSalted(hashedValue, salt);
        return (saltedHashValue.equals(saltedHashValue2));
    }

    /**
     * Compares if a plaintext(!) password from login is identical to the salted
     * hash value stored in database. It is highly recommanded not use this
     * method. Send an already hashed password to the server to verify it with
     * checkHashedPassword instead!
     *
     * @param pPlaintextValue Hashed password
     * @param pSalt Matching Salt for this password
     * @param pSaltedHashValue Salted hash password for comparison
     * @return true if the user password is correct
     */
    public boolean checkPlaintextPassword(final String pPlaintextValue, final String pSalt, final String pSaltedHashValue) {
        final String plaintextValue = (pPlaintextValue == null ? "" : pPlaintextValue.trim());
        final String salt = (pSalt == null ? "" : pSalt.trim());
        final String saltedHashValue = (pSaltedHashValue == null ? "" : pSaltedHashValue.trim());
        if (plaintextValue.isEmpty()) {
            throw new IllegalArgumentException("Hashed value is null or empty!");
        }
        if (salt.isEmpty()) {
            throw new IllegalArgumentException("Salt is null or empty!");
        }
        if (saltedHashValue.isEmpty()) {
            throw new IllegalArgumentException("Salted hash value is null or empty!");
        }

        final String hashedValue = hash(plaintextValue);
        return checkHashedPassword(hashedValue, salt, saltedHashValue);
    }

    /**
     * Hashes a (plaintext) string
     *
     * @param pPlaintextValue Password (plaintext)
     * @return hashed password
     */
    public String hash(String pPlaintextValue) {
        return hashSalted(pPlaintextValue, "");
    }

    /**
     * Generates a salted hash
     *
     * @param pHashedValue Hashed password
     * @param pSalt Salt
     * @return salted hash password
     */
    public String hashSalted(final String pHashedValue, final String pSalt) {
        String hashedValue = (pHashedValue == null) ? "" : pHashedValue.trim();
        String salt = (pSalt == null) ? "" : pSalt.trim();
        String saltedValue = hashedValue + salt;

        if (hashedValue.isEmpty()) {
            return "";
        }

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(saltedValue.getBytes("UTF-8")); // Change this to "UTF-16" if needed
            byte[] digest = md.digest();
            return String.format("%064x", new java.math.BigInteger(1, digest));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return "";
    }

    /**
     * Generates a random salt
     *
     * @return salt
     */
    public String generateSalt() {
        final Random r = new SecureRandom();
        byte[] salt = new byte[4];
        r.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

}
