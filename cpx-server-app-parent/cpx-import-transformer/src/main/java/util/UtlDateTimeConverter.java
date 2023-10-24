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
 *    2016  Somebody - initial API and implementation and/or initial documentation
 */
package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * this is a class from CheckpointDRG!
 *
 * @author niemeier
 */
public class UtlDateTimeConverter {

    private static UtlDateTimeConverter m_converter = null;
    public static final long ONE_HOUR = 60 * 60 * 1000L;
    private static final Logger LOG = Logger.getLogger(UtlDateTimeConverter.class.getName());

    /**
     *
     * @return instance of UtlDateTimeConverter
     */
    public static synchronized UtlDateTimeConverter converter() {
        /* 3.9.5 2015-09-01 DNi: #FINDBUGS - Wegen der Initialisierung des statischen Feldes m_converter sollte die Methode synchronized sein. */
        if (m_converter == null) {
            m_converter = new UtlDateTimeConverter();
        }
        return m_converter;
    }

    /**
     *
     * @param dtString datetime string
     * @param formatString format
     * @return date
     */
    public static synchronized Date getDateFromStringWithFormat(String dtString, String formatString) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(formatString);

            return format.parse(dtString);
        } catch (ParseException e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
            return new Date();
        }

    }

    /**
     *
     * @param strDate datetime string
     * @param format format
     * @return date
     */
    public static java.sql.Date convertStringToDate(String strDate, String format) {
        SimpleDateFormat dateformat = new SimpleDateFormat(format);
        Date datum;
        try {
            datum = dateformat.parse(strDate);
        } catch (ParseException ex) {
            datum = new Date();
        }
        java.sql.Date sqlDate = new java.sql.Date(datum.getTime());
        return sqlDate;
    }

    /**
     *
     * @param date1 date1
     * @param date2 date2
     * @return is same date?
     */
    public static boolean isOnSameDay(Date date1, Date date2) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        return fmt.format(date1).equals(fmt.format(date2));
    }

    private final SimpleDateFormat m_dateFormatEnglDet = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final SimpleDateFormat m_dateFormatEngl = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat m_dateFormatEnglDetLong = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private final SimpleDateFormat m_dateFormatGerm = new SimpleDateFormat("dd.MM.yyyy");
    private final SimpleDateFormat m_dateTimeFormatGerm = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    private final SimpleDateFormat m_dateFormatExpTime = new SimpleDateFormat("yyyyMMddHHmm");
    private final SimpleDateFormat m_dateFormatExp = new SimpleDateFormat("yyyyMMdd");
    private final SimpleDateFormat m_timeFormatExp = new SimpleDateFormat("HHmm");
    private final SimpleDateFormat m_dateFormatDetailTime = new SimpleDateFormat("yyyyMMddHHmmss");
    private final SimpleDateFormat m_dateUpload = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

