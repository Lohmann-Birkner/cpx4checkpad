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

import static de.lb.cpx.str.utils.StrUtils.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import static line.Field.Type.*;
import line.impl.BillLine;
import line.impl.CancelCaseLine;
import line.impl.CaseLine;
import line.impl.DepartmentLine;
import line.impl.DiagnoseLine;
import line.impl.DrugLine;
import line.impl.FeeLine;
import line.impl.KainInkaLine;
import line.impl.KainInkaPvtLine;
import line.impl.KainInkaPvvLine;
import line.impl.LabLine;
import line.impl.PatientLine;
import line.impl.ProcedureLine;
import line.impl.SapFiBillLine;
import line.impl.SapFiBillpositionLine;
import line.impl.SapFiOpenItemsLine;
import line.impl.WardLine;
import util.DbBuilder;

/**
 *
 * @author Dirk Niemeier
 */
public abstract class AbstractLine implements LineI {

    private static final long serialVersionUID = 1L;

    public static final String DELIMITER = ";";
    public static final String DELIMITER_REPLACEMENT = "#";
    public static final String LINEBREAK_REPLACEMENT = "~";
    public static final Field NR = new Field(1, "NR", STRING);
    public static final Field TP_SOURCE = new Field(2, "TP_SOURCE", STRING); //Third-Party Source (e.g. Table or Class)
    public static final Field TP_ID = new Field(3, "TP_ID", STRING); //Third-Party NR
    public static final Field IKZ = new Field(4, "IKZ", STRING, 20);
    public static final Field FALLNR = new Field(5, "FALLNR", STRING, 25);
    public static final Field PATNR = new Field(6, "PATNR", STRING, 50);

    protected static Field createField(final Set<Field> pFieldSet, final Integer pFieldNumber, final String pFieldName, final Field.Type pType) {
        return createField(pFieldSet, pFieldNumber, pFieldName, pType, 0);
    }

    public static String getDelimiter() {
        return DELIMITER;
    }

    public static String getDelimiterReplacement() {
        return DELIMITER_REPLACEMENT;
    }

    public static String getLinebreakReplacement() {
        return LINEBREAK_REPLACEMENT;
    }

    /*
  protected static Field createIkzField(final Integer pFieldNumber) {
  return new Field(pFieldNumber, "IKZ", Type.STRING, 20);
  }
  
  protected static Field createFallNrField(final Integer pFieldNumber) {
  return new Field(pFieldNumber, "FALLNR", Type.STRING, 20);
  }
     */
    protected static void addStandardFields(final Set<Field> pFieldSet) {
        if (pFieldSet == null) {
            throw new IllegalArgumentException("FieldSet is null!");
        }
        if (!pFieldSet.contains(NR)) {
            pFieldSet.add(NR);
        } else {
            return;
        }
        if (!pFieldSet.contains(TP_SOURCE)) {
            pFieldSet.add(TP_SOURCE);
        }
        if (!pFieldSet.contains(TP_ID)) {
            pFieldSet.add(TP_ID);
        }
        if (!pFieldSet.contains(IKZ)) {
            pFieldSet.add(IKZ);
        }
        if (!pFieldSet.contains(FALLNR)) {
            pFieldSet.add(FALLNR);
        }
        if (!pFieldSet.contains(PATNR)) {
            pFieldSet.add(PATNR);
        }
    }

