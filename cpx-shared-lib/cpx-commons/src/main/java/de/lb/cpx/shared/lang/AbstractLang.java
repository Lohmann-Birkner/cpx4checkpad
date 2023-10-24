/* 
 * Copyright (c) 2016 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner.
 * http://www.lohmann-birkner.de/de/index.php
 *
 * Contributors:
 *    2016  Niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.shared.lang;

import de.lb.cpx.model.lang.CpxLanguageInterface;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.math3.util.Precision;

/**
 *
 * @author Dirk Niemeier
 */
public class AbstractLang {

    protected static CpxLanguageInterface cpxLanguage = null;
    protected static final String PLACEHOLDER = "*EMPTY*";
    //tm = translation map
    protected static final Map<String, Map<String, String>> M = new HashMap<>();
    private static final Logger LOG = Logger.getLogger(AbstractLang.class.getName());
    private static String locale = null;

    protected AbstractLang() {
        //utility class needs no public constructor
    }

    public static void setCpxLanguage(CpxLanguageInterface pCpxLanguage) {
        cpxLanguage = pCpxLanguage;
    }

    public static CpxLanguageInterface getCpxLanguage() {
        return cpxLanguage;
    }

    public static Translation get(final String pKey) {
        return get(pKey, (Object[]) null);
    }

    public static Translation get(final String pKey, final Object... pParams) {
        String value = "";
        if (cpxLanguage != null) {
            value = cpxLanguage.get(pKey, false, pParams);
        }
        if (value == null || value.isEmpty()) {
            value = getStaticFallback(pKey, pParams);
        }
        if (value == null || value.isEmpty()) {
            value = CpxLanguageInterface.toPlaceholder(pKey);
        }
        return new Translation(value);
        //return pKey;
    }

    public static String getStaticFallback(final String pKey) {
        return getStaticFallback(pKey, (Object[]) null);
    }

    public static String getStaticLocale() {
        if (locale == null) {
            locale = CpxLanguageInterface.getSystemLocaleShort();
        }
        return locale;
    }

    public static void setStaticLocale(final String pLocale) {
        String loc = pLocale == null ? null : pLocale.trim().toLowerCase();
        if (loc != null && !loc.equals("de") && !loc.equals("en")) {
            LOG.log(Level.WARNING, "locale is maybe set to invalid value: {0}", loc);
        }
        locale = loc;
    }

    public static String getStaticFallback(final String pKey, final Object... pParams) {
        //CPX Language was not set (can happen on server side!) or key does not exist in locale property file (de.properties, en.properties...)
        //Try to lookup in our static translation map that is embedded in Lang.java with system locale
        String value = "";
        if (M.isEmpty()) {
            //should not happen, but sometimes the map is losing it's entries (maybe garbage collection or because of de-/serialization?)
            Lang.initLocales();
        }
        //String locale = CpxLanguageInterface.getSystemLocaleShort();
        String locale = getStaticLocale();
        value = lookupStaticTranslation(locale, pKey, pParams);
        if ((value == null || value.isEmpty()) && !"en".equalsIgnoreCase(locale)) {
            //Key was not found in embedded static translation map, try to fallback to english version
            value = lookupStaticTranslation("en", pKey, pParams);
        }
        return value;
    }

    public static String lookupStaticTranslation(final String pKey, final Object... pParams) {
        //String locale = CpxLanguageInterface.getSystemLocaleShort();
        String locale = getStaticLocale();
        return lookupStaticTranslation(locale, pKey, pParams);
    }

    public static String lookupStaticTranslation(final String pLocale, final String pKey, final Object... pParams) {
        //Class<Lang> clazz = Lang.class; //Dummy to initialize static translationMap...
        //String key = CpxLanguageInterface.getKey(pKey);
        String key = pKey;
        String locale = (pLocale == null) ? "" : pLocale.trim().toLowerCase();
        if (locale.isEmpty()) {
            locale = "en";
        }
        Map<String, String> subMap = M.get(locale);
        if (subMap == null || subMap.isEmpty()) {
            return pKey;
        }
        String value = subMap.get(key);
        if (value == null || value.isEmpty()) {
            return pKey;
        }
        value = CpxLanguageInterface.replaceParams(value, pParams);
        //return getValue(value);
        return value;
    }

    public static LocalDate toLocalDate(final String pDate) {
        final Date date = toDate(pDate);
        return date == null ? null : toLocalDate(date);
    }

    public static LocalDateTime toLocalDateTime(final String pDateTime) {
        final Date dateTime = toDate(pDateTime);
        return dateTime == null ? null : toLocalDateTime(dateTime);
    }

