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
package line;

import de.lb.cpx.str.utils.StrUtils;
import static de.lb.cpx.str.utils.StrUtils.toStr;
import static de.lb.cpx.str.utils.StrUtils.validateDbName;
import java.io.Serializable;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import static line.Field.Type.*;

/**
 *
 * @author Dirk Niemeier
 */
public class Field implements Comparable<Field>, Serializable {

    private static final long serialVersionUID = 1L;

    //final public static int DEFAULT_INT_LENGTH = 20;
    //final public static int DEFAULT_STRING_LENGTH = 255;
    //final public static int DEFAULT_BOOLEAN_LENGTH = 1;
    public static final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_DATE = "yyyy-MM-dd";
    public static final String FORMAT_TIME = "HH:mm";
    private static final Logger LOG = Logger.getLogger(Field.class.getName());

    public final int number;
    public final String name;
    public final int length;
    public final Type type;

    public Field(final Integer pFieldNumber, final String pFieldName, final Type pType) {
        this(pFieldNumber, pFieldName, pType, 0);
    }

    public Field(final Integer pFieldNumber, final String pFieldName, final Type pType, final Integer pFieldLength) {
        String fieldName = (pFieldName == null) ? "" : pFieldName.trim().toUpperCase();
        int fieldNumber = (pFieldNumber == null) ? 0 : pFieldNumber;
        int fieldLength = (pFieldLength == null) ? 0 : pFieldLength;
        if (fieldName.isEmpty()) {
            throw new IllegalArgumentException("No field name was given!");
        }
        validateDbName(fieldName);
        if (fieldNumber <= 0) {
            throw new IllegalArgumentException("No valid field number was given (must be > 0)!");
        }
        if (fieldLength < 0) {
            throw new IllegalArgumentException("No valid field length was given (must be >= 0)!");
        }
        if (pType == null) {
            throw new IllegalArgumentException("No valid field type was given!");
        }

        //Use default length
        if (fieldLength <= 0) {
            fieldLength = pType.defaultLength;
        }

        name = fieldName;
        number = fieldNumber;
        length = fieldLength;
        type = pType;
    }

    public int getNextNumber() {
        return getNumber() + 1;
    }

    public int getPrevNumber() {
        return getNumber() - 1;
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public int getLength() {
        return length;
    }

    public Type getType() {
        return type;
    }

    public String getDbType(final boolean pIsOracle) {
        Type type = getType();
        int length = getLength();

        switch (type) {
            case STRING:
                return "VARCHAR(" + length + ")";
            case INT:
                return (pIsOracle ? "NUMBER(" + length + ")" : "INT");
            case FLOAT:
                return "FLOAT";
            case MONEY:
                return "FLOAT";
            case DATETIME:
                return (pIsOracle ? "DATE" : "DATETIME2");
            case DATE:
                return "DATE";
            case TIME:
                return "DATE";
            case BOOLEAN:
                return (pIsOracle ? "NUMBER(" + length + ")" : "BIT");
            case LONG:
                return (pIsOracle ? "NUMBER(" + length + ")" : "BIGINT");
        }
        throw new IllegalArgumentException("No database type defined for " + type.name());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Field other = (Field) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(final Field that) {
        if (this == that) {
            return 0;
        }
        int n1 = this.number;
        int n2 = that.number;
        if (n1 == n2) {
            return 0;
        }
        return (n1 > n2) ? 1 : -1;
    }

    public int getLengthOverflow(final String pValue) {
        if (pValue == null) {
            throw new IllegalArgumentException("Value is null!");
        }
        int valueLength = pValue.length();
        int maxLength = getLength();
        if (maxLength == 0) {
            return 0;
        }
        return valueLength - maxLength;
    }

    /*
  public boolean isLengthInvalid(final String pValue) {
    return (getLengthOverflow(pValue) > 0);
  }
     */
    public static enum Type {
        INT(Types.INTEGER, 20),
        FLOAT(Types.FLOAT, 0),
        MONEY(Types.FLOAT, 0),
        STRING(Types.VARCHAR, 255),
        DATETIME(Types.DATE, 0),
        DATE(Types.DATE, 0),
        TIME(Types.DATE, 0),
        BOOLEAN(Types.BOOLEAN, 1),
        LONG(Types.BIGINT, 19);

        public final int jdbcType;
        public final int defaultLength;

        public static Map<Integer, String> getAllJdbcTypeNames() throws IllegalArgumentException, IllegalAccessException {
            Map<Integer, String> result = new HashMap<>();
            for (java.lang.reflect.Field field : Types.class.getFields()) {
                result.put((Integer) field.get(null), field.getName());
            }
            return result;
        }

        public static String getJdbcTypeName(final int pType) throws IllegalArgumentException, IllegalAccessException {
            String typeName = getAllJdbcTypeNames().get(pType);
            return (typeName == null) ? "" : typeName.trim();
        }

        Type(final int pJdbcType, final int pDefaultLength) {
            this.jdbcType = pJdbcType;
            this.defaultLength = pDefaultLength;
        }

        public int getJdbcType() {
            return this.jdbcType;
        }

        public String getJdbcTypeName() {
            try {
                return getJdbcTypeName(getJdbcType());
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(Field.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }

        public int getDefaultLength() {
            return this.defaultLength;
        }
    }

    public static java.sql.Date parseDate(final String pDate) throws ParseException {
        return StrUtils.parseDate(pDate, Field.FORMAT_DATE);
    }

    public static java.sql.Date parseTime(final String pTime) throws ParseException {
        return StrUtils.parseDate(pTime, Field.FORMAT_TIME);
    }

    public static java.sql.Date parseDateTime(final String pDateTime) throws ParseException {
        return StrUtils.parseDate(pDateTime, Field.FORMAT_DATETIME);
    }

    public static java.sql.Timestamp parseTimestamp(final String pDateTime) throws ParseException {
        return StrUtils.parseTimestamp(pDateTime, Field.FORMAT_DATETIME);
    }

    public static String formatDate(final Date pDate) {
        String lStrDate = "";
        if (pDate != null) {
            SimpleDateFormat lDateFormat = new SimpleDateFormat(Field.FORMAT_DATE);
            lStrDate = lDateFormat.format(pDate);
        }
        return toStr(lStrDate);
    }

    public static String formatTime(final Date pTime) {
        String lStrDate = "";
        if (pTime != null) {
            SimpleDateFormat lDateFormat = new SimpleDateFormat(Field.FORMAT_TIME);
            lStrDate = lDateFormat.format(pTime);
        }
        return toStr(lStrDate);
    }

    public static String formatDateTime(final Date pDateTime) {
        String lStrDate = "";
        if (pDateTime != null) {
            SimpleDateFormat lDateFormat = new SimpleDateFormat(Field.FORMAT_DATETIME);
            lStrDate = lDateFormat.format(pDateTime);
        }
        return toStr(lStrDate);
    }

}
