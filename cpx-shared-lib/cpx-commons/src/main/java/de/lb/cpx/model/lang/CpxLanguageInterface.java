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
package de.lb.cpx.model.lang;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang3.LocaleUtils;

/**
 *
 * @author Dirk Niemeier
 */
public interface CpxLanguageInterface {

    public static final String DELIMITER = "\\*\\*\\*";
    public static final String ISO_DATE = "yyyy-MM-dd";
    public static final String ISO_TIME = "HH:mm:ss";
    public static final String ISO_DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final String FILE_DATETIME = "yyy-MM-dd HH-mm-ss";

    public static String getDELIMITER() {
        return DELIMITER;
    }

    public static String[] splitValue(final String pValue) {
        String value = (pValue == null) ? "" : pValue.trim();
        String[] arr = value.split(DELIMITER);
        //if (value.contains(DELIMITER)) {
        //  arr = value.split(DELIMITER);
        //} else {
        //  arr = new String[] { value };
        //}
        return arr;
    }

    public static String[] splitValue(final String pValue, final int pMinLength) {
        String[] arr = splitValue(pValue);
        if (arr.length < pMinLength) {
            String newArr[] = new String[pMinLength];
            Arrays.fill(newArr, "");
            System.arraycopy(arr, 0, newArr, 0, arr.length);
            arr = newArr;
        }
        return arr;
    }

    public static String getValue(final String pValue) {
        String[] arr = splitValue(pValue);
        return arr[0].trim();
    }

    public static String getAbbreviation(final String pValue) {
        String[] arr = splitValue(pValue);
        if (arr.length < 2) {
            return "";
        }
        String abbr = arr[1].trim();
        return abbr;
    }

    public static String getDescription(final String pValue) {
        String[] arr = splitValue(pValue);
        if (arr.length < 3) {
            return "";
        }
        String tooltip = arr[2].trim();
        return tooltip;
    }

    void cleanUp();

    /*
  public String getDecimalFormat() {
  String format = get("DecimalFormat", false);
  if (format.isEmpty()) {
  //International Fallback
  format = "###,###.##";
  }
  return format;
  }
     */
    boolean exists(final String pKey);

    String get(final String pKey);

    String get(final String pKey, final boolean pUsePlaceholderIfNotFound);

    String get(final String pKey, Object... pParams);

    String get(final String pKey, final boolean pUsePlaceholderIfNotFound, Object... pParams);

    String getDateFormat();

    String getDateTimeFormat();

    String getLocale();

    String getTimeFormat();

    void setLanguageFile(final String pLocale, final String pFileContent) throws IOException;

    String toDate(final Date pDate);

    String toDateTime(final Date pDate);

    String toDate(final LocalDate pLocalDate);

    String toDateTime(final LocalDateTime pLocalDateTime);

    String toDecimal(final BigDecimal pDecimal);

    String toDecimal(final Double pDouble);

    String toDecimal(final Long pLong);

    //String toIsoDate(final Date pDate);
    LocalDate toLocalDate(final Date pDate);

    public static String toPlaceholder(final String pKey) {
        String key = (pKey == null) ? "" : pKey.trim();
        String value = "*" + key.toUpperCase();
        return value;
    }

    public static String toDateTime(final Date pDate, final String pFormat) {
        if (pDate == null || pFormat == null || pFormat.trim().isEmpty()) {
            return "";
        }
        SimpleDateFormat dateTimeFormatter = new SimpleDateFormat(pFormat);
        return dateTimeFormatter.format(pDate);
    }

    public static Date toDateTime(final String pDate, final String pFormat) throws ParseException {
        if (pDate == null || pDate.trim().isEmpty() || pFormat == null || pFormat.trim().isEmpty()) {
            return null;
        }
        SimpleDateFormat dateTimeFormatter = new SimpleDateFormat(pFormat);
        return dateTimeFormatter.parse(pDate.trim());
    }

