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
 *    2017  nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.cpx.license.crypter;

import de.lb.cpx.app.crypter.AppDecrypter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nandola
 */
public class LicenseDecrypter extends AppDecrypter {

    private static final long serialVersionUID = 1L;
//    private final transient static String PASSWORD = "tdpkrlkbssjk_solar117_vft531.,nandola";
    //private static final String PASSWORD = "sdgfbsdhufgskdhusdhshjdq2sdjklsdhfsdh676347563465efnbsdhsd";
    //private static final byte[] SALT = {(byte) 0xd, (byte) 0x7d1, (byte) 0x10, (byte) 0x7d5, (byte) 0x7b8, (byte) 0x7b7, (byte) 0x29, (byte) 0x5e5};
//    final int iterations = 3;
    //private static final int ITERATIONS = 5;
    //private static final String CHARSET = "UTF16";

    /**
     * instance
     */
    private static LicenseDecrypter instance;

    @Override
    public int getIterations() {
        return 5;
    }

    @Override
    protected String getPassword() {
        return "sdgfbsdhufgskdhusdhshjdq2sdjklsdhfsdh676347563465efnbsdhsd";
    }

    /**
     * Singleton Factory
     *
     * @return instance
     */
    public static synchronized LicenseDecrypter getInstance() {
        if (instance == null) {
            instance = new LicenseDecrypter();
            instance.init();
        }
        return instance;
    }

    public synchronized String encryptHex(String str) throws SecurityException, UnsupportedEncodingException {
        try {
            str = encrypt(str);
            return toHex(str.getBytes("Cp1252"));
        } catch (SecurityException e) {
            throw new SecurityException("Could not encrypt: " + e.getMessage(), e);
        }
    }

    /**
     * Converts the given array of bytes to a hex String
     *
     * @param buf Buffer
     * @return Hex String
     */
    public static String toHex(byte[] buf) {
        String res = "";
        for (int i = 0; i < buf.length; i++) {
            int b = buf[i];
            if (b < 0) {
                res = res.concat("-");
                b = -b;
            }
            if (b < 16) {
                res = res.concat("0");
            }
            res = res.concat(Integer.toHexString(b).toUpperCase());
        }
        return res;
    }

    /**
     * Converts the given array of bytes to a hex String
     *
     * @param pref Prefix
     * @param buf Buffer
     * @return Hexcoded result
     */
    public static String toHex(String pref, byte[] buf) {
        String res = "";
        for (int i = 0; i < buf.length; i++) {
            int b = buf[i];
            if (b < 0) {
                res = res.concat("-");
                b = -b;
            }
            if (b < 16) {
                res = res.concat("0");
            }
            res += pref + (Integer.toHexString(b).toUpperCase());
        }
        return res;
    }

    public synchronized String decryptHex(String str) throws SecurityException, UnsupportedEncodingException {
        try {
            str = new String(toByteArray(str), "Cp1252");
            return decrypt(str);
        } catch (SecurityException e) {
            throw new SecurityException("Could not decrypt: " + e.getMessage(), e);
        }
    }

    /**
     * Converts the given hex String to an array of bytes
     *
     * @param hex Hexcoded input
     * @return decoded hex
     */
    public static byte[] toByteArray(String hex) {
        List<Byte> res = new ArrayList<>();
        String part;
        int pos = 0;
        int len = 0;
        while (pos < hex.length()) {
            int test;
            len = (("-".equals(hex.substring(pos, pos + 1))) ? 3 : 2);
            part = hex.substring(pos, pos + len);
            pos += len;
            try {

                test = Integer.parseInt(part, 16);
            } catch (NumberFormatException e) {
                return new byte[0];
            }
            res.add((byte) test);
        }
        if (!res.isEmpty()) {
            byte[] b = new byte[res.size()];
            for (int i = 0; i < res.size(); i++) {
                Byte a = res.get(i);
                b[i] = a;
            }
            return b;
        } else {
            return new byte[0];
        }
    }

//    // just for testing
//    public static void main(String[] args) {
////        String license = "467751722F496D74727239575554344477675A68676661317468562F3577524C0A7438644B65774E4B686E6E6B6D466572596475734A444F474965485656697030544A6D64723333384E2B485965306179683651426E673D3D0A";
////        Decrypter.getInstance().decryptHex(license);
//
////        Decrypter decrypter = Decrypter.getInstance();
//////        final String encrypted = decrypter.encrypt("+Thomas Dehne Softwareentwicklung");
////        final String encrypted = decrypter.encrypt("+Lohmann & Birkner Softwareentwicklung");
////        System.out.println("Encoded :" + encrypted);
////        final String decrypted = decrypter.decrypt(encrypted);
////        System.out.println("Decoded :" + decrypted);
//        System.out.println(LicenseDecrypter.getInstance().decrypt("FwQr/Imtrr9WUT4DwgZhgfa1thV/5wRL"));
//        System.out.println(LicenseDecrypter.getInstance().decrypt("t8dKewNKhnnkmFerYdusJDOGIeHVVip0TJmdr338N+HYe0ayh6QBng=="));
//    }
}
