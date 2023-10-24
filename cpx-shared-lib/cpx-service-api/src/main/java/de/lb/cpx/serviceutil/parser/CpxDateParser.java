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
package de.lb.cpx.serviceutil.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wilde
 */
public class CpxDateParser {

    private CpxDateParser() {
        //utility class needs no public constructor
    }

    //public final SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy-MM-dd");
    //public final SimpleDateFormat formatterDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat getDateFormatter() {
        return new SimpleDateFormat("yyyy-MM-dd");
    }

    public static SimpleDateFormat getDateTimeFormatter() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    //Access to formatterDate should be synchronized!
    public static Date parseDateExc(final String dateVal) throws ParseException {
        String val = (dateVal == null) ? "" : dateVal.trim();
        if (val.isEmpty()) {
            return null;
        }
        return getDateFormatter().parse(dateVal);
    }

    //Access to formatterDateTime should be synchronized!
    public static Date parseDateTimeExc(final String dateTimeVal) throws ParseException {
        String val = (dateTimeVal == null) ? "" : dateTimeVal.trim();
        if (val.isEmpty()) {
            return null;
        }
        return getDateTimeFormatter().parse(dateTimeVal);
    }

    public static Date parseDate(final String dateVal) {
        try {
            return parseDateExc(dateVal);
        } catch (ParseException ex) {
            Logger.getLogger(CpxDateParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static Date parseDateTime(final String dateVal) {
        try {
            return parseDateTimeExc(dateVal);
        } catch (ParseException ex) {
            Logger.getLogger(CpxDateParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
