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
package de.lb.cpx.sap.importer.utils;

import de.lb.cpx.str.utils.StrUtils;
import dto.RmeDiagnose;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author niemeier
 */
public class SapStrUtils extends StrUtils {

    public static final String FORMAT_DATETIME = "yyyyMMddHHmmss";
    public static final String FORMAT_DATE = "yyyyMMdd";
    public static final String FORMAT_TIME = "HHmmss";
    private static final Logger LOG = Logger.getLogger(SapStrUtils.class.getName());

    /**
     *
     * @param pDate date
     * @return date without time
     */
    public static String getDatumOhneUhrzeit(final Date pDate) {
        if (pDate == null) {
            return "";
        }
        SimpleDateFormat lDateFormat = new SimpleDateFormat(FORMAT_DATE);
        return lDateFormat.format(pDate);
    }

    /**
     *
     * @param pDate date
     * @return date
     */
    public static String getDate(final Date pDate) {
        if (pDate == null) {
            return "";
        }
        SimpleDateFormat lDateFormat = new SimpleDateFormat(FORMAT_DATE);
        return lDateFormat.format(pDate) + "000000";
    }

    /**
     *
     * @param pDate date
     * @return date with time
     */
    public static String getDatumUhrzeit(final Date pDate) {
        if (pDate == null) {
            return "";
        }
        SimpleDateFormat lDateFormat = new SimpleDateFormat(FORMAT_DATETIME);
        return lDateFormat.format(pDate);
    }

    /**
     *
     * @param pDate date
     * @return time
     */
    public static String getUhrzeit(final Date pDate) {
        if (pDate == null) {
            return "";
        }
        SimpleDateFormat lDateFormat = new SimpleDateFormat(FORMAT_TIME);
        return lDateFormat.format(pDate);
    }

    /**
     *
     * @param fnr case number
     * @return SAP case number
     */
    public static String checkSapFallNr(final String fnr) {
        char c;
        String tt = "";
        if (fnr == null) {
            return tt;
        }
        int n = fnr.length();
        for (int i = 0; i < n; i++) {
            c = fnr.charAt(i);
            if (c != '0') {
                tt = fnr.substring(i, n);
                break;
            }
        }
        return tt;
    }

    /**
     *
     * @param date date
     * @param time time
     * @return date
     */
    public static Date combineDate(final Date date, final Date time) {
        int year, month, day, hour, minute, second;
        if (date == null || time == null) {
            return null;
        }
        Calendar inst = Calendar.getInstance();
        inst.setTime(date);
        year = inst.get(Calendar.YEAR);
        if (year == 9999) {
            return null;
        }
        month = inst.get(Calendar.MONTH);
        day = inst.get(Calendar.DAY_OF_MONTH);
        inst.setTime(time);
        hour = inst.get(Calendar.HOUR_OF_DAY);
        minute = inst.get(Calendar.MINUTE);
        second = inst.get(Calendar.SECOND);
        inst.set(year, month, day, hour, minute, second);
        return inst.getTime();
    }

    /**
     *
     * @param date date
     * @param time time
     * @return date
     */
    public static Date checkDate(Date date, Date time) {
        int year, month, day, hour, minute, second;
        if (date == null) {
            return null;
        }
        Calendar inst = Calendar.getInstance();
        inst.setTime(date);
        year = inst.get(Calendar.YEAR);
        month = inst.get(Calendar.MONTH);
        day = inst.get(Calendar.DAY_OF_MONTH);
        inst.setTime(time);
        hour = inst.get(Calendar.HOUR_OF_DAY);
        minute = inst.get(Calendar.MINUTE);
        second = inst.get(Calendar.SECOND);
        inst.set(year, month, day, hour, minute, second);
        return inst.getTime();
    }

    /**
     *
     * @param inS input string
     * @return output string
     */
    public static String checkQuotes(String inS) {
        if (inS.indexOf('\'') >= 0) {
            StringBuilder s = new StringBuilder();
            char c;
            for (int i = 0, n = inS.length(); i < n; i++) {
                c = inS.charAt(i);
                if (c == '\'') {
                    s.append(' ');
                } else {
                    s.append(c);
                }
            }
            return s.toString();
        } else {
            return inS;
        }
    }

