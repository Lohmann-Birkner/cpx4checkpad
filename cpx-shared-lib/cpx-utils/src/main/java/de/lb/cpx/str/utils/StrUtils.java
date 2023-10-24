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
package de.lb.cpx.str.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dirk Niemeier
 */
public class StrUtils {

    private static final Logger LOG = Logger.getLogger(StrUtils.class.getName());

    protected StrUtils() {
        //
    }

    public static String toStr(final int pValue) {
        return toStr(String.valueOf(pValue));
    }

    public static String toStr(final float pValue) {
        return toStr(String.valueOf(pValue));
    }

    public static String toStr(final Long pValue) {
        return toStr(longToString(pValue));
    }

    /* 3.9.3 2015-04-29 DNi: Wandelt null in Leerstring und trimmt den String. */
    public static String toStr(final String pValue) {
        return (pValue == null) ? "" : pValue.trim();
    }

    public static String toStr(final Long pValue, final String pDefault) {
        String lValue = toStr(pValue);
        return toStr(lValue, pDefault);
    }

    public static String toStr(final String pValue, final String pDefault) {
        String lValue = toStr(pValue);
        if (lValue.isEmpty() && pDefault != null) {
            lValue = pDefault;
        }
        return lValue;
    }

    public static String longToString(final Long pValue) {
        String lValue = "";
        if (pValue == null) {
            return lValue;
        }
        lValue = pValue.toString();
        return lValue;
    }

    public static String floatToMoney(final BigDecimal pValue) {
        if (pValue == null) {
            return null;
        }
        float lValue = pValue.floatValue();
        return StrUtils.floatToMoney(lValue);
    }

    public static String floatToStr(final float pValue) {
        String lValue = String.valueOf(pValue);
        lValue = toStr(lValue);
        lValue = lValue.replace(",", ".");
        return lValue;
    }

    public static String floatToStr(final double pValue) {
        String lValue = String.valueOf(pValue);
        lValue = toStr(lValue);
        lValue = lValue.replace(",", ".");
        return lValue;
    }

    public static String floatToStr(BigDecimal pValue) {
        String lValue = String.valueOf(pValue);
        lValue = toStr(lValue);
        lValue = lValue.replace(",", ".");
        return lValue;
    }

    public static String floatToMoney(final float pValue) {
        String lValue = StrUtils.floatToStr(pValue);
        return floatToMoney(lValue);
    }

    public static String floatToMoney(final double pValue) {
        return StrUtils.floatToMoney((float) pValue);
    }

    public static String floatToMoney(String pValue) {
        pValue = toStr(pValue);
        if (pValue.isEmpty()) {
            pValue = "0";
        }
        pValue = pValue.replace(",", ".");
        int lPos = pValue.indexOf('.');
        if (lPos > -1) {
            String sTmp = pValue.substring(lPos + 1);
            String lCent = rightPad(sTmp.substring(0, sTmp.length() <= 2 ? sTmp.length() : 2), 2, '0');
            pValue = pValue.substring(0, lPos + 1) + lCent;
        } else {
            pValue += ".00";
        }
        return pValue;
    }

    public static java.sql.Date parseDate(final String pDate, final String pFormat) throws ParseException {
        final String date = pDate == null ? "" : pDate.trim();
        if (date.isEmpty()) {
            return null;
        }
        if (pFormat == null || pFormat.trim().isEmpty()) {
            throw new IllegalArgumentException("Date format is null!");
        }
        DateFormat df = new SimpleDateFormat(pFormat);
        return new java.sql.Date(df.parse(date).getTime());
    }

    public static java.sql.Timestamp parseTimestamp(final String pDate, final String pFormat) throws ParseException {
        final String date = pDate == null ? "" : pDate.trim();
        if (date.isEmpty()) {
            return null;
        }
        if (pFormat == null || pFormat.trim().isEmpty()) {
            throw new IllegalArgumentException("Date format is null!");
        }
        DateFormat df = new SimpleDateFormat(pFormat);
        return new java.sql.Timestamp(df.parse(date).getTime());
    }

    public static boolean toBool(final BigDecimal pValue) {
        if (pValue == null) {
            return false;
        }
        return toBool(pValue.toString());
    }