    protected static Field createField(final Set<Field> pFieldSet, final Integer pFieldNumber, final String pFieldName, final Field.Type pType, final int pLength) {
        if (pFieldSet == null) {
            throw new IllegalArgumentException("FieldSet is null!");
        }
        addStandardFields(pFieldSet);
        /*
    if (!pFieldSet.contains(NR)) {
    pFieldSet.add(NR);
    }
    if (!pFieldSet.contains(TP_SOURCE)) {
    pFieldSet.add(TP_SOURCE);
    }
    if (!pFieldSet.contains(TP_ID)) {
    pFieldSet.add(TP_ID);
    }
         */
 /*
    if (!pFieldSet.contains(IKZ)) {
    pFieldSet.add(IKZ);
    }
    if (!pFieldSet.contains(FALLNR)) {
    pFieldSet.add(FALLNR);
    }
    if (!pFieldSet.contains(PATNR)) {
    pFieldSet.add(PATNR);
    }
         */
        Field field = new Field(pFieldNumber, pFieldName, pType, pLength);
        //Iterator<Field> it = pFieldSet.iterator();
        if (getFieldByNumber(pFieldSet, field.getNumber()) != null) {
            throw new IllegalArgumentException("Field with the given number '" + field.getNumber() + "' with the name '" + field.getName() + "' is already defined in this field set (perhaps you mean " + (field.getNumber() + 1) + " for '" + field.getName() + "'?)!");
        }
        int predNumber = field.getPrevNumber();
        if (field.getNumber() > 1 && getFieldByNumber(pFieldSet, predNumber) == null) {
            throw new IllegalArgumentException("Field with the predecessor number '" + predNumber + "' was not defined yet, create predecessor of field '" + field.getName() + "' first!");
        }
        if (getFieldByName(pFieldSet, field.getName()) != null) {
            throw new IllegalArgumentException("Field with the given name '" + field.getName() + "' is already defined in this field set!");
        }
//        if (field == null) {
//            throw new IllegalArgumentException("Field is null!");
//        }
        if (!pFieldSet.add(field)) {
            throw new IllegalStateException("Field with name '" + field.getName() + "' is already defined in this field set!");
        }
        return field;
    }

    public static Set<Field> getFields(final Set<Field> pFieldSet) {
        if (pFieldSet == null) {
            throw new IllegalArgumentException("FieldSet is null!");
        }
        Set<Field> copy = new TreeSet<>(pFieldSet);
        return copy;
    }

    public static Set<String> getFieldNames(final Set<Field> pFieldSet) {
        if (pFieldSet == null) {
            throw new IllegalArgumentException("FieldSet is null!");
        }
        Set<String> names = new TreeSet<>();
        Iterator<Field> it = pFieldSet.iterator();
        while (it.hasNext()) {
            Field tmp = it.next();
            names.add(tmp.getName());
        }
        return names;
    }

    public static Set<Integer> getFieldNumbers(final Set<Field> pFieldSet) {
        if (pFieldSet == null) {
            throw new IllegalArgumentException("FieldSet is null!");
        }
        Set<Integer> numbers = new TreeSet<>();
        Iterator<Field> it = pFieldSet.iterator();
        while (it.hasNext()) {
            Field tmp = it.next();
            numbers.add(tmp.getNumber());
        }
        return numbers;
    }

    public static Field getFieldByName(final Set<Field> pFieldSet, final String pFieldName) {
        if (pFieldSet == null) {
            throw new IllegalArgumentException("FieldSet is null!");
        }
        String fieldName = (pFieldName == null) ? "" : pFieldName.trim().toUpperCase();
        if (fieldName.isEmpty()) {
            throw new IllegalArgumentException("No field name was given!");
        }
        Field field = null;
        Iterator<Field> it = pFieldSet.iterator();
        while (it.hasNext()) {
            Field tmp = it.next();
            if (tmp.getName().equalsIgnoreCase(fieldName)) {
                field = tmp;
                break;
            }
        }
        return field;
    }

    public static Field getFieldByNumber(final Set<Field> pFieldSet, final Integer pFieldNumber) {
        if (pFieldSet == null) {
            throw new IllegalArgumentException("FieldSet is null!");
        }
        int fieldNumber = (pFieldNumber == null) ? 0 : pFieldNumber;
        if (fieldNumber <= 0) {
            throw new IllegalArgumentException("No field number was given!");
        }
        Field field = null;
        Iterator<Field> it = pFieldSet.iterator();
        while (it.hasNext()) {
            Field tmp = it.next();
            if (tmp.getNumber() == fieldNumber) {
                field = tmp;
                break;
            }
        }
        return field;
    }

    public static String headline(final Set<Field> pFieldSet) {
        if (pFieldSet == null) {
            throw new IllegalArgumentException("FieldSet is null!");
        }
        List<String> values = new ArrayList<>();
        Iterator<Field> it = pFieldSet.iterator();
        while (it.hasNext()) {
            Field field = it.next();
            values.add(field.name);
        }
        return join(values);
    }