    public static LocalDate toLocalDate(final Date pDate, final String pFormat) {
        if (pDate == null || pFormat == null || pFormat.trim().isEmpty()) {
            return null;
        }
        String dateStr = CpxLanguageInterface.toDateTime(pDate, pFormat);
        SimpleDateFormat dateFormatter = new SimpleDateFormat(pFormat);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormatter.toPattern());
        return LocalDate.parse(dateStr, formatter);
    }

    public static LocalTime toLocalTime(final Date pDate, final String pFormat) {
        if (pDate == null || pFormat == null || pFormat.trim().isEmpty()) {
            return null;
        }
        String dateStr = CpxLanguageInterface.toDateTime(pDate, pFormat);
        SimpleDateFormat dateFormatter = new SimpleDateFormat(pFormat);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormatter.toPattern());
        return LocalTime.parse(dateStr, formatter);
    }

    public static LocalDateTime toLocalDateTime(final Date pDate, final String pFormat) {
        if (pDate == null || pFormat == null || pFormat.trim().isEmpty()) {
            return null;
        }
        String dateStr = CpxLanguageInterface.toDateTime(pDate, pFormat);
        SimpleDateFormat dateFormatter = new SimpleDateFormat(pFormat);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormatter.toPattern());
        return LocalDateTime.parse(dateStr, formatter);
    }

    public static String toTruncedDateTime(final Date pDate, final String pDateTimeFormat, final String pDateFormat) {
        if (pDate == null) {
            return "";
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(pDate);
        if (calendar.get(Calendar.HOUR_OF_DAY) != 0
                || calendar.get(Calendar.MINUTE) != 0
                || calendar.get(Calendar.SECOND) != 0) {
            return CpxLanguageInterface.toDateTime(pDate, pDateTimeFormat);
        } else {
            return CpxLanguageInterface.toDateTime(pDate, pDateFormat);
        }
    }

    public static String replaceParams(final String pValue, final Object... pParams) {
        if (pValue == null || pValue.trim().isEmpty()) {
            return pValue;
        }
        if (pParams != null) {
            return MessageFormat.format(pValue, pParams);
        }
        return pValue;
    }

    public static String getKey(final String pKey) {
        //return pKey;
        return transformKey(pKey);
    }

    public static String transformKey(final String pKey) {
        String key = (pKey == null) ? "" : pKey.trim().toLowerCase();
        key = key.trim().toLowerCase();
        if (!key.isEmpty()) {
            key = key.replace("_", "");
            key = key.replace(" ", "");
            key = key.replace("ä", "ae");
            key = key.replace("ü", "ue");
            key = key.replace("ö", "oe");
            key = key.replace("ß", "ss");
            key = key.replace(".", "_");
            /*
      if (pWithPrefix) {
        key = getPrefix() + key;
      }
             */
            while (true) {
                String tmp = key;
                key = key.replace("__", "_");
                if (tmp.equals(key)) {
                    break;
                }
            }
        }
        return key;
    }

    public static String toDecimal(final BigDecimal pDecimal, final String pLocale) {
        if (pDecimal == null || pLocale == null || pLocale.trim().isEmpty()) {
            return "";
        }
        NumberFormat numberFormatter = NumberFormat.getNumberInstance(LocaleUtils.toLocale(pLocale.toLowerCase()));
        DecimalFormat decimalFormatter = (DecimalFormat) numberFormatter;
        return decimalFormatter.format(pDecimal);
    }

    public static String getSystemLocale() {
        String locale = System.getProperty("user.language");
        locale = (locale == null) ? "" : locale.trim().toLowerCase();
        if (locale.isEmpty()) {
            return "de";
        }
        return locale;
    }

    public static String getSystemLocaleShort() {
        String locale = getSystemLocale();
        int pos = locale.indexOf('-');
        if (pos > -1) {
            locale = locale.substring(0, pos).trim();
        }
        return locale;
    }

    public static String toDecimal(final Double decimal, final String pLocale) {
        if (decimal == null) {
            return "";
        }
        return toDecimal(BigDecimal.valueOf(decimal), pLocale);
    }

    public static String toDecimal(final Long decimal, final String pLocale) {
        if (decimal == null) {
            return "";
        }
        return toDecimal(BigDecimal.valueOf(decimal), pLocale);
    }

    public static Date localToDate(final LocalDateTime pLocalDateTime) {
        if (pLocalDateTime == null) {
            return null;
        }
        Date dateTime = Date.from(pLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
        return dateTime;
    }

    public static Date localToDate(final LocalDate pLocalDate) {
        if (pLocalDate == null) {
            return null;
        }
        Date date = Date.from(pLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return date;
    }

    public static Date localToDate(final LocalTime pLocalTime) {
        if (pLocalTime == null) {
            return null;
        }
        int year = 1900; //dummy
        int month = 1; //dummy
        int day = 1; //dummy
        Instant instant = pLocalTime.atDate(LocalDate.of(year, month, day)).atZone(ZoneId.systemDefault()).toInstant();
        Date time = Date.from(instant);
        return time;
    }

    LocalDateTime toLocalDateTime(final Date pDate);

    LocalTime toLocalTime(final Date pDate);

    String formatDateTime(final Date pDate, final String pFormat);

    String toTime(final Date pDate);

    String toTime(final LocalTime pLocalTime);

    String toTruncedDateTime(final Date pDate);

}