    public static boolean toBool(final String pValue) {
        String value = (pValue == null) ? "" : pValue.trim().toLowerCase();
        if (value.isEmpty()) {
            return false;
        }
        if ("1".equalsIgnoreCase(pValue)) {
            return true;
        }
        if ("0".equalsIgnoreCase(pValue)) {
            return false;
        }
        if ("t".equalsIgnoreCase(pValue)) {
            return true;
        }
        if ("f".equalsIgnoreCase(pValue)) {
            return false;
        }
        if ("true".equalsIgnoreCase(pValue)) {
            return true;
        }
        if ("false".equalsIgnoreCase(pValue)) {
            return false;
        }
        if ("on".equalsIgnoreCase(pValue)) {
            return true;
        }
        if ("off".equalsIgnoreCase(pValue)) {
            return false;
        }
        if ("enabled".equalsIgnoreCase(pValue)) {
            return true;
        }
        if ("disabled".equalsIgnoreCase(pValue)) {
            return false;
        }
        if ("enable".equalsIgnoreCase(pValue)) {
            return true;
        }
        if ("disable".equalsIgnoreCase(pValue)) {
            return false;
        }
        if ("wahr".equalsIgnoreCase(pValue)) {
            return true;
        }
        if ("falsch".equalsIgnoreCase(pValue)) {
            return false;
        }
        if ("ja".equalsIgnoreCase(pValue)) {
            return true;
        }
        if ("nein".equalsIgnoreCase(pValue)) {
            return false;
        }
        if ("j".equalsIgnoreCase(pValue)) {
            return true;
        }
        if ("n".equalsIgnoreCase(pValue)) {
            return false;
        }
        throw new IllegalArgumentException("Was not able to detect boolean value of '" + value + "'");
    }

    public static Integer toInt(final String pValue, final Integer pDefaultValue) {
        Integer val = toInt(pValue);
        if (val == null) {
            return pDefaultValue;
        }
        return val;
    }

    public static String toStrAsNumber(final boolean pValue){
        return pValue?"1":"0";
    }
    
    public static Integer toInt(final String pValue) {
        String value = (pValue == null) ? "" : pValue.trim().toLowerCase();
        if (value.isEmpty()) {
            return null;
        }
        try {
            int val = Integer.parseInt(value);
            return val;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Was not able to detect integer value of '" + value + "'", ex);
        }
    }

    public static Long toLong(final String pValue) {
        String value = (pValue == null) ? "" : pValue.trim().toLowerCase();
        if (value.isEmpty()) {
            return null;
        }
        try {
            long val = Long.parseLong(value);
            return val;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Was not able to detect integer value of '" + value + "'", ex);
        }
    }

    /* 3.9.3 2015-04-29 DNi: Auffüllen auf der beiden Seiten mit Leerzeichen. */
    public static String centerPad(final String pValue, final int pLength) {
        return centerPad(pValue, pLength, ' ');
    }

    /* 3.9.3 2015-04-29 DNi: Auffüllen auf der beiden Seiten. */
    public static String centerPad(final String pValue, final int pLength, final char pChar) {
        return centerPad(pValue, pLength, pChar, false);
    }

    /* 3.9.3 2015-04-29 DNi: Auffüllen auf der beiden Seiten mit Leerzeichen. */
    public static String centerPad(final boolean pValue, final int pLength) {
        return centerPad(String.valueOf(pValue), pLength, ' ');
    }

    /* 3.9.3 2015-04-29 DNi: Auffüllen auf der beiden Seiten. */
    public static String centerPad(String pValue, final int pLength, final char pChar, final boolean pNoTrim) {
        if (pNoTrim) {
            if (pValue == null) {
                pValue = "";
            }
        } else {
            pValue = toStr(pValue);
        }
        if (pLength <= 0 || pValue.length() >= pLength) {
            return pValue;
        }

        int iLeftPadLength = (pLength - pValue.length()) / 2;
        int iRightPadLength = pLength - pValue.length() - iLeftPadLength;

        StringBuilder lBuilder = new StringBuilder();
        while (lBuilder.length() < iLeftPadLength) {
            lBuilder.append(pChar);
        }
        lBuilder.append(pValue);
        for (int i = 0; i < iRightPadLength; i++) {
            lBuilder.append(pChar);
        }

        return lBuilder.toString();
    }

    public static String leftPad(final boolean pValue, final int pLength) {
        return leftPad(pValue, pLength, ' ');
    }