    public static LocalTime toLocalTime(final String pTime) {
        final Date time = toDate(pTime);
        return time == null ? null : toLocalTime(time);
    }

    public static Date toDate(final String pDate) {
        final String date = pDate == null ? "" : pDate.trim();
        final String format = Lang.getDateFormat();
        try {
            return CpxLanguageInterface.toDateTime(date, format);
        } catch (ParseException ex) {
            LOG.log(Level.FINEST, "This does not fulfil the required date format '" + format + "': " + date, ex);
            return null;
        }
    }

    public static Date toDateTime(final String pDateTime) {
        final String dateTime = pDateTime == null ? "" : pDateTime.trim();
        final String format = Lang.getDateTimeFormat();
        try {
            return CpxLanguageInterface.toDateTime(dateTime, format);
        } catch (ParseException ex) {
            LOG.log(Level.FINEST, "This does not fulfil the required date/time format '" + format + "': " + dateTime, ex);
            return null;
        }
    }

    public static Date toTime(final String pTime) {
        final String time = pTime == null ? "" : pTime.trim();
        final String format = Lang.getTimeFormat();
        try {
            return CpxLanguageInterface.toDateTime(time, format);
        } catch (ParseException ex) {
            LOG.log(Level.FINEST, "This does not fulfil the required time format '" + format + "': " + time, ex);
            return null;
        }
    }

    public static String toDate(final Date pDate) {
        String value = "";
        if (cpxLanguage != null) {
            value = cpxLanguage.toDate(pDate);
        }
        if (value == null || value.isEmpty()) {
            value = CpxLanguageInterface.toDateTime(pDate, Lang.getDateFormat());
        }
        if (value == null || value.isEmpty()) {
            //Fallback, international format
            value = toIsoDate(pDate);
        }
        return value;
        //return PLACEHOLDER;
    }

    public static String toDate(final LocalDate pLocalDate) {
        return toDate(CpxLanguageInterface.localToDate(pLocalDate));
    }

    public static String toDateTime(final Date pDate) {
        String value = "";
        if (cpxLanguage != null) {
            value = cpxLanguage.toDateTime(pDate);
        }
        if (value == null || value.isEmpty()) {
            value = CpxLanguageInterface.toDateTime(pDate, Lang.getDateTimeFormat());
        }
        if (value == null || value.isEmpty()) {
            //Fallback, international format
            value = toIsoDateTime(pDate);
        }
        return value;
        //return PLACEHOLDER;
    }

    public static String toDateTime(final LocalDateTime pLocalDateTime) {
        return toDateTime(CpxLanguageInterface.localToDate(pLocalDateTime));
    }

    public static String toDecimal(final Integer pNumber) {
        return toDecimal(pNumber == null ? null : pNumber.longValue());
    }

    public static String toDecimal(final BigDecimal pDecimal) {
        String value = "";
        if (cpxLanguage != null) {
            value = cpxLanguage.toDecimal(pDecimal);
        }
        if (value == null || value.isEmpty()) {
            value = CpxLanguageInterface.toDecimal(pDecimal, getStaticLocale() /* CpxLanguageInterface.getSystemLocaleShort() */);
        }
        if (value == null || value.isEmpty()) {
            //Fallback, international format
            value = CpxLanguageInterface.toDecimal(pDecimal, "en");
        }
        return value;
        //return PLACEHOLDER;
    }

    public static String toDecimal(final Double pDouble) {
        String value = "";
        if (cpxLanguage != null) {
            value = cpxLanguage.toDecimal(pDouble);
        }
        if (value == null || value.isEmpty()) {
            value = CpxLanguageInterface.toDecimal(pDouble, getStaticLocale() /* CpxLanguageInterface.getSystemLocaleShort() */);
        }
        if (value == null || value.isEmpty()) {
            //Fallback, international format
            value = CpxLanguageInterface.toDecimal(pDouble, "en");
        }
        return value;
        //return PLACEHOLDER;
    }

    public static String toDecimal(final Long pLong) {
        String value = "";
        if (cpxLanguage != null) {
            value = cpxLanguage.toDecimal(pLong);
        }
        if (value == null || value.isEmpty()) {
            value = CpxLanguageInterface.toDecimal(pLong, getStaticLocale() /* CpxLanguageInterface.getSystemLocaleShort() */);
        }
        if (value == null || value.isEmpty()) {
            //Fallback, international format
            value = CpxLanguageInterface.toDecimal(pLong, "en");
        }
        return value;
        //return PLACEHOLDER;
    }