    /**
     *
     * @param in input string
     * @return localisation
     */
    public static String getLokalisation(String in) {
        if (in.length() > 0) {
            char c = in.charAt(0);
            switch (c) {
                case 'r':
                    return "1";
                case 'R':
                    return "1";
                case 'l':
                    return "2";
                case 'L':
                    return "2";
                case 'b':
                    return "3";
                case 'B':
                    return "3";
                default:
                    LOG.log(Level.WARNING, "Unknown localisation: {0}", c);
            }
        }
        return "0";
    }

    /*
    Items: ['1', '2', '3', '9', 'M', 'W', 'I', 'U', 'm', 'w', 'i', 'u'];
    Descriptions: ['männlich', 'weiblich', 'unbestimmt', 'unbekannt', 'männlich', 'weiblich', 'unbestimmt', 'unbekannt', 'männlich', 'weiblich', 'unbestimmt', 'unbekannt'];
        
     */
    /**
     *
     * @param pSex sex
     * @return gender
     */
    public static String getSex(final String pSex) {
        if (pSex != null && pSex.length() > 0) {
            char s = pSex.charAt(0);
            switch (s) {
                case '1':
                case 'M':
                case 'm':
                    return "m";
                case '2':
                case 'W':
                case 'w':
                    return "w";
                case '3':
                case 'i':
                case 'I':
                    return "i";
                default:
                    LOG.log(Level.WARNING, "Unknown gender: {0}", s);
            }
        }
        return "u";
    }

    /**
     *
     * @param pInString input string
     * @param pWeightUnit weight unit
     * @return type of weight unit
     */
    public static int transformAdmissionWeight(final String pInString, final String pWeightUnit) {
        String inString = pInString;
        double d = 0;
        if (inString.length() > 0) {
            try {
                d = Double.parseDouble(inString);
            } catch (NumberFormatException e1) {
                LOG.log(Level.FINEST, "cannot parse string as double (1): " + inString, e1);
                try {
                    if (inString.indexOf('.') >= 1) {
                        inString = inString.replaceAll("\\.", "");
                    }
                    if (inString.indexOf(',') >= 1) {
                        inString = inString.replaceAll(",", ".");
                    }
                    d = Double.parseDouble(inString);
                } catch (NumberFormatException e2) {
                    LOG.log(Level.WARNING, "cannot parse string as double (2): " + inString, e2);
                }
            }
            if ("KG".equals(pWeightUnit) || "KILOGRAMM".equals(pWeightUnit)) {
                d *= 1000;
            }
        }
        return (int) d;
    }

    /**
     *
     * @param pRefType ref type
     * @return type of reference
     */
    public static int getDiagRefType(final String pRefType) {
        if (pRefType != null && pRefType.length() > 0) {
            char c = pRefType.charAt(0);
            switch (c) {
                case '+':
                    return RmeDiagnose.REF_TYPE_KREUZ;
                case '*':
                    return RmeDiagnose.REF_TYPE_STERN;
                case '!':
                    return RmeDiagnose.REF_TYPE_ADDITIONAL;
                default:
                    return 0;
            }
        }
        return 0;
    }

    /**
     *
     * @param lFile file
     * @param pMinSizeKb min size
     * @return was compressed?
     */
    public static boolean compressFile(final File lFile, int pMinSizeKb) {
        if (lFile == null || !lFile.exists()) {
            return true;
        }
        if (pMinSizeKb <= 0 || lFile.length() / 1024 > pMinSizeKb) {
            try {
                zipFile(lFile.getAbsolutePath());
            } catch (Throwable lEx) {
                Logger.getLogger(SapStrUtils.class.getName()).log(Level.SEVERE, lEx.getMessage(), lEx);
                return false;
            }
//            final boolean deleted = lFile.delete();
            try {
                Files.delete(lFile.toPath());
                LOG.log(Level.FINEST, "deleted file: {0}", lFile.getAbsolutePath());
            } catch (IOException ex) {
                LOG.log(Level.WARNING, "Was not able to delete file: " + lFile.getAbsolutePath(), ex);
            }
//            if (!deleted) {
//                LOG.log(Level.WARNING, "Was not able to delete file: " + lFile.getAbsolutePath());
//            }
        }
        return true;
    }

    /**
     *
     * @param lFile file
     * @return was compressed?
     */
    public static boolean compressFile(File lFile) {
        return compressFile(lFile, 0);
    }