    /* 3.9.3 2015-04-29 DNi: Auffüllen auf der linken Seite mit Leerzeichen. */
    public static String leftPad(final String pValue, final int pLength) {
        return leftPad(pValue, pLength, ' ');
    }

    public static String leftPad(final int pValue, final int pLength) {
        return leftPad(pValue, pLength, ' ');
    }

    public static String leftPad(final float pValue, final int pLength) {
        return leftPad(pValue, pLength, ' ');
    }

    public static String leftPad(final BigDecimal pValue, final int pLength) {
        return leftPad(pValue, pLength, ' ');
    }

    public static String leftPad(final boolean pValue, final int pLength, final char pChar) {
        return leftPad(String.valueOf(pValue), pLength, pChar);
    }

    public static String leftPad(final int pValue, final int pLength, final char pChar) {
        return leftPad(String.valueOf(pValue), pLength, pChar);
    }

    public static String leftPad(final float pValue, final int pLength, final char pChar) {
        return leftPad(StrUtils.floatToStr(pValue), pLength, pChar);
    }

    public static String leftPad(final BigDecimal pValue, final int pLength, final char pChar) {
        return leftPad(String.valueOf(pValue), pLength, pChar);
    }

    /* 3.9.3 2015-04-29 DNi: Auffüllen auf der linken Seite. */
    public static String leftPad(final String pValue, final int pLength, final char pChar) {
        return leftPad(pValue, pLength, pChar, false);
    }

    /* 3.9.3 2015-04-29 DNi: Auffüllen auf der linken Seite. */
    public static String leftPad(String pValue, final int pLength, final char pChar, final boolean pNoTrim) {
        pValue = toStr(pValue);
        StringBuilder lBuilder = new StringBuilder(pValue);
        StringBuilder lBuilder2 = new StringBuilder(rightPad(lBuilder.reverse().toString(), pLength, pChar, pNoTrim));
        return lBuilder2.reverse().toString();
    }

    public static String rightPad(final boolean pValue, final int pLength) {
        return rightPad(pValue, pLength, ' ');
    }

    public static String rightPad(final int pValue, final int pLength) {
        return rightPad(pValue, pLength, ' ');
    }

    public static String rightPad(final float pValue, final int pLength) {
        return rightPad(pValue, pLength, ' ');
    }

    public static String rightPad(final BigDecimal pValue, final int pLength) {
        return rightPad(pValue, pLength, ' ');
    }

    /* 3.9.3 2015-04-29 DNi: Auffüllen auf der rechten Seite mit Leerzeichen. */
    public static String rightPad(final String pValue, final int pLength) {
        return rightPad(pValue, pLength, ' ');
    }

    public static String rightPad(final boolean pValue, final int pLength, final char pChar) {
        return rightPad(String.valueOf(pValue), pLength, pChar);
    }

    public static String rightPad(final int pValue, final int pLength, final char pChar) {
        return rightPad(String.valueOf(pValue), pLength, pChar);
    }

    public static String rightPad(final float pValue, final int pLength, final char pChar) {
        return rightPad(StrUtils.floatToStr(pValue), pLength, pChar);
    }

    public static String rightPad(final BigDecimal pValue, final int pLength, final char pChar) {
        return rightPad(String.valueOf(pValue), pLength, pChar);
    }

    /* 3.9.3 2015-04-29 DNi: Auffüllen auf der rechten Seite. */
    public static String rightPad(final String pValue, final int pLength, final char pChar) {
        return rightPad(pValue, pLength, pChar, false);
    }

    /* 3.9.3 2015-04-29 DNi: Auffüllen auf der rechten Seite. */
    public static String rightPad(String pValue, final int pLength, final char pChar, final boolean pNoTrim) {
        if (pNoTrim) {
            if (pValue == null) {
                pValue = "";
            }
        } else {
            pValue = toStr(pValue);
        }
        if (pLength <= 0) {
            return pValue;
        }
        StringBuilder lBuilder = new StringBuilder(pValue);
        while (lBuilder.length() < pLength) {
            lBuilder.append(pChar);
        }
        return lBuilder.toString();
    }

    public static String removeTrailingZeros(final String pValue) {
        return removeTrailingChars(pValue, '0');
    }