    /**
     * parse given Double to Decimal with the given amount of fraction digits
     *
     * @param pDouble number to parse
     * @param pAmountOfDigits maximum number of fraction digits
     * @return parsed double value
     */
    public static String toDecimal(Double pDouble, Integer pAmountOfDigits) {
        //AWi-20170608-CPX-554: In some Cases(No Baserate found) pDouble can be null
        //to prevent NumberFormatException 0.0 is set instead
        pDouble = pDouble != null ? pDouble : 0.0d;
        DecimalFormat format = (DecimalFormat) NumberFormat.getNumberInstance(LocaleUtils.toLocale(getStaticLocale() /* CpxLanguageInterface.getSystemLocaleShort() */));//new DecimalFormat();//"0.#");
        format.setRoundingMode(RoundingMode.HALF_UP);
        format.setMaximumFractionDigits(pAmountOfDigits);
        format.setMinimumFractionDigits(pAmountOfDigits);
        return format.format(pDouble);
    }

    public static double round(Double pDouble, int pAmountOfDigits, RoundingMode pMode) {
        if (pDouble == null) {
            throw new IllegalArgumentException("Double value to round is null!");
        }
        if (pAmountOfDigits < 0) {
            throw new IllegalArgumentException("pAmountOfDigits is lower than 0!");
        }
        return Precision.round(pDouble, pAmountOfDigits, pMode.ordinal());
    }

    public static double round(Double pDouble, int pAmountOfDigits) {
        // this double rouning is for rare case when the double presents the end ziffer 5 as 49999... 
        // and we have to round on digit before 5
        // example: pDouble = 3,745, pAmoutOfDigits
        // estimated value 3,75, calculated - 3,74, because the input value was represented as 3,74499999
        return round(round(pDouble, pAmountOfDigits + 1, RoundingMode.HALF_UP), pAmountOfDigits, RoundingMode.HALF_UP);
    }

    public static LocalDate toLocalDate(final Date pDate) {
        LocalDate value = null;
        if (cpxLanguage != null) {
            value = cpxLanguage.toLocalDate(pDate);
        }
        if (value == null) {
            value = CpxLanguageInterface.toLocalDate(pDate, Lang.getDateFormat());
        }
        if (value == null) {
            //Fallback, international format
            value = CpxLanguageInterface.toLocalDate(pDate, CpxLanguageInterface.ISO_DATE);
        }
        return value;
        //return null;
    }

    public static LocalDateTime toLocalDateTime(final Date pDate) {
        LocalDateTime value = null;
        if (cpxLanguage != null) {
            value = cpxLanguage.toLocalDateTime(pDate);
        }
        if (value == null) {
            value = CpxLanguageInterface.toLocalDateTime(pDate, Lang.getDateTimeFormat());
        }
        if (value == null) {
            //Fallback, international format
            value = CpxLanguageInterface.toLocalDateTime(pDate, CpxLanguageInterface.ISO_DATETIME);
        }
        return value;
        //return null;
    }

    public static LocalTime toLocalTime(final Date pDate) {
        LocalTime value = null;
        if (cpxLanguage != null) {
            value = cpxLanguage.toLocalTime(pDate);
        }
        if (value == null) {
            value = CpxLanguageInterface.toLocalTime(pDate, Lang.getTimeFormat());
        }
        if (value == null) {
            //Fallback, international format
            value = CpxLanguageInterface.toLocalTime(pDate, CpxLanguageInterface.ISO_TIME);
        }
        return value;
        //return null;
    }

    public static String toTime(final Date pDate) {
        String value = "";
        if (cpxLanguage != null) {
            value = cpxLanguage.toTime(pDate);
        }
        if (value == null || value.isEmpty()) {
            value = CpxLanguageInterface.toDateTime(pDate, Lang.getTimeFormat());
        }
        if (value == null || value.isEmpty()) {
            //Fallback, international format
            value = toIsoTime(pDate);
        }
        return value;
        //return PLACEHOLDER;
    }

    public static String toTime(final LocalTime pLocalTime) {
        return toTime(CpxLanguageInterface.localToDate(pLocalTime));
    }

    public static String toTruncedDateTime(final Date pDate) {
        String value = "";
        if (cpxLanguage != null) {
            value = cpxLanguage.toTruncedDateTime(pDate);
        }
        if (value == null || value.isEmpty()) {
            value = CpxLanguageInterface.toTruncedDateTime(pDate, Lang.getDateTimeFormat(), Lang.getDateFormat());
        }
        if (value == null || value.isEmpty()) {
            value = CpxLanguageInterface.toTruncedDateTime(pDate, CpxLanguageInterface.ISO_DATETIME, CpxLanguageInterface.ISO_DATE);
        }
        return value;
        //return PLACEHOLDER;
    }

