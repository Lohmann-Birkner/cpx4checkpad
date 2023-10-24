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

/**
 * Password encoding/decoding mechanism for CP. AppDecrypter is a 1:1 copy from
 * Checkpoint!
 *
 * @author niemeier
 */
public class PasswordDecrypter extends AppDecrypter {

    private static final long serialVersionUID = 1L;
    /**
     * instance
     */
    private static AppDecrypter instance;

    /**
     * Singleton Factory
     *
     * @return instance
     */
    public static synchronized AppDecrypter getInstance() {
        if (instance == null) {
            instance = new PasswordDecrypter();
            instance.init();
        }
        return instance;
    }

    @Override
    public int getIterations() {
        return 20;
    }

    @Override
    protected String getPassword() {
        return "tdpkrlkbssjk_solar117_vft531.,antonia";
    }

    //multi-threading test: cipher is not thread-safe and can create corrupted decrypted passwords if it is not synchronized!
//    public static void main(String[] args) {
//        final String pw1 = "rAwnLzQFtbLQLty4ijW3cMGLGrNkoTmU";
//        final String exp1 = "Sbbcpx*18";
//
//        final String pw2 = "tDcNAppQcA5ruD1my1cNXw==";
//        final String exp2 = "oracle";
//
//        final String pw3 = "bZEaCeljBIY=";
//        final String exp3 = "sb";
//
//        int limit = 10000;
//        for(int i = 1; i <= 100; i++) {
//            final int j = i;
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    for(int k = 1; k <= limit; k++) {
//                        final String pw;
//                        final String exp;
//                        SecureRandom r = new SecureRandom();
//                        int rand = r.nextInt(9);
//                        if (rand <= 3) {
//                            pw = pw1;
//                            exp = exp1;
//                        } else if (rand <= 6) {
//                            pw = pw2;
//                            exp = exp2;
//                        } else {
//                            pw = pw3;
//                            exp = exp3;
//                        }
//                        
//                        final String dec = getInstance().decrypt(pw);
//                        if (!dec.equals(exp)) {
//                            //LOG.log(Level.SEVERE, "wrong result: " + dec);
//                            System.err.println("wrong result: " + dec);
//                        }
//                    }
//                }
//            }).start();
//        }
//    }
//
//    public static void main(String[] args) {
//        String expected = "Sbbcpx*18";
//        AppDecrypter inst = AppDecrypter.getInstance();
//        final String filename = "E:\\Temp\\test.ser";
//        for (int i = 1; i <= Long.MAX_VALUE; i++) {
//            String bla = AppDecrypter.getInstance().decrypt("rAwnLzQFtbLQLty4ijW3cMGLGrNkoTmU");
//            LOG.log(Level.INFO, "Password " + i + ": " + bla);
//            if (!bla.equals(expected)) {
//                LOG.log(Level.SEVERE, "ERROR!");
//                break;
//            }
//            serialize(filename, inst);
//            inst = unserialize(filename);
//        }
//    }
//
//    private static void serialize(String filename, AppDecrypter p) {
//        // save the object to file
//        FileOutputStream fos = null;
//        ObjectOutputStream out = null;
//        try {
//            fos = new FileOutputStream(filename);
//            out = new ObjectOutputStream(fos);
//            out.writeObject(p);
//            out.close();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    private static AppDecrypter unserialize(String filename) {
//        // read the object from file
//        // save the object to file
//        FileInputStream fis = null;
//        ObjectInputStream in = null;
//        AppDecrypter p = null;
//        try {
//            fis = new FileInputStream(filename);
//            in = new ObjectInputStream(fis);
//            p = (AppDecrypter) in.readObject();
//            in.close();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return p;
//    }
//
//    public static void main(String[] args) {
//        final String decoded = "Sbbcpx*18";
//        final String encoded = "rAwnLzQFtbLQLty4ijW3cMGLGrNkoTmU";
//        System.out.println(getInstance().decrypt2(decoded) + " -> " + decoded);
//        System.out.println(getInstance().encrypt2(getInstance().decrypt2(decoded)) + " -> " + encoded);
//        System.out.println(getInstance().encrypt2(decoded) + " -> " + encoded);
//        System.out.println(getInstance().decrypt2(encoded) + " -> " + decoded);
//        System.out.println(getInstance().decrypt2(getInstance().encrypt2(decoded)) + " -> " + decoded);
//        System.out.println(getInstance().isDecrypted(decoded) + " -> true");
//        System.out.println(getInstance().isDecrypted(encoded) + " -> false");
//        System.out.println(getInstance().isEncrypted(decoded) + " -> false");
//        System.out.println(getInstance().isEncrypted(encoded) + " -> true");
//    }
}