    public static String removeTrailingChars(final String pValue, final char pChar) {
        if (pValue == null || pValue.isEmpty()) {
            return pValue;
        }
        String tmp = pValue;
        while (tmp.length() > 0 && tmp.charAt(0) == pChar) {
            tmp = tmp.substring(1);
        }
        return tmp;
    }

    public static String getConstantKey(final String pKey /*, final boolean pWithPrefix*/) {
        /*
    if (!isValidKey(pKey)) {
      return "";
    }
         */
        String key = (pKey == null) ? "" : pKey.trim();

        String tmp = "";
        if (pKey != null && key.equals(pKey.toUpperCase())) {
            tmp = pKey;
        } else {
            key = key.replace(".", "_");
            key = key.replace("-", "_");
            tmp = "";
            String s_old = "";
            Character c_old = ' ';
            for (int i = 0; i < key.length(); i++) {
                Character c = key.charAt(i);
                String s = String.valueOf(c);
                if (c >= '0' && c <= '9' && c_old >= '0' && c_old <= '9' || c == '_') {
                    //
                } else if (!s_old.isEmpty()
                        && !s_old.equals("_")
                        && s_old.toLowerCase().equals(s_old)
                        && s.toUpperCase().equals(s)) {
                    tmp += "_";
                }
                tmp += s.toUpperCase();
                if (s_old.equals(".") || s_old.equals("_")) {
                    s_old = s.toUpperCase();
                    c_old = Character.toUpperCase(c);
                } else {
                    s_old = s;
                    c_old = c;
                }
            }
        }

        /*
    if (!tmp.isEmpty()) {
      if (pWithPrefix) {
        tmp = getPrefix() + tmp;
      }
    }
         */
        return tmp;
    }

    public static String cutStr(final String pValue, final int pLength, String pEllipse) {
        if (pEllipse == null) {
            pEllipse = "";
        }
        String lValue = cutStr(pValue, pLength - pEllipse.length());
        if (lValue.length() == pLength - pEllipse.length()) {
            lValue += pEllipse;
        }
        lValue = cutStr(lValue, pLength);
        return lValue;
    }

    public static String cutStr(String pValue, final int pLength) {
        pValue = toStr(pValue);
        if (pLength <= 0 || pValue.isEmpty() || pValue.length() < pLength) {
            return pValue;
        }
        pValue = toStr(pValue.substring(0, pLength));
        return pValue;
    }

    public static BigDecimal getKaufmaennischGerundet(BigDecimal pValue, int pNachkommastellen) {
        if (pValue == null) {
            return null;
        }
        //pValue = pValue.add(new BigDecimal(0.00000000001d));
        if (pNachkommastellen < 0) {
            pNachkommastellen = 0;
        }
        pValue = pValue.setScale(pNachkommastellen, RoundingMode.HALF_UP);
        return pValue;
    }

    public static Float getKaufmaennischGerundet(final Double pValue, final int pNachkommastellen) {
        if (pValue == null) {
            return null;
        }
        BigDecimal lValue = BigDecimal.valueOf(pValue);
        BigDecimal lValueNew = getKaufmaennischGerundet(lValue, pNachkommastellen);
        return lValueNew == null ? null : lValueNew.floatValue();
    }

    public static Float getKaufmaennischGerundet(final Float pValue, final int pNachkommastellen) {
        if (pValue == null) {
            return null;
        }
        BigDecimal lValue = BigDecimal.valueOf(pValue);
        BigDecimal lValueNew = getKaufmaennischGerundet(lValue, pNachkommastellen);
        return lValueNew == null ? null : lValueNew.floatValue();
    }