    /**
     *
     * @param lFile file
     * @return uncompressed file name
     */
    public static String uncompressFile(File lFile) {
        if (lFile == null || !lFile.exists()) {
            return "";
        }
        String newFilename = "";
        try {
            newFilename = unzipFile(lFile.getAbsolutePath(), lFile.getParentFile().getAbsolutePath());
        } catch (IOException ex) {
            Logger.getLogger(SapStrUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return newFilename;
    }

    /**
     * Extracts a zip file specified by the zipFilePath to a directory specified
     * by destDirectory (will be created if does not exists)
     *
     * @param zipFilePath Path of ZIP file
     * @param destDirectory Destination path for decompression
     * @return filename of the first decompressed file
     * @throws IOException Exception
     */
    public static String unzipFile(String zipFilePath, String destDirectory) throws IOException {
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            final boolean created = destDir.mkdir();
            if (!created) {
                LOG.log(Level.WARNING, "Was not able to create destination directory: {0}", destDir.getAbsolutePath());
            }
        }
        String uncompressedFilename;
        try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry = zipIn.getNextEntry();
            // iterates over entries in the zip file
            uncompressedFilename = "";
            while (entry != null) {
                String filePath = destDirectory + File.separator + entry.getName();
                uncompressedFilename = filePath;
                if (!entry.isDirectory()) {
                    // if the entry is a file, extracts it
                    extractFile(zipIn, filePath);
                } else {
                    // if the entry is a directory, make the directory
                    File dir = new File(filePath);
                    final boolean created = dir.mkdir();
                    if (!created) {
                        LOG.log(Level.WARNING, "Was not able to create directory: {0}", dir.getAbsolutePath());
                    }
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
        }
        return uncompressedFilename;
    }

    /**
     * Extracts a zip entry (file entry)
     *
     * @param zipIn
     * @param filePath
     * @throws IOException
     */
    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        final int BUFFER_SIZE = 4096;
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath))) {
            byte[] bytesIn = new byte[BUFFER_SIZE];
            int read;
            while ((read = zipIn.read(bytesIn)) != -1) {
                bos.write(bytesIn, 0, read);
            }
        }
    }

    private static void zipFile(String pSourceFileName) throws Throwable {
        pSourceFileName = toStr(pSourceFileName);
        String pTargetFileName = pSourceFileName + ".zip";
        zipFile(pSourceFileName, pTargetFileName);
    }

    private static void zipFile(String pSourceFileName, String pTargetFileName) throws Throwable {
        pSourceFileName = toStr(pSourceFileName);
        pTargetFileName = toStr(pTargetFileName);

        File lSourceFile = new File(pSourceFileName);

        if (pSourceFileName.isEmpty()) {
            return;
        }

        byte[] lBuffer = new byte[1024];

        FileOutputStream lFos = new FileOutputStream(pTargetFileName);
        try (ZipOutputStream lZos = new ZipOutputStream(lFos)) {
            ZipEntry lZe = new ZipEntry(lSourceFile.getName());
            lZos.putNextEntry(lZe);
            try (FileInputStream lIn = new FileInputStream(pSourceFileName)) {
                int lLength;
                while ((lLength = lIn.read(lBuffer)) > 0) {
                    lZos.write(lBuffer, 0, lLength);
                }
            }
            lZos.closeEntry();
        }
    }

    /**
     *
     * @param pPlaintext plain text
     * @return hash
     */
    public static String getHash(final String pPlaintext) {
        return getHash(pPlaintext, 0);
    }

    /**
     *
     * @param pPlaintext plain text
     * @param pTruncSize truncation size
     * @return hash
     */
    public static String getHash(final String pPlaintext, final int pTruncSize) {
        final String encoding = "Cp1252";
        if (pPlaintext == null) {
            return null;
        }
        if (pPlaintext.trim().isEmpty()) {
            return pPlaintext;
        }
        String hashtext = "";
        MessageDigest m;
        try {
            m = MessageDigest.getInstance("SHA-256");
            m.reset();
            m.update(pPlaintext.getBytes(encoding));
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            hashtext = bigInt.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

        if (pTruncSize > 0 && hashtext.length() > pTruncSize) {
            hashtext = hashtext.substring(0, pTruncSize - 1).trim();
        }

        return hashtext;
    }

}
