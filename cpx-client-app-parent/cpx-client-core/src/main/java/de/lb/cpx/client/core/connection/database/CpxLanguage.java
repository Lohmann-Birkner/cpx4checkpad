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
package de.lb.cpx.client.core.connection.database;

import de.lb.cpx.model.lang.CpxLanguageInterface;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.StringConverter;

/**
 *
 * @author Dirk Niemeier
 */
public class CpxLanguage implements CpxLanguageInterface {

    private static CpxLanguage instance = null;
    //SimpleDateFormat dateFormatter = null;
    //SimpleDateFormat timeFormatter = null;
    //SimpleDateFormat dateTimeFormatter = null;
    //DecimalFormat decimalFormatter = null;

    private static final Logger LOG = Logger.getLogger(CpxLanguage.class.getSimpleName());

    public static synchronized CpxLanguage instance() {
        if (instance == null) {
            instance = new CpxLanguage();
        }
        return instance;
    }
    //private ResourceBundle resourceBundle = null;
    private String locale = null;
    private Connection conn = null;
    //private CpxDbManager cacheManager = null;
    //private File file = null;

    private CpxLanguage() {
        conn = CpxDbManager.instance().getLanguageDb();
    }

    @Override
    public synchronized void cleanUp() {
        //dateFormatter = null;
        //timeFormatter = null;
        //dateTimeFormatter = null;
        //decimalFormatter = null;
    }

