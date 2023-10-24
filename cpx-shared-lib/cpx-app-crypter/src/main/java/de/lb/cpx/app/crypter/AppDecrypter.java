/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.app.crypter;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

/**
 * Password encoding/decoding mechanism for CP. AppDecrypter is a 1:1 copy from
 * Checkpoint!
 *
 * @author niemeier
 */
public abstract class AppDecrypter implements Serializable {

    private static final Logger LOG = Logger.getLogger(AppDecrypter.class.getName());
//    private static final String PASSWORD = "tdpkrlkbssjk_solar117_vft531.,antonia";
    private static final byte[] SALT = {(byte) 0xd, (byte) 0x7d1, (byte) 0x10, (byte) 0x7d5, (byte) 0x7b8, (byte) 0x7b7, (byte) 0x29, (byte) 0x5e5};
    //private static final int ITERATIONS = 20;

    /**
     * Verwendete Zeichendecodierung
     */
    private static final String CHARSET = "UTF16";

    private static final long serialVersionUID = 1L;

    /**
     * Notwendige Instanczen
     */
    private Cipher encryptCipher;
    private Cipher decryptCipher;

    protected AppDecrypter() {
    }

    public abstract int getIterations();

    protected abstract String getPassword();

    /**
     * Initialisiert den Verschlüsselungsmechanismus
     *
     * @throws SecurityException security problem
     */
    protected final void init() throws SecurityException {
        final char[] pass = getPassword().toCharArray();
        try {
            final PBEParameterSpec ps = new PBEParameterSpec(SALT, getIterations());
            final SecretKeyFactory kf = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            final SecretKey k = kf.generateSecret(new PBEKeySpec(pass));
            encryptCipher = Cipher.getInstance("PBEWithMD5AndDES/CBC/PKCS5Padding");
            encryptCipher.init(Cipher.ENCRYPT_MODE, k, ps);
            decryptCipher = Cipher.getInstance("PBEWithMD5AndDES/CBC/PKCS5Padding");
            decryptCipher.init(Cipher.DECRYPT_MODE, k, ps);
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException e) {
            throw new SecurityException("Could not initialize CryptoLibrary: "
                    + e.getMessage(), e);
        }
    }

    /**
     * Verschlüsselt eine Zeichenkette
     *
     * @param pStrToEncrypt value to encrypt
     * @return String the encrypted string.
     * @exception SecurityException Description of the Exception
     */
    public final String encrypt(final String pStrToEncrypt) throws SecurityException {
        if (pStrToEncrypt == null || pStrToEncrypt.isEmpty()) {
            return pStrToEncrypt;
        }
        try {
            byte[] b = pStrToEncrypt.getBytes(CHARSET);
            byte[] enc = encryptCipher(b);
            return Base64.getEncoder().encodeToString(enc);
        } catch (UnsupportedEncodingException | BadPaddingException | IllegalBlockSizeException | IllegalArgumentException e) {
            throw new SecurityException("Could not encrypt: " + e.getMessage(), e);
        }
    }

    //2019-10-18 DNi - Ticket CPX-2008: Cipher is not thread-safe. Use synchronized key word!
    private synchronized byte[] encryptCipher(byte[] b) throws IllegalBlockSizeException, BadPaddingException {
        byte[] enc = encryptCipher.doFinal(b);
        return enc;
    }

    //2019-10-18 DNi - Ticket CPX-2008: Cipher is not thread-safe. Use synchronized key word!
    private synchronized byte[] decryptCipher(byte[] dec) throws IllegalBlockSizeException, BadPaddingException {
        decryptCipher.doFinal(); //first non functional call because of CPX-2008 (this workaround rly helps, hell yeah!)
        byte[] b = decryptCipher.doFinal(dec);
        return b;
    }

    /**
     * same as encrypt method, but does not throw exception (returns input
     * parameter if already encrypted)
     *
     * @param pStrToEncrypt value to encrypt
     * @return encrypted string
     */
    public String encrypt2(final String pStrToEncrypt) {
        try {
            return encrypt(pStrToEncrypt);
        } catch (SecurityException ex) {
            LOG.log(Level.FINEST, "Password ist already encrypted!", ex);
        }
        return pStrToEncrypt;
    }

    /**
     * same as decrypt method, but does not throw exception (returns input
     * parameter if already decrypted)
     *
     * @param pStrToDecrypt value to decrypt
     * @return decrypted string
     */
    public String decrypt2(final String pStrToDecrypt) {
        try {
            return decrypt(pStrToDecrypt);
        } catch (SecurityException ex) {
            LOG.log(Level.FINEST, "Password is already decrypted!", ex);
        }
        return pStrToDecrypt;
    }

    /**
     * Entschlüsselt eine Zeichenkette, welche mit der Methode encrypt
     * verschlüsselt wurde.
     *
     * @param pStrToDecrypt value to decrypt
     * @return String the encrypted string.
     * @exception SecurityException Description of the Exception
     */
    public final String decrypt(final String pStrToDecrypt) throws SecurityException {
//        if (pStrToDecrypt == null) {
//            LOG.log(Level.INFO, "password to decrypt is null!");
//        } else {
//            LOG.log(Level.INFO, "password to decrypt: '" + pStrToDecrypt + "'" + " -> " + pStrToDecrypt.length());
//        }
        if (pStrToDecrypt == null || pStrToDecrypt.isEmpty()) {
            return pStrToDecrypt;
        }
        try {
            byte[] dec = Base64.getDecoder().decode(pStrToDecrypt);
//            LOG.log(Level.INFO, "dec: " + new String(dec) + " -> " + new String(dec).length());
            byte[] b = decryptCipher(dec);
//            LOG.log(Level.INFO, "b: " + new String(b) + " -> " + new String(b).length());
            String result = new String(b, CHARSET);
//            LOG.log(Level.INFO, "result of password decryption: '" + result + "'" + " -> " + result.length());
            return result;
        } catch (UnsupportedEncodingException | BadPaddingException | IllegalBlockSizeException | IllegalArgumentException e) {
            throw new SecurityException("Could not decrypt: " + e.getMessage(), e);
        }
    }

    public boolean isDecrypted(final String pPasswordCandidate) {
        final String candidate = (pPasswordCandidate == null) ? "" : pPasswordCandidate.trim();
        String decodedCandidate = candidate;
        try {
            decodedCandidate = decrypt(candidate);
        } catch (SecurityException ex) {
            LOG.log(Level.FINEST, "Unencrypted password found!", ex);
        }
        final boolean isDecrypted = candidate.equalsIgnoreCase(decodedCandidate);
        return isDecrypted;
    }

    public boolean isEncrypted(final String pPasswordCandidate) {
        return !isDecrypted(pPasswordCandidate);
    }

}