//	private Calendar m_calendar = new GregorianCalendar();
    private java.sql.Date m_maxValue = null;

    /**
     *
     */
    public UtlDateTimeConverter() {
    }

    /**
     *
     * @param dt date
     * @return year
     */
    public int getYear(java.util.Date dt) {
        if (dt != null) {
            Calendar m_calendar = new GregorianCalendar();
            m_calendar.setTime(dt);
            return m_calendar.get(Calendar.YEAR);
        } else {
            return 0;
        }
    }

    /**
     *
     * @return current year
     */
    public int getCurrentYear() {
        java.util.Date dt = new Date(System.currentTimeMillis());
        Calendar m_calendar = new GregorianCalendar();
        m_calendar.setTime(dt);
        return m_calendar.get(Calendar.YEAR);
    }

    /**
     *
     * @param year year
     * @return date date
     */
    public java.util.Date getStartDateForYear(int year) {
        Calendar m_calendar = new GregorianCalendar();
        m_calendar.set(Calendar.YEAR, year);
        m_calendar.set(Calendar.MONTH, Calendar.JANUARY);
        m_calendar.set(Calendar.DAY_OF_MONTH, 1);
        m_calendar.set(Calendar.HOUR_OF_DAY, 0);
        m_calendar.set(Calendar.MINUTE, 0);
        m_calendar.set(Calendar.SECOND, 0);
        m_calendar.set(Calendar.MILLISECOND, 0);
        return m_calendar.getTime();
    }

    /**
     *
     * @param year year
     * @return date date
     */
    public long getStartDatetimeForYear(int year) {
        Calendar m_calendar = new GregorianCalendar();
        m_calendar.set(Calendar.YEAR, year);
        m_calendar.set(Calendar.MONTH, Calendar.JANUARY);
        m_calendar.set(Calendar.DAY_OF_MONTH, 1);
        m_calendar.set(Calendar.HOUR_OF_DAY, 0);
        m_calendar.set(Calendar.MINUTE, 0);
        m_calendar.set(Calendar.SECOND, 0);
        m_calendar.set(Calendar.MILLISECOND, 0);
        return m_calendar.getTimeInMillis();
    }

    /**
     *
     * @param year year
     * @param quarter quarter
     * @return date
     */
    public java.util.Date getStartDateForQuarter(int year, int quarter) {
        Calendar m_calendar = new GregorianCalendar();
        m_calendar.set(Calendar.YEAR, year);
        switch (quarter) {
            case 4:
                m_calendar.set(Calendar.MONTH, Calendar.OCTOBER);
                break;
            case 3:
                m_calendar.set(Calendar.MONTH, Calendar.JULY);
                break;
            case 2:
                m_calendar.set(Calendar.MONTH, Calendar.APRIL);
                break;
            default:
                m_calendar.set(Calendar.MONTH, Calendar.JANUARY);
                break;
        }
        m_calendar.set(Calendar.DAY_OF_MONTH, 1);
        m_calendar.set(Calendar.HOUR_OF_DAY, 0);
        m_calendar.set(Calendar.MINUTE, 0);
        m_calendar.set(Calendar.SECOND, 0);
        m_calendar.set(Calendar.MILLISECOND, 0);
        return m_calendar.getTime();
    }

    /**
     *
     * @param year year
     * @param quarter quarter
     * @return date
     */
    public long getStartDatetimeForQuarter(int year, int quarter) {
        Calendar m_calendar = new GregorianCalendar();
        m_calendar.set(Calendar.YEAR, year);
        switch (quarter) {
            case 4:
                m_calendar.set(Calendar.MONTH, Calendar.OCTOBER);
                break;
            case 3:
                m_calendar.set(Calendar.MONTH, Calendar.JULY);
                break;
            case 2:
                m_calendar.set(Calendar.MONTH, Calendar.APRIL);
                break;
            default:
                m_calendar.set(Calendar.MONTH, Calendar.JANUARY);
                break;
        }
        m_calendar.set(Calendar.DAY_OF_MONTH, 1);
        m_calendar.set(Calendar.HOUR_OF_DAY, 0);
        m_calendar.set(Calendar.MINUTE, 0);
        m_calendar.set(Calendar.SECOND, 0);
        m_calendar.set(Calendar.MILLISECOND, 0);
        return m_calendar.getTimeInMillis();
    }

    /**
     *
     * @param year year
     * @return date
     */
    public java.util.Date getEndDateForYear(int year) {
        Calendar m_calendar = new GregorianCalendar();
        m_calendar.set(Calendar.YEAR, year);
        m_calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        m_calendar.set(Calendar.DAY_OF_MONTH, 31);
        m_calendar.set(Calendar.HOUR_OF_DAY, 23);
        m_calendar.set(Calendar.MINUTE, 59);
        m_calendar.set(Calendar.SECOND, 59);
        m_calendar.set(Calendar.MILLISECOND, 0);
        return m_calendar.getTime();
    }

    /**
     *
     * @param year year
     * @return date
     */
    public long getEndDatetimeForYear(int year) {
        Calendar m_calendar = new GregorianCalendar();
        m_calendar.set(Calendar.YEAR, year);
        m_calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        m_calendar.set(Calendar.DAY_OF_MONTH, 31);
        m_calendar.set(Calendar.HOUR_OF_DAY, 23);
        m_calendar.set(Calendar.MINUTE, 59);
        m_calendar.set(Calendar.SECOND, 59);
        m_calendar.set(Calendar.MILLISECOND, 0);
        return m_calendar.getTimeInMillis();
    }

    /**
     *
     * @param year year
     * @param quarter quarter
     * @return date
     */
    public java.util.Date getEndDateForQuarter(int year, int quarter) {
        Calendar m_calendar = new GregorianCalendar();
        m_calendar.set(Calendar.YEAR, year);
        switch (quarter) {
            case 4:
                m_calendar.set(Calendar.MONTH, Calendar.DECEMBER);
                m_calendar.set(Calendar.DAY_OF_MONTH, 31);
                break;
            case 3:
                m_calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
                m_calendar.set(Calendar.DAY_OF_MONTH, 30);
                break;
            case 2:
                m_calendar.set(Calendar.MONTH, Calendar.JUNE);
                m_calendar.set(Calendar.DAY_OF_MONTH, 30);
                break;
            default:
                m_calendar.set(Calendar.MONTH, Calendar.MARCH);
                m_calendar.set(Calendar.DAY_OF_MONTH, 31);
                break;
        }
        m_calendar.set(Calendar.HOUR_OF_DAY, 23);
        m_calendar.set(Calendar.MINUTE, 59);
        m_calendar.set(Calendar.SECOND, 59);
        m_calendar.set(Calendar.MILLISECOND, 0);
        return m_calendar.getTime();
    }

    /**
     *
     * @param year year
     * @param quarter quarter
     * @return date
     */
    public long getEndDatetimeForQuarter(int year, int quarter) {
        Calendar m_calendar = new GregorianCalendar();
        m_calendar.set(Calendar.YEAR, year);
        switch (quarter) {
            case 4:
                m_calendar.set(Calendar.MONTH, Calendar.DECEMBER);
                m_calendar.set(Calendar.DAY_OF_MONTH, 31);
                break;
            case 3:
                m_calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
                m_calendar.set(Calendar.DAY_OF_MONTH, 30);
                break;
            case 2:
                m_calendar.set(Calendar.MONTH, Calendar.JUNE);
                m_calendar.set(Calendar.DAY_OF_MONTH, 30);
                break;
            default:
                m_calendar.set(Calendar.MONTH, Calendar.MARCH);
                m_calendar.set(Calendar.DAY_OF_MONTH, 31);
                break;
        }
        m_calendar.set(Calendar.HOUR_OF_DAY, 23);
        m_calendar.set(Calendar.MINUTE, 59);
        m_calendar.set(Calendar.SECOND, 59);
        m_calendar.set(Calendar.MILLISECOND, 0);
        return m_calendar.getTimeInMillis();
    }

    /**
     *
     * @return date
     */
    public Calendar getCurrentCalendar() {
        Calendar m_calendar = new GregorianCalendar();
        m_calendar.setTimeInMillis(System.currentTimeMillis());
        return m_calendar;
    }

    /**
     *
     * @param dt date
     * @return date
     */
    public Calendar getCalendar(java.util.Date dt) {
        Calendar m_calendar = new GregorianCalendar();
        if (dt != null) {
            m_calendar.setTime(dt);
        }
        return m_calendar;
    }

    /**
     *
     * @param dt date
     * @return date
     */
    public String formatToGermanDate(java.util.Date dt) {
        return formatToGermanDate(dt, false);
    }

    /**
     *
     * @param dt date
     * @param withTime with time?
     * @return date
     */
    public synchronized String formatToGermanDate(java.util.Date dt, boolean withTime) {
        if (dt == null) {
            return "";
        }
        if (withTime) {
            return m_dateTimeFormatGerm.format(dt);
        } else {
            return m_dateFormatGerm.format(dt);
        }
    }

    /**
     *
     * @param destDate destination date
     * @param sourceDate source date
     * @return date
     */
    public java.util.Date setTime(java.util.Date destDate, java.util.Date sourceDate) {
        if (destDate == null) {
            destDate = new Date(System.currentTimeMillis());
        }
        if (sourceDate != null) {
            Calendar m_calendar = new GregorianCalendar();
            m_calendar.setTime(sourceDate);
            int hh = m_calendar.get(Calendar.HOUR_OF_DAY);
            int mm = m_calendar.get(Calendar.MINUTE);
            m_calendar.setTime(destDate);
            m_calendar.set(Calendar.HOUR_OF_DAY, hh);
            m_calendar.set(Calendar.MINUTE, mm);
            m_calendar.set(Calendar.SECOND, 0);
            m_calendar.set(Calendar.MILLISECOND, 0);
            return m_calendar.getTime();
        }
        return destDate;
    }

    /**
     *
     * @return date
     */
    public synchronized String patternDateToStringEnglDetail() {
        return m_dateFormatEnglDet.toPattern();
    }

    /**
     *
     * @param dt date
     * @param resetTime reset time
     * @return date
     */
    public synchronized String convertDateToStringEnglDetail(java.util.Date dt, boolean resetTime) {
        if (dt == null) {
            return "";
        }
        if (resetTime) {
            Calendar m_calendar = new GregorianCalendar();
            m_calendar.setTime(dt);
            m_calendar.set(Calendar.HOUR_OF_DAY, 0);
            m_calendar.set(Calendar.MINUTE, 0);
            m_calendar.set(Calendar.SECOND, 0);
            m_calendar.set(Calendar.MILLISECOND, 0);
            return m_dateFormatEnglDet.format(m_calendar.getTime());
        } else {
            return m_dateFormatEnglDet.format(dt);
        }
    }

    /**
     *
     * @param dt date
     * @return date
     */
    public synchronized String convertDateToStringEngl(java.util.Date dt) {
        if (dt == null) {
            return "";
        }
        return m_dateFormatEngl.format(dt);
    }

    /* Umbau 18.10.2012 durch LUR
	 * Ersetzung des Aufrufes der Methode convertDateToStringEnglDetail(java.util.Date dt, boolean resetTime) 
	 * durch den eigentlichen Funktionsrumpf zur Vermeidung von Deadlocks in diesem Bereich
     */
    /**
     *
     * @param dt date
     * @param resetTime reset time
     * @param withMax with max?
     * @return date
     */
    public synchronized String convertDateToStringEnglDetail(java.util.Date dt, boolean resetTime, boolean withMax) {
        if (dt == null) {
            return "";
        }
        if (withMax) {
            Calendar m_calendar = new GregorianCalendar();
            m_calendar.setTime(dt);
            m_calendar.set(Calendar.HOUR_OF_DAY, 23);
            m_calendar.set(Calendar.MINUTE, 59);
            m_calendar.set(Calendar.SECOND, 59);
            m_calendar.set(Calendar.MILLISECOND, 0);
            return m_dateFormatEnglDet.format(m_calendar.getTime());
        } else {
            //return convertDateToStringEnglDetail(dt, resetTime);
            if (resetTime) {
                Calendar m_calendar = new GregorianCalendar();
                m_calendar.setTime(dt);
                m_calendar.set(Calendar.HOUR_OF_DAY, 0);
                m_calendar.set(Calendar.MINUTE, 0);
                m_calendar.set(Calendar.SECOND, 0);
                m_calendar.set(Calendar.MILLISECOND, 0);
                return m_dateFormatEnglDet.format(m_calendar.getTime());
            } else {
                return m_dateFormatEnglDet.format(dt);
            }
        }
    }

    /**
     *
     * @return date
     */
    public synchronized String patternDateToStringEnglDetailLong() {
        return m_dateFormatEnglDetLong.toPattern();
    }

    /**
     *
     * @param dt date
     * @param resetTime reset time
     * @return date
     */
    public synchronized String convertDateToStringEnglDetailLong(java.util.Date dt, boolean resetTime) {
        if (dt == null) {
            return "";
        }
        if (resetTime) {
            Calendar m_calendar = new GregorianCalendar();
            m_calendar.setTime(dt);
            m_calendar.set(Calendar.HOUR_OF_DAY, 0);
            m_calendar.set(Calendar.MINUTE, 0);
            m_calendar.set(Calendar.SECOND, 0);
            m_calendar.set(Calendar.MILLISECOND, 0);
            return m_dateFormatEnglDetLong.format(m_calendar.getTime());
        } else {
            return m_dateFormatEnglDetLong.format(dt);
        }
    }

    /* Umbau 18.10.2012 durch LUR
	 * Ersetzung des Aufrufes der Methode convertDateToStringEnglDetail(java.util.Date dt, boolean resetTime) 
	 * durch den eigentlichen Funktionsrumpf zur Vermeidung von Deadlocks in diesem Bereich
     */
    /**
     *
     * @param dt date
     * @param resetTime reset time
     * @param withMax with max
     * @return date
     */
    public synchronized String convertDateToStringEnglDetailLong(java.util.Date dt, boolean resetTime, boolean withMax) {
        if (dt == null) {
            return "";
        }
        if (withMax) {
            Calendar m_calendar = new GregorianCalendar();
            m_calendar.setTime(dt);
            m_calendar.set(Calendar.HOUR_OF_DAY, 23);
            m_calendar.set(Calendar.MINUTE, 59);
            m_calendar.set(Calendar.SECOND, 59);
            m_calendar.set(Calendar.MILLISECOND, 000);
            return m_dateFormatEnglDetLong.format(m_calendar.getTime());
        } else {
            //return convertDateToStringEnglDetail(dt, resetTime);
            if (resetTime) {
                Calendar m_calendar = new GregorianCalendar();
                m_calendar.setTime(dt);
                m_calendar.set(Calendar.HOUR_OF_DAY, 0);
                m_calendar.set(Calendar.MINUTE, 0);
                m_calendar.set(Calendar.SECOND, 0);
                m_calendar.set(Calendar.MILLISECOND, 0);
                return m_dateFormatEnglDet.format(m_calendar.getTime());
            } else {
                return m_dateFormatEnglDet.format(dt);
            }
        }
    }

    /**
     *
     * @param dt date
     * @param withTime with time?
     * @return date
     */
    public synchronized String convertDateToExportString(java.util.Date dt, boolean withTime) {
        if (dt == null) {
            return "";
        }
        if (withTime) {
            return this.m_dateFormatExpTime.format(dt);
        } else {
            return this.m_dateFormatExp.format(dt);
        }
    }

    /**
     *
     * @param dt date
     * @return date
     */
    public synchronized String convertDateToUpload(java.util.Date dt) {
        if (dt == null) {
            return "";
        }
        return this.m_dateUpload.format(dt);
    }

    /**
     *
     * @param dt date
     * @return date
     */
    public synchronized Date convertStringToUpload(String dt) {
        try {
            if (dt == null) {
                return null;
            }
            return this.m_dateUpload.parse(dt);
        } catch (ParseException ex) {
            return null;
        }
    }

    /**
     *
     * @param dt date
     * @return date
     */
    public synchronized String convertDateToDetailedString(java.util.Date dt) {
        if (dt == null) {
            return "";
        }
        return this.m_dateFormatDetailTime.format(dt);
    }

    /**
     *
     * @param dt date
     * @return date
     */
    public synchronized String convertTimeToExportString(java.util.Date dt) {
        if (dt == null) {
            return "";
        } else {
            return this.m_timeFormatExp.format(dt);
        }
    }

    /**
     *
     * @param startDate start date
     * @param endDate end date
     * @return difference in days
     */
    public int getDateDifferenzDays(java.util.Date startDate, java.util.Date endDate) {
        if (endDate == null) {
            endDate = new java.util.Date(System.currentTimeMillis());
        }
//        if (endDate == null) {
//            endDate = new java.util.Date(System.currentTimeMillis());
//        }
        long diff = endDate.getTime() - startDate.getTime();
        if (diff > 0) {
            return (int) (diff / 1000 / 60 / 60 / 24);
        } else {
            return 0;
        }
    }

    /**
     *
     * @param strDate date
     * @return date
     */
    public synchronized Date convertStringToDate(String strDate) {
        Date date = null;
        try {
            date = m_dateFormatExp.parse(strDate);
        } catch (ParseException ex) {
        }
        return date;
    }

    /**
     *
     * @param dt date
     * @return date
     */
    public Date resetTime(Date dt) {
        Calendar m_calendar = new GregorianCalendar();
        m_calendar.setTime(dt);
        m_calendar.set(Calendar.HOUR, 0);
        m_calendar.set(Calendar.HOUR_OF_DAY, 0);
        m_calendar.set(Calendar.MINUTE, 0);
        m_calendar.set(Calendar.SECOND, 0);
        m_calendar.set(Calendar.MILLISECOND, 1000);
        return m_calendar.getTime();
    }

    /**
     *
     * @param dt date
     * @return date
     */
    public long getDateInMillisWithoutTime(java.sql.Date dt) {
        Calendar m_calendar = new GregorianCalendar();
        m_calendar.setTime(dt);
        m_calendar.set(Calendar.HOUR, 0);
        m_calendar.set(Calendar.HOUR_OF_DAY, 0);
        m_calendar.set(Calendar.MINUTE, 0);
        m_calendar.set(Calendar.SECOND, 0);
        m_calendar.set(Calendar.MILLISECOND, 1000);
        return m_calendar.getTimeInMillis();
    }

    /**
     *
     * @param d1 date1
     * @param d2 date2
     * @return difference in ms
     */
    public long daysBetween(Date d1, Date d2) {
        return daysBetween(d2.getTime(), d1.getTime());
    }

    /**
     *
     * @param d1 date1
     * @param d2 date2
     * @return difference in ms
     */
    public long daysBetween(long d1, long d2) {
        return ((d2 - d1 + ONE_HOUR)
                / (ONE_HOUR * 24));
    }

    /**
     *
     * @param dt date
     * @return week day
     */
    public int getWeekDay(java.util.Date dt) {
        Calendar m_calendar = new GregorianCalendar();
        m_calendar.setTime(dt);
        return m_calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     *
     * @return date
     */
    public java.sql.Date getMaximumDate() {
        if (m_maxValue == null) {
            Calendar m_calendar = new GregorianCalendar();
            m_calendar.set(Calendar.YEAR, 9999);
            m_calendar.set(Calendar.MONTH, Calendar.DECEMBER);
            m_calendar.set(Calendar.DAY_OF_MONTH, 31);
            m_calendar.set(Calendar.HOUR_OF_DAY, 0);
            m_calendar.set(Calendar.MINUTE, 0);
            m_calendar.set(Calendar.SECOND, 0);
            m_maxValue = new java.sql.Date(m_calendar.getTimeInMillis());
        }
        return new java.sql.Date(m_maxValue.getTime());
    }

    /**
     *
     * @param date date
     * @return date
     */
    public java.sql.Date setSystemTimeStamp2Date(java.sql.Date date) {
        java.sql.Date changedDate = null;
        GregorianCalendar calSystem = new GregorianCalendar();
        GregorianCalendar calReminderDate = new GregorianCalendar();

        if (date != null) {
            calSystem.setTimeInMillis(System.currentTimeMillis());
            calReminderDate.setTime(date);
            calReminderDate.set(GregorianCalendar.HOUR_OF_DAY, calSystem.get(GregorianCalendar.HOUR_OF_DAY));
            calReminderDate.set(GregorianCalendar.MINUTE, calSystem.get(GregorianCalendar.MINUTE));
            calReminderDate.set(GregorianCalendar.SECOND, calSystem.get(GregorianCalendar.SECOND));
            calReminderDate.set(GregorianCalendar.MILLISECOND, calSystem.get(GregorianCalendar.MILLISECOND));

            changedDate = new java.sql.Date(calReminderDate.getTimeInMillis());
        }
        if (changedDate == null) {
            changedDate = date;
        }
        return changedDate;
    }


    /*	public int calculateYearInteval(java.util.Date beginDate, java.util.Date endDate)
	{
		int years = 0;
		m_calendar1.setTime(endDate);
		m_calendar2.setTime(beginDate);
		if(m_calendar1.get(m_calendar1.MONTH) < m_calendar2.get(m_calendar2.MONTH)) {
			years = m_calendar1.get(m_calendar1.YEAR) - m_calendar2.get(m_calendar2.YEAR);
		} else if((m_calendar1.get(m_calendar1.MONTH) == m_calendar2.get(m_calendar2.MONTH)) &&
			((m_calendar1.get(m_calendar1.DATE) <= m_calendar2.get(m_calendar2.DATE)))) {
			years = m_calendar1.get(m_calendar1.YEAR) - m_calendar2.get(m_calendar2.YEAR);
		} else {
			years = m_calendar1.get(m_calendar1.YEAR) - m_calendar2.get(m_calendar2.YEAR) - 1;
		}
		return years;
	}*/
    /**
     *
     * @param beginDate begin date
     * @param endDate end date
     * @return interval
     */
    public int calculateDaysInteval(java.util.Date beginDate, java.util.Date endDate) {
        Calendar cal1 = new GregorianCalendar();
        cal1.setTime(beginDate);
        cal1.set(Calendar.HOUR_OF_DAY, 0);
        cal1.set(Calendar.MINUTE, 0);
        Calendar cal2 = new GregorianCalendar();
        cal2.setTime(endDate);
        cal2.set(Calendar.HOUR_OF_DAY, 0);
        cal2.set(Calendar.MINUTE, 0);
        long numDays = Math.round((cal2.getTime().getTime()
                - cal1.getTime().getTime())
                / (double) 86400000);
        return (int) numDays;
    }

    /**
     *
     * @param endDate1 end date
     * @param beginDate2 begin date
     * @return difference less than 24 hours
     */
    public boolean checkDiffLess24Hours(java.util.Date endDate1, java.util.Date beginDate2) {
        boolean isLess24 = false;
        if (endDate1 != null && beginDate2 != null) {
            long diff = beginDate2.getTime() - endDate1.getTime();
            if (diff <= (ONE_HOUR * 24)) {
                isLess24 = true;
            }
        }
        return isLess24;
    }

    /**
     *
     * @param dt date
     * @return date
     */
    public java.sql.Date getNextMidnightDate(long dt) {
        Calendar m_calendar = new GregorianCalendar();
        m_calendar.setTimeInMillis(dt);
        m_calendar.set(Calendar.HOUR, 0);
        m_calendar.set(Calendar.HOUR_OF_DAY, 0);
        m_calendar.set(Calendar.MINUTE, 0);
        m_calendar.set(Calendar.SECOND, 0);
        m_calendar.set(Calendar.MILLISECOND, 0);
        m_calendar.add(Calendar.DAY_OF_YEAR, 1);

        return new java.sql.Date(m_calendar.getTimeInMillis());
    }

    /**
     *
     * @param dt date
     * @return date
     */
    public java.sql.Date getPreviewDateLastTime(long dt) {
        Calendar m_calendar = new GregorianCalendar();
        m_calendar.setTimeInMillis(dt);
        m_calendar.set(Calendar.HOUR, 23);
        m_calendar.set(Calendar.HOUR_OF_DAY, 23);
        m_calendar.set(Calendar.MINUTE, 59);
        m_calendar.set(Calendar.SECOND, 59);
        m_calendar.set(Calendar.MILLISECOND, 0);
        m_calendar.add(Calendar.DAY_OF_YEAR, -1);

        return new java.sql.Date(m_calendar.getTimeInMillis());
    }

}