    public static String toIsoDate(final LocalDate pLocalDate) {
        return toIsoDate(CpxLanguageInterface.localToDate(pLocalDate));
    }

    /**
     * sets time to the beginning of the day (sets time to 00:00:00,000)
     *
     * @param pDate date
     * @return trunced date object
     */
    public static Date setTimeTo0000(final Date pDate) {
        if (pDate == null) {
            return null;
        }
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(pDate);
        return new GregorianCalendar(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).getTime();
    }

    /**
     * sets time to the end of the day (sets time to 23:59:59,999)
     *
     * @param pDate date
     * @return trunced date object
     */
    public static Date setTimeTo2359(final Date pDate) {
        if (pDate == null) {
            return null;
        }
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(pDate);
        final Calendar newCal = new GregorianCalendar(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH) + 1
        );
        newCal.add(Calendar.MILLISECOND, -1);
        return newCal.getTime();
    }

    public static Date fromIsoDate(final String pDate) {
        final String format = CpxLanguageInterface.ISO_DATE;
        try {
            return CpxLanguageInterface.toDateTime(pDate, format);
        } catch (ParseException ex) {
            LOG.log(Level.FINEST, "This does not fulfil the required date format '" + format + "': " + pDate, ex);
            return null;
        }
    }

    public static Date fromIsoDateTime(final String pDate) {
        final String format = CpxLanguageInterface.ISO_DATETIME;
        try {
            return CpxLanguageInterface.toDateTime(pDate, format);
        } catch (ParseException ex) {
            LOG.log(Level.FINEST, "This does not fulfil the required date format '" + format + "': " + pDate, ex);
            return null;
        }
    }

    public static Date fromIsoTime(final String pDate) {
        final String format = CpxLanguageInterface.ISO_TIME;
        try {
            return CpxLanguageInterface.toDateTime(pDate, format);
        } catch (ParseException ex) {
            LOG.log(Level.FINEST, "This does not fulfil the required date format '" + format + "': " + pDate, ex);
            return null;
        }
    }

    /**
     * YYYY-MM-DD
     *
     * @param pDate date to format
     * @return formatted date
     */
    public static String toIsoDate(final Date pDate) {
        return CpxLanguageInterface.toDateTime(pDate, CpxLanguageInterface.ISO_DATE);
    }

    /**
     * YYYY-MM-DD HH:MM:SS
     *
     * @param pDate date to format
     * @return formatted date/time
     */
    public static String toIsoDateTime(final Date pDate) {
        return CpxLanguageInterface.toDateTime(pDate, CpxLanguageInterface.ISO_DATETIME);
    }
    
    public static String toFileDateTime(final Date pDate){
        return CpxLanguageInterface.toDateTime(pDate, CpxLanguageInterface.FILE_DATETIME);
    }
    /**
     * HH:MM:SS
     *
     * @param pDate date to format
     * @return formatted time
     */
    public static String toIsoTime(final Date pDate) {
        return CpxLanguageInterface.toDateTime(pDate, CpxLanguageInterface.ISO_TIME);
    }

    /**
     * get Year of the Date Object
     *
     * @param pDate java.util.Date if null, new Date is used
     * @return Year of the Date
     */
    public static int toYear(final Date pDate) {
        return extractValue(pDate, Calendar.YEAR);
    }

    /**
     * Extracts Calendar Field from given Date
     *
     * @param pDate Date to extract Value from, if null new Date is used
     * @param calendarField Calaendar Field e.g. Calendar.YEAR ,
     * Calendar.DAY_OF_YEAR ..
     * @return int Value of the extracted Field
     */
    public static int extractValue(final Date pDate, int calendarField) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(pDate != null ? pDate : new Date());
        return cal.get(calendarField);
    }

    public static LocalDate getElapsedTime(Date dateFrom, LocalDate dateTo) {
        if (dateFrom != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateFrom);
            return dateTo.minusYears(cal.get(Calendar.YEAR)).minus(cal.get(Calendar.DAY_OF_YEAR), ChronoUnit.DAYS);//,ChronoUnit.YEARS);//ChronoField.MILLI_OF_DAY.getBaseUnit());

        }
        return LocalDate.now();
    }

    public static LocalDate getElapsedTimeToNow(Date dateFrom) {
        return getElapsedTime(dateFrom, LocalDate.now());
    }
    
    public static String formatBytes(long pAmountOfByte, int digits){
        String[] dictionary = { "bytes", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB" };
        int index = 0;
        double size = pAmountOfByte;
        for (index = 0; index < dictionary.length; index++) {
            if (size < 1024) {
                break;
            }
            size = size / 1024;
        }
        return String.format("%." + digits + "f", size) + " " + dictionary[index];
    }
    public static Double toMByte(Long amountOfByte) {
        if (amountOfByte == null || amountOfByte <= 0) {
            return 0.00;
        }
        double amountInMByte = Double.valueOf(amountOfByte) / (1024 * 1024);
        return amountInMByte;
    }

    public static long toDaysBetween(Date pStart, Date pEnd) {
        return ChronoUnit.DAYS.between(Instant.ofEpochMilli(pStart.getTime()), Instant.ofEpochMilli(pEnd.getTime()));
    }

    /**
     *
     * @param pStart admissiondate
     * @param pEnd dischargedate
     * @param pOffSet possible day offset, days between could return 0 if pStart
     * matches pEnd - but these days may counted as one day to avoid that,
     * offset should be 1
     * @return days Between admissiondate and dischargedate
     */
    public static int toDaysBetween(Date pStart, Date pEnd, int pOffSet) {
        if (pStart == null) {
            return -1;
        }
        if (pEnd == null) {
            return -1;
        }
        final int days = ((int) ((pEnd.getTime() - pStart.getTime()) / (1000 * 60 * 60 * 24)) + pOffSet);
        return days;
    }

    /**
     * @param pDouble double as string to detected fraction digits, used to
     * determine desired fractions from double patterns like #0.00 or plain 0.00
     * @return number of fraction digits
     */
    public static int toMinFractionDigits(String pDouble) {
        DecimalFormat df = new DecimalFormat(pDouble.replace(",", "."));
        return df.getMinimumFractionDigits();
    }

    /**
     * @param pDouble double as string to detected fraction digits, used to
     * determine desired fractions from double patterns like #0.00 or plain 0.00
     * @return number of fraction digits
     */
    public static int toMaxFractionDigits(String pDouble) {
        DecimalFormat df = new DecimalFormat(pDouble.replace(",", "."));
        return df.getMinimumFractionDigits();
    }

    /**
     * @param pDouble double as string to detected integer digits, used to
     * determine desired integer digits from double patterns like #0.00 or plain
     * 0.00
     * @return number of integer digits
     */
    public static int toMinIntegerDigits(String pDouble) {
        DecimalFormat df = new DecimalFormat(pDouble.replace(",", "."));
        return df.getMinimumIntegerDigits();
    }

    /**
     * @param pDouble double as string to detected integer digits, used to
     * determine desired integer digits from double patterns like #0.00 or plain
     * 0.00
     * @return number of integer digits
     */
    public static int toMaxIntegerDigits(String pDouble) {
        DecimalFormat df = new DecimalFormat(pDouble.replace(",", "."));
        return df.getMaximumIntegerDigits();
    }

    public static Date localToDate(final LocalDateTime pLocalDateTime) {
        return CpxLanguageInterface.localToDate(pLocalDateTime);
    }

    public static Date localToDate(final LocalDate pLocalDate) {
        return CpxLanguageInterface.localToDate(pLocalDate);
    }

    public static Date localToDate(final LocalTime pLocalTime) {
        return CpxLanguageInterface.localToDate(pLocalTime);
    }

    public static String abbreviate(String pText, int pLenght) {
        pText = Objects.requireNonNullElse(pText, "");
        pText = pText.trim();
        if (pLenght <= 0 || pText.length() <= pLenght) {
            return capitalize(pText);
        }
        StringBuilder builder = new StringBuilder();
        builder.append(pText.substring(0, pLenght));
        builder.append(".");
        return capitalize(builder.toString());
    }
    private static final Pattern CAPITALIZE_PATTERN = Pattern.compile("(\\b.{1})");

    public static String capitalize(String s) {
        return applyPattern(CAPITALIZE_PATTERN, s);
    }

    protected static String applyPattern(final Pattern pattern, final String s) {
        if(pattern == null){
            return null;
        }
        if(s == null){
            return null;
        }
        StringBuffer sb = new StringBuffer(s.length());
        Matcher m = pattern.matcher(s);
        while (m.find()) {
            m.appendReplacement(sb, m.group().toUpperCase());
        }
        m.appendTail(sb);
        return sb.toString();
    }
}