    /**
     *
     * @param pLocale language
     * @param pFileContent content
     * @throws IOException file error
     * @throws IllegalArgumentException invalid argument
     */
    @Override
    public synchronized void setLanguageFile(final String pLocale, final String pFileContent) throws IOException {
        try {
            if (pFileContent == null) {
                throw new FileNotFoundException("No resource bundle for '" + pLocale + "' found!");
            }
            if (pFileContent.trim().isEmpty()) {
                Logger.getLogger(CpxLanguage.class.getName()).log(Level.SEVERE, "Content of resource bundle '" + pLocale + "' is empty!");
                return;
            }
            //Connection conn = Session.instance().cacheManager.getConnection();
            //CpxDbManager cacheManager = Session.instance().cacheManager;
            //cacheManager.createLanguageDb();
            createLanguageDb();
            String lLocale = (pLocale == null) ? "" : pLocale.trim().toLowerCase();
            if (lLocale.isEmpty()) {
                throw new IllegalArgumentException("Locale '" + pLocale + "' is not valid!");
            }
            cleanUp();
            locale = lLocale;
            Properties props = new Properties();
            //use UTF-8 to decode non-latin characters correctly!
            /*
      try (BufferedReader reader =
              new BufferedReader(new InputStreamReader(new FileInputStream(pFileContent), "UTF8"))) {
             */
            try ( BufferedReader reader
                    = new BufferedReader(new StringReader(pFileContent))) {
                props.load(reader);

                Enumeration<?> e = props.propertyNames();

                /*
        cacheManager.deleteLanguage(locale);
        cacheManager.getConnection().setAutoCommit(false);
                 */
                deleteLanguage(locale);
                conn.setAutoCommit(false);

                String sql = "INSERT INTO LANGUAGE(LOCALE, KEY, VALUE) VALUES (?, ?, ?)";
                try ( PreparedStatement pstmt = conn.prepareStatement(sql)) {

                    while (e.hasMoreElements()) {
                        String key = (String) e.nextElement();
                        String value = props.getProperty(key);

                        if (!isValidKey(key)) {
                            continue;
                        }

                        /*
            if (value.isEmpty()) {
              continue;
            }
                         */
                        //key = CpxLanguageInterface.getKey(key);
//            System.out.println(key);
                        pstmt.setString(1, pLocale);
                        pstmt.setString(2, key);
                        pstmt.setString(3, value);
                        pstmt.addBatch();
                    }
                    pstmt.executeBatch();
                } catch (SQLException ex) {
                    Logger.getLogger(CpxLanguage.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            conn.setAutoCommit(true);
        } catch (SQLException ex) {
            Logger.getLogger(CpxLanguage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean deleteLanguage(final String pLocale) {
        String sql = "DELETE FROM LANGUAGE WHERE LOCALE = ?";
        try ( PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pLocale);
            return pstmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public synchronized boolean createLanguageDb() {
        try ( Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS LANGUAGE "
                    + "(ID     INTEGER   PRIMARY KEY   AUTOINCREMENT,"
                    + " LOCALE TEXT               NOT NULL, "
                    + " KEY    TEXT               NOT NULL, "
                    + " VALUE  TEXT               NOT NULL ) ";
            stmt.executeUpdate(sql);

            sql = "CREATE UNIQUE INDEX IF NOT EXISTS IDX_KEY ON LANGUAGE (LOCALE, KEY)";
            stmt.executeUpdate(sql);

            stmt.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean isValidKey(final String pKey) {
        String key = (pKey == null) ? "" : pKey.trim();
        if (key.isEmpty()) {
            return false;
        }
        if (key.startsWith("[") && key.endsWith("]")) {
            //is section, don't care!
            return false;
        }
        if (key.startsWith("#")) {
            //is comment, don't care!
            return false;
        }
        if (key.startsWith(";")) {
            //is comment, don't care!
            return false;
        }
        return true;
    }

    @Override
    public String getLocale() {
        return locale;
    }

    @Override
    public String get(final String pKey) {
        return get(pKey, true);
    }

    @Override
    public String get(final String pKey, final boolean pUsePlaceholderIfNotFound) {
        return get(pKey, pUsePlaceholderIfNotFound, (Object[]) null);
    }

    @Override
    public String get(final String pKey, Object... pParams) {
        return get(pKey, true, pParams);
    }

    @Override
    public String get(final String pKey, final boolean pUsePlaceholderIfNotFound, Object... pParams) {
        //String key = CpxLanguageInterface.getKey(pKey);
        String key = (pKey == null) ? "" : pKey.trim();
        if (key.isEmpty()) {
            return "";
        }
        /*
    if(pKey.equals(Lang.DIALOG_BATCH_GROUPING_DRGRESULT)){
        String s = "";
    }
         */
        String value = "";

        String sql = "SELECT VALUE FROM LANGUAGE WHERE LOCALE LIKE ? AND KEY LIKE ?";
        try ( PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, locale);
            pstmt.setString(2, key);
            try ( ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    value = rs.getString(1);
                    value = (value == null) ? "" : value;
                    break;
                }
                rs.close();
            } finally {
                if (pstmt != null) {
                    pstmt.close();
                }
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

        if (value.isEmpty()) {
            if (pUsePlaceholderIfNotFound) {
                value = CpxLanguageInterface.toPlaceholder(pKey);
            }
            LOG.log(Level.FINEST, "Warning: cannot find translation for locale '" + locale + "' and key '" + pKey + "', return '" + value + "' instead");
        }
        /* else {
      value = AbstractLang.getValue(value);
    } */

        value = CpxLanguageInterface.replaceParams(value, pParams);
        return value;
    }

    @Override
    public synchronized String formatDateTime(final Date pDate, final String pFormat) {
        return CpxLanguageInterface.toDateTime(pDate, pFormat);
        /*
    if (pDate == null) {
      return "";
    }
    if (timeFormatter == null) {
      timeFormatter = new SimpleDateFormat(getTimeFormat());
    }
    return timeFormatter.format(pDate);
         */
    }

    @Override
    public synchronized String toTime(final Date pDate) {
        return CpxLanguageInterface.toDateTime(pDate, getTimeFormat());
        /*
    if (pDate == null) {
      return "";
    }
    if (timeFormatter == null) {
      timeFormatter = new SimpleDateFormat(getTimeFormat());
    }
    return timeFormatter.format(pDate);
         */
    }

    @Override
    public synchronized String toDate(final Date pDate) {
        return CpxLanguageInterface.toDateTime(pDate, getDateFormat());
        /*
    if (pDate == null) {
      return "";
    }
    if (dateFormatter == null) {
      dateFormatter = new SimpleDateFormat(getDateFormat());
    }
    return dateFormatter.format(pDate);
         */
    }

    /*
  @Override
  public String toIsoDate(final Date pDate) {
    if (pDate == null) {
      return "";
    }
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    return formatter.format(pDate);
  }
     */
    @Override
    public String toTruncedDateTime(final Date pDate) {
        return CpxLanguageInterface.toTruncedDateTime(pDate, getDateTimeFormat(), getDateFormat());
        /*
    if (date == null) {
      return "";
    }
    Calendar calendar = GregorianCalendar.getInstance();
    calendar.setTime(date);
    if (calendar.get(Calendar.HOUR_OF_DAY) != 0 || 
            calendar.get(Calendar.MINUTE) != 0 || 
            calendar.get(Calendar.SECOND) != 0) {
      return toDateTime(date);
    } else {
      return toDateTime(date);
    }
         */
    }

    @Override
    public synchronized String toDateTime(final Date pDate) {
        return CpxLanguageInterface.toDateTime(pDate, getDateTimeFormat());
        /*
    if (date == null) {
      return "";
    }
    if (dateTimeFormatter == null) {
      dateTimeFormatter = new SimpleDateFormat(getDateTimeFormat());
    }
    return dateTimeFormatter.format(date);
         */
    }

    @Override
    public synchronized LocalDate toLocalDate(final Date pDate) {
        return CpxLanguageInterface.toLocalDate(pDate, getDateFormat());
        /*
    if (date == null) {
      return null;
    }
    String dateStr = toDateTime(date);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormatter.toPattern());
    return LocalDate.parse(dateStr, formatter);
         */
    }

    @Override
    public synchronized LocalDateTime toLocalDateTime(final Date pDate) {
        return CpxLanguageInterface.toLocalDateTime(pDate, getDateTimeFormat());
        /*
    if (date == null) {
      return null;
    }
    String dateStr = toDateTime(date);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimeFormatter.toPattern());
    return LocalDateTime.parse(dateStr, formatter);
         */
    }

    @Override
    public synchronized LocalTime toLocalTime(final Date pDate) {
        return CpxLanguageInterface.toLocalTime(pDate, getTimeFormat());
        /*
    if (date == null) {
      return null;
    }
    String dateStr = toTime(date);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timeFormatter.toPattern());
    return LocalTime.parse(dateStr, formatter);
         */
    }

    @Override
    public synchronized String toDecimal(final BigDecimal pDecimal) {
        return CpxLanguageInterface.toDecimal(pDecimal, getLocale());
        /*
    if (decimal == null) {
      return "";
    }
    if (decimalFormatter == null) {
      NumberFormat numberFormatter = NumberFormat.getNumberInstance(LocaleUtils.toLocale(getLocale()));
      decimalFormatter = (DecimalFormat)numberFormatter;
    }
    return decimalFormatter.format(decimal);
         */
    }

    @Override
    public String toDecimal(final Double pDecimal) {
        return CpxLanguageInterface.toDecimal(pDecimal, getLocale());
        /*
    if (decimal == null) {
      return "";
    }
    return toDecimal(BigDecimal.valueOf(decimal));
         */
    }

    @Override
    public String toDecimal(final Long pDecimal) {
        return CpxLanguageInterface.toDecimal(pDecimal, getLocale());
        /*
    if (pDecimal == null) {
      return "";
    }
    return toDecimal(BigDecimal.valueOf(pDecimal));
         */
    }

    @Override
    public String getTimeFormat() {
        String format = get("TimeFormat", false);
        if (format.isEmpty()) {
            //International Fallback
            format = "HH:mm:ss";
        }
        return format;
    }

    @Override
    public String getDateFormat() {
        String format = get("DateFormat", false);
        if (format.isEmpty()) {
            //International Fallback
            format = "yyyy-MM-dd";
        }
        return format;
    }

    @Override
    public String getDateTimeFormat() {
        String format = get("DateTimeFormat", false);
        if (format.isEmpty()) {
            //International Fallback
            format = "yyyy-MM-dd HH:mm:ss";
        }
        return format;
    }

    @Override
    public boolean exists(final String pKey) {
        String key = CpxLanguageInterface.getKey(pKey);

        String sql = "SELECT COUNT(*) FROM LANGUAGE WHERE LOCALE = ? AND KEY = ?";
        try ( PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, locale);
            pstmt.setString(2, pKey);
            try ( ResultSet rs = pstmt.getResultSet()) {
                boolean val = rs.getInt(1) > 0;
                rs.close();
                return val;
            } finally {
                if (pstmt != null) {
                    pstmt.close();
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /*
  public String getPrefix() {
    return locale + ".";
  }
     */
 /*
  protected String transformKey(final String pKey) {
    return transformKey(pKey, true);
  }
     */
    public StringConverter<LocalDate> getLocalDateStringConverter() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(getDateFormat());
        return new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate object) {
                if (object == null) {
                    return "";
                }
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormatter.toPattern());
                return object.format(formatter);
            }

            @Override
            public LocalDate fromString(String string) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormatter.toPattern());
                return LocalDate.parse(string, formatter);
            }
        };
    }

    public Date toDateTimeFromLocalDateTime(LocalDateTime ldt) {
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }

    public Date toDate(LocalDate date, LocalTime dateTime) {
        return toDateTimeFromLocalDateTime(LocalDateTime.of(date, dateTime));
    }

    @Override
    public String toDateTime(LocalDateTime pLocalDateTime) {
        return toDateTime(CpxLanguageInterface.localToDate(pLocalDateTime));
    }

    @Override
    public String toDate(LocalDate pLocalDate) {
        return toDate(CpxLanguageInterface.localToDate(pLocalDate));
    }

    @Override
    public String toTime(LocalTime pLocalTime) {
        return toTime(CpxLanguageInterface.localToDate(pLocalTime));
    }

}