    public static void validateDbName(final String pName) {
        String name = (pName == null) ? "" : pName.trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("No name given");
        }
        int maxLength = 30;
        if (name.length() > maxLength) {
            throw new IllegalArgumentException("Name '" + name + "' is longer than " + maxLength + " characters");
        }
        if (name.equalsIgnoreCase("CASE")
                || name.equalsIgnoreCase("ALTER")
                || name.equalsIgnoreCase("ORDER")
                || name.equalsIgnoreCase("GROUP")
                || name.equalsIgnoreCase("DELETE")
                || name.equalsIgnoreCase("DROP")
                || name.equalsIgnoreCase("CREATE")) {
            throw new IllegalArgumentException(name + " is a preserved word");
        }
        char firstChar = name.charAt(0);
        if (firstChar >= '0' && firstChar <= '9') {
            throw new IllegalArgumentException("First must not be a number");
        }
        for (int i = 0; i < name.length(); i++) {
            char ch = name.charAt(i);
            if (ch >= '0' && ch <= '9'
                    || ch >= 'a' && ch <= 'z'
                    || ch >= 'A' && ch <= 'Z'
                    || ch == '_') {
                //Nice!
            } else {
                //Bad!
                throw new IllegalArgumentException("Illegal character found in name: " + ch);
            }
        }
    }

    public static String getConnectionUrl(final Connection pConnection) {
        Connection connection = pConnection;
        if (connection == null) {
            return "";
        }
        String connectionString = "";
        try {
            connectionString = connection.getMetaData().getURL();
        } catch (SQLException ex) {
            Logger.getLogger(StrUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return StrUtils.toStr(connectionString);
    }

    public static String getConnectionDatabase(final Connection pConnection) {
        Connection connection = pConnection;
        if (connection == null) {
            return "";
        }
        String connectionDatabase = "";
        try {
            String connectionString = getConnectionUrl(pConnection);
            if (isOracle(connectionString)) {
                //Oracle
                connectionDatabase = connection.getMetaData().getUserName();
            } else {
                //SqlSrv
                connectionString = connectionString == null ? "" : connectionString.trim();
                String token = "databaseName=";
                String tmp = toStr(connectionString.substring(connectionString.indexOf(token) + token.length()));
                if (tmp.contains(";")) {
                    tmp = tmp.substring(0, tmp.indexOf(';'));
                }
                connectionDatabase = toStr(tmp);
            }
        } catch (SQLException ex) {
            Logger.getLogger(StrUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return StrUtils.toStr(connectionDatabase);
    }

    public static boolean isOracle(final String pConnectionUrl) {
        String connUrl = (pConnectionUrl == null) ? "" : pConnectionUrl.toLowerCase().trim();
        return connUrl.contains(":oracle:");
    }

    public static boolean isSqlSrv(final String pConnectionUrl) {
        String connUrl = (pConnectionUrl == null) ? "" : pConnectionUrl.toLowerCase().trim();
        return connUrl.contains(":sqlserver:") || connUrl.contains(":sqlsrv:");
    }

    public static String DoubleToStr(double pValue) {
        String lValueStr = String.format("%.2f%n", pValue);
        if (lValueStr != null) {
            lValueStr = lValueStr.replace("\r\n", "");
            lValueStr = lValueStr.replace(".", ",");
        }
        return lValueStr;
    }

    public static long getMaxMemory() {
        return Runtime.getRuntime().maxMemory();
    }

    public static long getTotalMemory() {
        return Runtime.getRuntime().totalMemory();
    }

    public static long getFreeMemory() {
        return Runtime.getRuntime().freeMemory();
    }

    public static long getUsedMemory() {
        return getTotalMemory() - getFreeMemory();
    }

    public static double getHeapInUse() {
        return (getUsedMemory() / (double) getMaxMemory());
    }

    public static String getMaxMemoryAsString() {
        double lMemory = (double) StrUtils.getMaxMemory() / (1024 * 1024L);
        String lMemoryStr = StrUtils.DoubleToStr(lMemory);
        return lMemoryStr;
    }

    public static String getTotalMemoryAsString() {
        double lMemory = (double) StrUtils.getTotalMemory() / (1024 * 1024L);
        String lMemoryStr = StrUtils.DoubleToStr(lMemory);
        return lMemoryStr;
    }

    public static String getFreeMemoryAsString() {
        double lMemory = (double) StrUtils.getFreeMemory() / (1024 * 1024L);
        String lMemoryStr = StrUtils.DoubleToStr(lMemory);
        return lMemoryStr;
    }

    public static String getUsedMemoryAsString() {
        double lMemory = (double) StrUtils.getUsedMemory() / (1024 * 1024L);
        String lMemoryStr = StrUtils.DoubleToStr(lMemory);
        return lMemoryStr;
    }

    public static String getHeapInUseAsString() {
        double lHeapInUse = StrUtils.getHeapInUse();
        lHeapInUse = (lHeapInUse * 10000 / 100);
        return StrUtils.DoubleToStr(lHeapInUse);
    }

//    public static Map<String, String> split(final Map<String, String> pMap, final char pSplitChar) {
//        final HashMap<String, String> tmp = pMap == null ? null : new HashMap<>(pMap);
//        HashMap<String, String> result = new HashMap<>();
//        if (tmp == null || tmp.isEmpty()) {
//            return result;
//        }
//        for (Map.Entry<String, String> entry : tmp.entrySet()) {
//            final String value = entry.getValue();
//            String[] kisStati = sfName.split(",");
//            for (int i = 0, n = kisStati.length; i < n; i++) {
//                String[] statiMap = kisStati[i].trim().split("_");
//                if (statiMap.length == 2) {
//                    this.m_nexusKisStatusDefHash.put(statiMap[0].trim(), statiMap[1].trim());
//                }
//            }
//        }
//        return result;
//    }
    public static List<String> split(final String pLine, final char pSplitChar) {
        List<String> fields = new ArrayList<>();
        String value = "";
        for (int i = 0, n = pLine.length(); i < n; i++) {
            if (pLine.charAt(i) != pSplitChar) {
                value += pLine.charAt(i);
            } else {
                fields.add(value.trim());
                value = "";
            }
        }
        fields.add(value.trim()); //letztes Element einfügen
        return fields;
    }

    public static Integer getYear(final Date pDate) {
        if (pDate == null) {
            return null;
        }
        final Calendar tempCalendar = new GregorianCalendar(TimeZone.getTimeZone("ECT"));
        tempCalendar.setTime(pDate);
        int year = tempCalendar.get(Calendar.YEAR);
        //int month = tempCalendar.get(Calendar.MONTH) + 1;
        return year;
    }

    public static Integer getMonth(final Date pDate) {
        if (pDate == null) {
            return null;
        }
        final Calendar tempCalendar = new GregorianCalendar(TimeZone.getTimeZone("ECT"));
        tempCalendar.setTime(pDate);
        //int year = tempCalendar.get(Calendar.YEAR);
        int month = tempCalendar.get(Calendar.MONTH) + 1;
        return month;
        //return year + ";" + month;
    }

    public static String toStaticDate(final Date pDate, final boolean pIsSqlSrv) {
        String date = dateToString(pDate);
        return toStaticDate(date, pIsSqlSrv);
    }

    public static String toStaticDate(final String pDate, final boolean pIsSqlSrv) {
        if (pDate == null || pDate.trim().isEmpty()) {
            return "";
        }
        if (pIsSqlSrv) {
            return "'" + escapeSQL(pDate.trim().replace("-", "")) + "'";
        }
        return "TO_DATE('" + escapeSQL(pDate) + "', 'YYYY-MM-DD')";
    }

    public static String dateToString(final Date pDate) {
        if (pDate == null) {
            return "";
        }
        Calendar cal = new GregorianCalendar();
        cal.setTime(pDate);
        String year = String.valueOf(cal.get(Calendar.YEAR));
        String month = String.valueOf((cal.get(Calendar.MONTH) + 1));
        String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));

        if (month.length() == 1) {
            month = "0" + month;
        }
        if (day.length() == 1) {
            day = "0" + day;
        }

        String dateStr = year + "-" + month + "-" + day;
        return dateStr;
    }

    public static String escapeSQL(String pString) {
        int length = pString.length();
        int newLength = length;
        // first check for characters that might
        // be dangerous and calculate a length
        // of the string that has escapes.
        for (int i = 0; i < length; i++) {
            char c = pString.charAt(i);
            switch (c) {
                case '_':
                case '\\':
                case '\"':
                case '\'':
                case '\0': {
                    newLength += 1;
                }
                break;
                default:
                //stay newLength unchanged
            }
        }
        if (length == newLength) {
            // nothing to escape in the string
            return pString;
        }
        StringBuilder sb = new StringBuilder(newLength);
        for (int i = 0; i < length; i++) {
            char c = pString.charAt(i);
            switch (c) {
                case '\\': {
                    sb.append("\\\\");
                }
                break;
                case '\"': {
                    sb.append("\""); //before -> \\\"
                }
                break;
                case '\'': {
                    sb.append("\\\'");
                }
                break;
                case '\0': {
                    sb.append("\\0");
                }
                break;
                case '_': {
                    sb.append("\\_");
                }
                break;
                default: {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    /**
     * read argument value from string array
     *
     * @param args array of arguments
     * @param pIndex array index
     * @return array value at given index
     */
    public static String getArg(final String[] args, final int pIndex) {
        if (args == null || args.length == 0) {
            LOG.log(Level.FINEST, "array is null or empty!");
            return "";
        }
        if (pIndex >= args.length) {
            LOG.log(Level.FINEST, "illegal array index: " + pIndex + " (array length is only " + args.length + ")");
            return "";
        }
        if (pIndex < 0) {
            LOG.log(Level.INFO, "illegal array index: " + pIndex);
            return "";
        }
        final String val = args[pIndex];
        return toStr(val);
    }

    /**
     * returns null if database column is null
     *
     * @param <T> expected type
     * @param pResultSet result set
     * @param pColumnName column name
     * @return null if column is null
     * @throws SQLException error
     */
    @SuppressWarnings("unchecked")
    public static <T> T getNull(ResultSet pResultSet, String pColumnName) throws SQLException {
        if (pResultSet == null) {
            return null;
        }
        if (pColumnName == null) {
            return null;
        }
        Object value = pResultSet.getObject(pColumnName);
        return pResultSet.wasNull() ? null : (T) value;
    }

    /**
     * returns null if database column is null
     *
     * @param <T> expected type
     * @param pResultSet result set
     * @param pColumnIndex column index
     * @return null if column is null
     * @throws SQLException error
     */
    @SuppressWarnings("unchecked")
    public static <T> T getNull(ResultSet pResultSet, int pColumnIndex) throws SQLException {
        if (pResultSet == null) {
            return null;
        }
        if (pColumnIndex <= 0) {
            return null;
        }
        Object value = pResultSet.getObject(pColumnIndex);
        return pResultSet.wasNull() ? null : (T) value;
    }

//    /**
//     * returns null if database column is null
//     * @param pResultSet result set
//     * @param pColumnIndex column index
//     * @return null if column is null
//     * @throws SQLException error
//     */
//    public static Integer getInt(ResultSet pResultSet, int pColumnIndex) throws SQLException {
//        if (pResultSet == null) {
//            return null;
//        }
//        int value = pResultSet.getInt(pColumnIndex);
//        return pResultSet.wasNull() ? null : value;
//    }
//
//    /**
//     * returns null if database column is null
//     * @param pResultSet result set
//     * @param pColumnIndex column index
//     * @return null if column is null
//     * @throws SQLException error
//     */
//    public static Double getDouble(ResultSet pResultSet, int pColumnIndex) throws SQLException {
//        if (pResultSet == null) {
//            return null;
//        }
//        double value = pResultSet.getDouble(pColumnIndex);
//        return pResultSet.wasNull() ? null : value;
//    }
//
//    /**
//     * returns null if database column is null
//     * @param pResultSet result set
//     * @param pColumnIndex column index
//     * @return null if column is null
//     * @throws SQLException error
//     */
//    public static Long getLong(ResultSet pResultSet, int pColumnIndex) throws SQLException {
//        if (pResultSet == null) {
//            return null;
//        }
//        long value = pResultSet.getLong(pColumnIndex);
//        return pResultSet.wasNull() ? null : value;
//    }
//
//    /**
//     * returns null if database column is null
//     * @param pResultSet result set
//     * @param pColumnIndex column index
//     * @return null if column is null
//     * @throws SQLException error
//     */
//    public static Float getFloat(ResultSet pResultSet, int pColumnIndex) throws SQLException {
//        if (pResultSet == null) {
//            return null;
//        }
//        float value = pResultSet.getFloat(pColumnIndex);
//        return pResultSet.wasNull() ? null : value;
//    }
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
            LOG.log(Level.SEVERE, "unable to hash value", ex);
        }

//        if (pTruncSize > 0 && hashtext.length() > pTruncSize) {
//            hashtext = hashtext.substring(0, pTruncSize - 1).trim();
//        }
        hashtext = trunc(hashtext, pTruncSize);

        return hashtext;
    }

    public static String trunc(final String pValue, final int pTruncSize) {
        if (pValue == null) {
            return pValue;
        }
        String value = pValue;
        if (pTruncSize > 0 && value.length() > pTruncSize) {
            value = value.substring(0, pTruncSize - 1).trim();
        }
        return value;
    }

}