    public static String join(final List<String> pValues) {
        if (pValues == null) {
            throw new IllegalArgumentException("No values given!");
        }
        Iterator<String> it = pValues.iterator();
        StringBuilder sb = new StringBuilder();
        while (it.hasNext()) {
            String value = it.next();
            if (sb.length() > 0) {
                sb.append(DELIMITER);
            }
            sb.append(value);
        }
        return sb.toString();
    }

    public static String serialize(final LineI pLine) {
        if (pLine == null) {
            throw new IllegalArgumentException("Line is null!");
        }
        Iterator<Map.Entry<Field, String>> it = pLine.getValues().entrySet().iterator();
        List<String> values = new ArrayList<>();
        while (it.hasNext()) {
            Map.Entry<Field, String> entry = it.next();
            //Field field = entry.getKey();
            String value = entry.getValue();
            values.add(value);
        }
        return join(values);
    }

    public static LineI unserialize(final Class<? extends AbstractLine> pClass, final String pCsvData) throws InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
        if (pClass == null) {
            throw new IllegalArgumentException("Class is null!");
        }
        AbstractLine object = pClass.getDeclaredConstructor().newInstance();
        return unserialize(object, pCsvData);
    }

    public static LineI unserialize(final AbstractLine pObject, final String pCsvData) {
        if (pObject == null) {
            throw new IllegalArgumentException("Object is null!");
        }
        String csvData = (pCsvData == null) ? "" : pCsvData.trim();
        if (csvData.isEmpty()) {
            throw new IllegalArgumentException("CSV Data is empty!");
        }
        String[] data = csvData.split(DELIMITER);
        Set<Field> fields = pObject.getFields();
        int i = 0;
        Iterator<Field> it = fields.iterator();
        while (it.hasNext()) {
            if (i >= data.length) {
                break;
            }
            Field field = it.next();
            String value = data[i];
            pObject.setValue(field, value);
            i++;
        }
        return pObject;
    }
    //ADD LINE CLASSES HERE TO CREATE IMEX TABLES AUTOMATICALLY!

    public static List<Class<? extends LineI>> getLineClasses() {
        List<Class<? extends LineI>> lst = new ArrayList<>();
        lst.add(BillLine.class);
        lst.add(CaseLine.class);
        lst.add(DepartmentLine.class);
        lst.add(DiagnoseLine.class);
        lst.add(FeeLine.class);
        lst.add(LabLine.class);
        lst.add(PatientLine.class);
        lst.add(ProcedureLine.class);
        lst.add(WardLine.class);
        lst.add(KainInkaLine.class);
        lst.add(KainInkaPvvLine.class);
        lst.add(KainInkaPvtLine.class);
        lst.add(SapFiBillLine.class);
        lst.add(SapFiBillpositionLine.class);
        lst.add(SapFiOpenItemsLine.class);
        lst.add(DrugLine.class);
        lst.add(CancelCaseLine.class);
        return lst;
        /*
        String packag = AbstractLine.class.getPackage().getName();
        List<Class<?>> classesTmp = ClassFinder.findTypeOfSubclass(packag, LineI.class);
        List<Class<LineI>> classes = new ArrayList<>();
        for(Class<?> clazz: classesTmp) {
        if (clazz == null) {
        continue;
        }
        classes.add((Class<LineI>) clazz);
        }
        return classes;
         */
    }

    public static List<LineEntity> getLineEntities() throws IllegalArgumentException, IllegalAccessException {
        List<LineEntity> classNames = new ArrayList<>();
        for (Class<? extends LineI> clazz : getLineClasses()) {
            String tableNameTmp = DbBuilder.getTableName(clazz.getSimpleName());
            String imexTmpTableName = "IMEX_" + tableNameTmp + "_TMP";
            validateDbName(imexTmpTableName);
            String imexTableName = "IMEX_" + tableNameTmp;
            validateDbName(imexTableName);
            String fileName = imexTmpTableName + ".CSV";
            fileName = fileName.toLowerCase();
            //DbBuilder.validateName(imexTmpFileName);
            Iterator<LineEntity> it = classNames.iterator();
            while (it.hasNext()) {
                LineEntity entityTmp = it.next();
                if (entityTmp.imexTmpTableName.equalsIgnoreCase(imexTmpTableName)) {
                    throw new IllegalStateException("Temporary Table name '" + imexTmpTableName + "' is ambigious! There are at least 2 classes that would create the table name!");
                }
                if (entityTmp.imexTableName.equalsIgnoreCase(imexTableName)) {
                    throw new IllegalStateException("Table name '" + imexTableName + "' is ambigious! There are at least 2 classes that would create the table name!");
                }
                if (entityTmp.imexTmpFileName.equalsIgnoreCase(fileName)) {
                    throw new IllegalStateException("File name '" + fileName + "' is ambigious! There are at least 2 classes that would create the same file!");
                }
            }
            Set<Field> fieldSet = getFields(clazz);
            LineEntity lineEntity = new LineEntity(clazz, imexTmpTableName, imexTableName, fileName, fieldSet);
            classNames.add(lineEntity);
        }
        return classNames;
    }

    public static Set<Field> getFields(final Class<? extends LineI> pLineClass) throws IllegalArgumentException, IllegalAccessException {
        if (pLineClass == null) {
            throw new IllegalArgumentException("No line class given!");
        }
        if (pLineClass.isInterface()) {
            throw new IllegalArgumentException("line class interface is not valid here!");
        }
        if (java.lang.reflect.Modifier.isAbstract(pLineClass.getModifiers())) {
            throw new IllegalArgumentException("abstract line class is not valid here!");
        }
        java.lang.reflect.Field[] fields = pLineClass.getDeclaredFields();
        java.lang.reflect.Field field = null;
        final String pFieldName = "FIELDS";
        for (java.lang.reflect.Field fieldTmp : fields) {
            if (fieldTmp.getName().equalsIgnoreCase(pFieldName)) {
                if (java.lang.reflect.Modifier.isStatic(fieldTmp.getModifiers())) {
                    field = fieldTmp;
                    break;
                }
            }
        }
        if (field == null) {
            throw new IllegalArgumentException("No static attribute named '" + pFieldName + "' found");
        }
        if (field.getType() != Set.class) {
            throw new IllegalArgumentException("static attribute '" + pFieldName + "' must be a set of fields");
        }
        List<Type> typeArguments = Arrays.asList(((ParameterizedType) (field.getGenericType())).getActualTypeArguments());
        if (typeArguments.isEmpty() || typeArguments.get(0) != Field.class) {
            throw new IllegalArgumentException("static attribute '" + pFieldName + "' must be a set of fields");
        }
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        Set<Field> fieldSet = (Set<Field>) field.get(null);
        return fieldSet;
    }

    public static String[] splitLine(final String pLine, final String pDelimiter) {
        String line = (pLine == null) ? "" : pLine.trim();
        if (line.isEmpty()) {
            throw new IllegalArgumentException("No line given");
        }
        final String delimiter = (pDelimiter == null || pDelimiter.isEmpty()) ? DELIMITER : pDelimiter;
        return line.split(delimiter, -1);
    }

    public static String[] splitLine(final String pLine) {
        return splitLine(pLine, DELIMITER);
    }
    protected final Map<Field, String> values = new TreeMap<>();
    //final AtomicBoolean mLineWritten = new AtomicBoolean(false);

    public AbstractLine() {
        //getFieldSet().add(IKZ);
        //getFieldSet().add(FALLNR);
        //getFieldSet().add(TP_SOURCE);
        //getFieldSet().add(TP_ID);
        Set<Field> fields = getFields();
        if (fields.isEmpty()) {
            throw new IllegalStateException("No fields defined!");
        }
        Iterator<Field> it = fields.iterator();
        while (it.hasNext()) {
            Field field = it.next();
            setValue(field, "");
        }
    }

    @Override
    public String serialize() {
        return AbstractLine.serialize(this);
    }

    /**
     * Fills this object, pay attention for unwanted overwrites!
     *
     * @param pCsvData CSV Data
     */
    @Override
    public void load(final String pCsvData) {
        AbstractLine.unserialize(this, pCsvData);
    }

    protected abstract Set<Field> getFieldSet();

    @Override
    public final Set<Field> getFields() {
        return AbstractLine.getFields(getFieldSet());
    }

    @Override
    public Set<String> getFieldNames() {
        return AbstractLine.getFieldNames(getFieldSet());
    }

    @Override
    public Set<Integer> getFieldNumbers() {
        return AbstractLine.getFieldNumbers(getFieldSet());
    }

    @Override
    public Field getFieldByName(final String pFieldName) {
        return AbstractLine.getFieldByName(getFieldSet(), pFieldName);
    }

    @Override
    public Field getFieldByNumber(final Integer pFieldNumber) {
        return AbstractLine.getFieldByNumber(getFieldSet(), pFieldNumber);
    }

    /**
     *
     * @param pField Field
     * @param pValue Value
     * @return old value (if value was already assigned to this field)
     */
    @Override
    public final String setValue(final Field pField, final String pValue) {
        String value = (pValue == null) ? "" : pValue.trim();
        value = value.replace(DELIMITER, DELIMITER_REPLACEMENT);
        value = value.replace("\r\n", LINEBREAK_REPLACEMENT); //Remove line breaks
        value = value.replace("\n\r", LINEBREAK_REPLACEMENT); //Remove line breaks
        value = value.replace("\n", LINEBREAK_REPLACEMENT); //Remove line breaks
        value = value.replace("\r", LINEBREAK_REPLACEMENT); //Remove line breaks
        return values.put(pField, value);
    }

    @Override
    public String setValue(final String pFieldName, final String pValue) {
        return setValue(getFieldByName(getFieldSet(), pFieldName), pValue);
    }

    @Override
    public String setValue(final Integer pFieldNumber, final String pValue) {
        return setValue(getFieldByNumber(getFieldSet(), pFieldNumber), pValue);
    }

    @Override
    @SuppressWarnings("fallthrough")
    public String setValueObj(final Field pField, final Object pValue) {
        if (pValue == null) {
            return setValue(pField, "");
        }
        String value = String.valueOf(pValue);
        Field.Type type = pField.getType();
        Class<?> clazz = pValue.getClass();
        switch (type) {
            case STRING:
                return setValue(pField, value);
            case INT:
                if (clazz == Integer.class) {
                    return setValue(pField, value);
                }
                if (clazz == Short.class) {
                    return setValue(pField, value);
                }
                if (clazz == Byte.class) {
                    return setValue(pField, value);
                }
                if (clazz == String.class) {
                    int val = toInt(value);
                    return setValue(pField, String.valueOf(val));
                }
            case FLOAT:
                if (clazz == BigDecimal.class) {
                    return setValue(pField, floatToStr((BigDecimal) pValue));
                }
                if (clazz == Float.class) {
                    return setValue(pField, floatToStr((Float) pValue));
                }
                if (clazz == Double.class) {
                    return setValue(pField, floatToStr((Double) pValue));
                }
            case MONEY:
                if (clazz == BigDecimal.class) {
                    return setValue(pField, floatToMoney((BigDecimal) pValue));
                }
                if (clazz == Float.class) {
                    return setValue(pField, floatToMoney((Float) pValue));
                }
                if (clazz == Double.class) {
                    return setValue(pField, floatToMoney((Double) pValue));
                }
            case DATETIME:
                if (clazz == java.util.Date.class) {
                    return setValue(pField, Field.formatDateTime((java.util.Date) pValue));
                }
                if (clazz == java.sql.Date.class) {
                    return setValue(pField, Field.formatDateTime((Date) pValue));
                }
            case DATE:
                if (clazz == java.util.Date.class) {
                    return setValue(pField, Field.formatDate((java.util.Date) pValue));
                }
                if (clazz == java.sql.Date.class) {
                    return setValue(pField, Field.formatDate((Date) pValue));
                }
            case TIME:
                if (clazz == java.util.Date.class) {
                    return setValue(pField, Field.formatTime((java.util.Date) pValue));
                }
                if (clazz == java.util.Date.class) {
                    return setValue(pField, Field.formatTime((Date) pValue));
                }
            case BOOLEAN:
                if (clazz == Boolean.class) {
                    boolean val = (Boolean) pValue;
                    return setValue(pField, val ? "1" : "0");
                }
                if (clazz == String.class) {
                    boolean val = toBool(value);
                    return setValue(pField, val ? "1" : "0");
                }
            case LONG:
                if (clazz == Long.class) {
                    Long val = (Long) pValue;
                    return setValue(pField, longToString(val));
                }
        }
        throw new IllegalArgumentException("Value '" + value + "' of class " + pValue.getClass().getName() + " is not processible as " + type.name());
    }

    @Override
    public String setValueObj(final String pFieldName, final Object pValue) {
        return setValueObj(getFieldByName(getFieldSet(), pFieldName), pValue);
    }

    @Override
    public String setValueObj(final Integer pFieldNumber, final Object pValue) {
        return setValueObj(getFieldByNumber(getFieldSet(), pFieldNumber), pValue);
    }

    /*
  @Override
  public String setValue(final Field pField, final Number pValue) {
    String value = (pValue == null)?"":pValue.toString();
    return setValue(pField, value);
  }
     */
    @Override
    public String getValue(final Field pField) {
        return values.get(pField);
    }

    @Override
    public String getValue(final String pFieldName) {
        return values.get(getFieldByName(getFieldSet(), pFieldName));
    }

    @Override
    public String getValue(final Integer pFieldNumber) {
        return values.get(getFieldByNumber(getFieldSet(), pFieldNumber));
    }

    /*
  @Override
  public String setTimeValue(final Field pField, final Date pValue) {
    String value = "";
    if (pValue != null) {
      DateFormat df = new SimpleDateFormat("HH:mm:ss");
      value = df.format(pValue);
    }
    return setValue(pField, value);
  }
     */

 /*
  @Override
  public String setDateValue(Field pField, Date pValue) {
    String value = "";
    if (pValue != null) {
      DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
      value = df.format(pValue);
    }
    return setValue(pField, value);
  }
     */
    @Override
    public String clearValue(final Field pField) {
        return setValue(pField, "");
    }

    @Override
    public String clearValue(final String pFieldName) {
        return clearValue(getFieldByName(getFieldSet(), pFieldName));
    }

    @Override
    public String clearValue(final Integer pFieldNumber) {
        return clearValue(getFieldByNumber(getFieldSet(), pFieldNumber));
    }

    @Override
    public Map<Field, String> getValues() {
        Map<Field, String> copy = new TreeMap<>(values);
        return copy;
    }

    @Override
    public String toString() {
        Iterator<Map.Entry<Field, String>> it = values.entrySet().iterator();
        StringBuilder sb = new StringBuilder();
        String lNewline = System.getProperty("line.separator");
        sb.append(this.getClass().getName() + "@" + Integer.toHexString(this.hashCode()));
        int i = 0;
        while (it.hasNext()) {
            i++;
            Map.Entry<Field, String> entry = it.next();
            Field field = entry.getKey();
            String value = entry.getValue();
            int valueLength = value.length();
            int maxLength = field.getLength();
            boolean invalidLength = (maxLength > 0 && valueLength > maxLength);
            String strMsg = "";
            if (maxLength <= 0) {
                strMsg = "";
            } else {
                /*
        if (invalidLength) {
          strMsg = " > " + maxLength + "!!";
        } else {
          strMsg = " <= " + maxLength + " OK";
        }
                 */
                if (invalidLength) {
                    strMsg = " !!";
                } else {
                    //strMsg = " <= " + maxLength + " OK";
                }
            }
            sb.append(lNewline)
                    .append(i)
                    .append(". ")
                    .append(field.getName())
                    .append(": ")
                    .append(value)
                    .append(" [Size ")
                    .append(valueLength)
                    .append(maxLength <= 0 ? "" : "/" + maxLength)
                    .append(strMsg)
                    .append("]");
        }
        return sb.toString();
    }

    @Override
    public void setNr(final long pNr) {
        setValue(NR, longToString(pNr));
    }

    @Override
    public String getNr() {
        return getValue(NR);
    }

    @Override
    public void setTpSource(final String pTpSource) {
        setValue(TP_SOURCE, pTpSource);
    }

    @Override
    public String getTpSource() {
        return getValue(TP_SOURCE);
    }

    @Override
    public void setTpId(final String pTpId) {
        setValue(TP_ID, pTpId);
    }

    @Override
    public String getTpId() {
        return getValue(TP_ID);
    }

    @Override
    public String getHeadline() {
        return headline(getFieldSet());
    }

    /*
  @Override
  public String getHash() throws UnsupportedEncodingException, NoSuchAlgorithmException {
    return getLineHash(this);
  }

  public static String getLineHash(final LineI pLine) throws UnsupportedEncodingException, NoSuchAlgorithmException {
    if (pLine == null) {
      throw new CpxIllegalArgumentException("Line is null!");
    }
    return getLineHash(pLine.serialize());
  }
  
  public static String getLineHash(final String pLine) throws UnsupportedEncodingException, NoSuchAlgorithmException {
    String line = (pLine == null)?"":pLine.trim();
    if (line.isEmpty()) {
      throw new CpxIllegalArgumentException("No line given");
    }
    byte[] bytesOfMessage = pLine.getBytes("UTF-8");

    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] thedigest = md.digest(bytesOfMessage);
    return (new HexBinaryAdapter()).marshal(thedigest).toLowerCase();
  }
  
  @Override
  public boolean hasValues() {
    String line = this.serialize();
    line = line.replace(AbstractLine.DELIMITER, "").trim();
    return !line.isEmpty();
  }
     */
    @Override
    public void setIkz(final String pIkz) {
        setValue(IKZ, pIkz);
    }

    @Override
    public String getIkz() {
        return getValue(IKZ);
    }

    @Override
    public void setFallNr(final String pFallNr) {
        setFallNr(pFallNr, CaseNumberFormatEn.UNTOUCH);
    }

    @Override
    public void setFallNr(final String pFallNr, final CaseNumberFormatEn pCaseNumberFormatEn) {
        String fallNr = pFallNr;
        if (fallNr != null && pCaseNumberFormatEn != null) {
            switch (pCaseNumberFormatEn) {
                case FILL_WITH_LEADING_ZEROS:
                    fallNr = leftPad(fallNr, 10, '0');
                    break;
                case REMOVE_TRAILING_ZEROS:
                    fallNr = removeTrailingZeros(fallNr);
                    break;
                case UNTOUCH:
                default:
                    //do absolutely nothing!
                    break;
            }
        }
        setValue(FALLNR, fallNr);
    }

    @Override
    public String getFallNr() {
        return getValue(FALLNR);
    }

    @Override
    public void setPatNr(final String pPatNr) {
        setValue(PATNR, pPatNr);
    }

    @Override
    public String getPatNr() {
        return getValue(PATNR);
    }

    /*
  @Override
  public boolean isLineWritten() {
    return mLineWritten.get();
  }

  @Override
  public void lineWritten() {
    mLineWritten.set(true);
  }
     */
    @Override
    public void check() throws IllegalStateException {
        Iterator<Map.Entry<Field, String>> it = getValues().entrySet().iterator();
        StringBuilder sb = new StringBuilder();
        while (it.hasNext()) {
            Map.Entry<Field, String> entry = it.next();
            Field field = entry.getKey();
            String value = entry.getValue();
            int lengthOverflow = field.getLengthOverflow(value);
            if (lengthOverflow > 0) {
                if (sb.length() > 0) {
                    sb.append("\r\n");
                }
                sb.append(field.getName() + ": Der Wert '" + value + "' überschreitet die zulässige maximale Länge von " + field.getLength() + " um " + lengthOverflow + " Zeichen");
            }
        }
        if (sb.length() > 0) {
            throw new IllegalStateException(sb.toString());
        }
    }

}
