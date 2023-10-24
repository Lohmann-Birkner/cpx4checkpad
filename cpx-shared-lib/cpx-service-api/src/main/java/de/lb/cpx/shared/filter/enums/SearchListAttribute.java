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
package de.lb.cpx.shared.filter.enums;

import de.lb.cpx.model.lang.CpxLanguageInterface;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Transient;

/**
 *
 * @author Dirk Niemeier
 */
public class SearchListAttribute implements Serializable {
    
    public static enum ACTIONS_ALLOWED{DO_ALL, DO_NOTHING, DONT_SORT, DONT_SEARCH};

    private static final long serialVersionUID = 1L;
    @Transient
    private static CpxLanguageInterface cpxLanguage = null;

    public final String key;
    protected String database_table;
    public final String database_field;
    protected String unique_database_field;
    protected String qualified_database_field;
    public final String language_key;
    //private T value = null;
    //private final String tableName; 
    private int size;
    private int number;
    //private Serializable dataType;
    private boolean is_csv_data = false;
    private boolean long_content = false;
    private String align = "";
    private boolean visible = true;
    private SearchListFormat<? extends Serializable> format = null;
    private boolean disabled = false;
    private boolean no_filter = false;
    //AWi 20170530 CPX-529: added to show that attribut has no column in the ui and should be ignored if flag is true
    public boolean no_column = false;
    //Abo: will not be used for server site sorting and filtering
    private boolean isClientSide = false;
    private Serializable searchControlDataTyp;
    private boolean sortable = true;
    private boolean is4DecimalDigits = false;

    private OPERATOR operator = null;
    protected final ArrayList<SearchListAttributeList> deepChildren = new ArrayList<>();
    protected final ArrayList<SearchListAttribute> flatChildren = new ArrayList<>();
    public SearchListAttribute parent = null;
    //private LinkedList<SearchListAttribute> siblings = new LinkedList<>();
    //private CHILD_REL_TYPE childRelType = null;
    //private SearchListAttributeValue<?, ?> defaultValue;
    private boolean isRoot = true;
    private boolean isEqual = false;
    private boolean isFrom = false;
    private boolean isTo = false;
    private boolean isOpen = false;
    private boolean isExpires = false;
    private boolean isToday = false;

    private boolean isHospital = false;
    private boolean isInsurance = false;
    private boolean isDepartment = false;
    private boolean isDepartmentName = false;
    private ACTIONS_ALLOWED actionAllowed = ACTIONS_ALLOWED.DO_ALL;
//    private boolean isMdkAuditReason = false;

    /*
  public Attribute setValue(final T pValue) {
  value = pValue;
  return this;
  }
     */
    /**
     * creates a new search list attribute to match and link between client and
     * server field in search lists
     *
     * @param pKey unique key or name of attribute
     * @param pDatabaseTable database table
     * @param pDatabaseField database field
     * @param pLanguageKey language key for translation
     */
    public SearchListAttribute(final String pKey, final String pDatabaseTable, final String pDatabaseField, final String pLanguageKey) {
        //name = (pName==null)?new String[] { "" }:pName;
        key = pKey;
        //name = (pName==null)?"":pName.trim();
        language_key = (pLanguageKey == null) ? "" : pLanguageKey.trim();
        //database_table = (pDatabaseTable == null) ? "" : pDatabaseTable.trim().toUpperCase();
        database_field = (pDatabaseField == null) ? "" : pDatabaseField.trim().toUpperCase();
        setDatabaseTable(pDatabaseTable);
        unique_database_field = makeUniqueDatabaseField(database_table, database_field);
        //qualified_database_field = makeQualifiedDatabaseField(database_table, database_field);
        //unique_database_field = makeUniqueDatabaseField(database_table, database_field);
        /*
    setName(pName);
    setLanguageKey(pLanguageKey);
         */
        //setDataType(name.getClass());
    }

    public static void setCpxLanguage(final CpxLanguageInterface pCpxLanguage) {
        cpxLanguage = pCpxLanguage;
    }

    public static CpxLanguageInterface getCpxLanguage() {
        return cpxLanguage;
    }

    protected static String makeQualifiedDatabaseField(final String pTable, final String pField) {
        //String table = getDatabaseTable();
        //String field = getDatabaseField();
        if (pTable.isEmpty() && pField.isEmpty()) {
            return "-1";
        }
        if (pTable.isEmpty()) {
            //return "-1 " + field;
            return pField;
        }
        if (pTable.startsWith("$")) {
            return pTable;
        }
        return pTable + "." + pField;
    }

    protected static String makeDatabaseTableHashcode(final String pTable, final String pField) {
        //String table = getDatabaseTable();
        if (pTable.isEmpty()) {
            //return "-1 " + field;
            return "";
        }
        int hashCode = (pTable + pField).hashCode();
        if (hashCode < 0) {
            hashCode = hashCode * -1;
        }
        hashCode = (hashCode % 10000);
        return String.valueOf(hashCode);
    }

    protected static String makeUniqueDatabaseField(final String pTable, final String pField) {
        //String table = getDatabaseTable();
        //String field = getDatabaseField();
        if (pTable.isEmpty()) {
            //return "-1 " + field;
            return pField;
        }
        //return field + "_" + table.hashCode();
        String field = pField;
        String hashCode = makeDatabaseTableHashcode(pTable, pField);
        String delimiter = "_";
        int length = field.length() + delimiter.length() + hashCode.length();
        int oversize = length - 30;
        if (oversize > 0) {
            field = field.substring(0, field.length() - oversize);
        }
        return field + "_" + hashCode;
    }

    /**
     * database table (relevant for server to build query, e.g. T_CASE)
     *
     * @param pDatabaseTable database table
     * @return this
     */
    public final SearchListAttribute setDatabaseTable(final String pDatabaseTable) {
        database_table = (pDatabaseTable == null) ? "" : pDatabaseTable.trim().toUpperCase();
        qualified_database_field = makeQualifiedDatabaseField(database_table, database_field);
        return this;
    }

    /**
     * add children
     *
     * @param pChild child
     * @return attribute
     */
    public SearchListAttribute addChild(final SearchListAttribute pChild) {
        return addEqualChild(pChild);
    }

//    public SearchListAttribute addSingleChild(final String pKey) {
//        return addSingleChild(add(pKey));
//    }
//    public SearchListAttribute addBetweenChildren(final String pKeyFrom, final String pKeyTo) {
//        return addBetweenChildren(add(pKeyFrom), add(pKeyTo));
//    }
    protected void fillFromParent(final SearchListAttribute pChild) {
        //SearchListAttribute attribute = new SearchListAttribute(pKey, this.database_table, this.database_field, this.language_key);
        if (pChild.getParent() != null && pChild.getParent() != this) {
            throw new IllegalStateException("invalid definition, attribute points to another parent: " + pChild.key + "/" + String.valueOf(pChild));
        }
        pChild.setParent(this);
        if (pChild.format == null) {
            pChild.setFormat(this.format);
        }
        if (pChild.align == null || pChild.align.trim().isEmpty()) {
            pChild.setAlign(this.align);
        }
        pChild.setNoColumn(this.no_column);
        pChild.setVisible(this.visible);
        if (pChild.size == 0) {
            pChild.setSize(this.size);
        }
        //attribute.setNumber(this.number);
        pChild.setIsClientSide(this.isClientSide);
        pChild.setDisabled(this.disabled);
    }

    /**
     * add child where database field has to be equal filter value
     *
     * @param pChild child
     * @return this
     */
    public SearchListAttribute addEqualChild(final SearchListAttribute pChild) {
        if (pChild != null) {
            //CHILD_REL_TYPE relationType = CHILD_REL_TYPE.SINGLE;
            //children.add(pChild);
            //pChild.setChildType(childType);
            pChild.setEqual(true);
            fillFromParent(pChild);
            SearchListAttributeList childList = new SearchListAttributeList(CHILD_REL_TYPE.EQUAL, pChild);
            pChild.setOperator(OPERATOR.EQUAL);
            deepChildren.add(childList);
            //childList.add(pChild);
            //deepChildren.put(relationType, childList);
            flatChildren.add(pChild);
            //pChild.setParent(this);
        }
        return this;
    }

    /**
     * add child where database field has to be equal filter value
     *
     * @param pChild child
     * @return this
     */
    public SearchListAttribute addTodayChild(final SearchListAttribute pChild) {
        if (pChild != null) {
            //CHILD_REL_TYPE relationType = CHILD_REL_TYPE.SINGLE;
            //children.add(pChild);
            //pChild.setChildType(childType);
            pChild.setToday(true);
            fillFromParent(pChild);
            SearchListAttributeList childList = new SearchListAttributeList(CHILD_REL_TYPE.TODAY, pChild);
            pChild.setOperator(OPERATOR.EQUAL);
            deepChildren.add(childList);
            //childList.add(pChild);
            //deepChildren.put(relationType, childList);
            flatChildren.add(pChild);
            //pChild.setParent(this);
        }
        return this;
    }

    /**
     * add 2 children that are associated in a between query
     *
     * @param pChildFrom from
     * @param pChildTo to
     * @return attribute
     */
    public SearchListAttribute addBetweenChildren(final SearchListAttribute pChildFrom,
            final SearchListAttribute pChildTo) {
        if (pChildFrom == null || pChildTo == null) {
            throw new IllegalArgumentException("between needs exactly to 2 children and they cannot be null!");
        }
        pChildFrom.setFrom(true);
        pChildTo.setTo(true);

        //CHILD_REL_TYPE relationType = CHILD_REL_TYPE.BETWEEN;
        //LinkedList<SearchListAttribute> childList = new LinkedList<>();
        fillFromParent(pChildFrom);
        fillFromParent(pChildTo);

        SearchListAttributeList childList = new SearchListAttributeList(CHILD_REL_TYPE.BETWEEN, pChildFrom, pChildTo);

        //pChildFrom.setChildRelType(relationType);
        //pChildTo.setChildRelType(relationType);
        pChildFrom.setOperator(OPERATOR.GREATER_THAN_OR_EQUAL_TO);
        if (Date.class == pChildTo.getDataType() || pChildTo.getDataType() instanceof Date /* || SearchListFormatDeadLineDateTime.class == pChildTo.getDateType()
                || SearchListFormatDeadLineRadioButton.class == pChildTo.getDateType() */) {
            pChildTo.setOperator(OPERATOR.LESS_THAN);
        } else {
            pChildTo.setOperator(OPERATOR.LESS_THAN_OR_EQUAL_TO);
        }

        //childList.add(pChildFrom);
        //childList.add(pChildTo);
        //deepChildren.put(relationType, childList);
        deepChildren.add(childList);
        flatChildren.add(pChildFrom);
        flatChildren.add(pChildTo);
        return this;
    }

    /**
     * add child that is automatically set to today (only possible for date
     * field). Is to be used to find all open entries (not older than today).
     *
     * @param pChild child
     * @return this
     */
    public SearchListAttribute addOpenChild(final SearchListAttribute pChild) {
        if (pChild != null) {
            //CHILD_REL_TYPE relationType = CHILD_REL_TYPE.SINGLE;
            //children.add(pChild);
            //pChild.setChildType(childType);
            fillFromParent(pChild);

            if (!(Date.class == pChild.getDataType() || pChild.getDataType() instanceof Date /* || SearchListFormatDeadLineDateTime.class == pChild.getDateType()
                    || SearchListFormatDeadLineRadioButton.class == pChild.getDateType()) */)) {
                throw new IllegalArgumentException("open (future) child can only be added if datatype is of type date! -> " + pChild.key + "/" + String.valueOf(pChild));
            }

            SearchListAttributeList childList = new SearchListAttributeList(CHILD_REL_TYPE.OPEN, pChild);
            pChild.setOperator(OPERATOR.GREATER_THAN_OR_EQUAL_TO);
            pChild.setOpen(true);
//            pChild.setDefaultValue((param) -> {
//                Calendar cal = new GregorianCalendar();
//                cal.setTime(new Date());
//                cal.set(Calendar.HOUR_OF_DAY, 0);
//                cal.set(Calendar.MINUTE, 0);
//                cal.set(Calendar.SECOND, 0);
//                cal.set(Calendar.MILLISECOND, 0);
//                //cal.add(Calendar.DAY_OF_MONTH, 1);
//                return cal.getTime();
//            });
            deepChildren.add(childList);
            //childList.add(pChild);
            //deepChildren.put(relationType, childList);
            flatChildren.add(pChild);
            //pChild.setParent(this);
        }
        return this;
    }

    /**
     * add child that is automatically set to today (only possible for date
     * field). Is to be used to find all expired entries (older than today).
     *
     * @param pChild child
     * @return this
     */
    public SearchListAttribute addExpChild(final SearchListAttribute pChild) {
        if (pChild != null) {
            //CHILD_REL_TYPE relationType = CHILD_REL_TYPE.SINGLE;
            //children.add(pChild);
            //pChild.setChildType(childType);
            fillFromParent(pChild);

            if (!(Date.class == pChild.getDataType() || pChild.getDataType() instanceof Date /* || SearchListFormatDeadLineDateTime.class == pChild.getDateType()
                    || SearchListFormatDeadLineRadioButton.class == pChild.getDateType()) */)) {
                throw new IllegalArgumentException("expired (past) child can only be added if datatype is of type date! -> " + pChild.key + "/" + String.valueOf(pChild));
            }

            SearchListAttributeList childList = new SearchListAttributeList(CHILD_REL_TYPE.EXPIRED, pChild);
            pChild.setOperator(OPERATOR.LESS_THAN);
            pChild.setExpires(true);
//            pChild.setDefaultValue((param) -> {
//                Calendar cal = new GregorianCalendar();
//                cal.setTime(new Date());
//                cal.set(Calendar.HOUR_OF_DAY, 0);
//                cal.set(Calendar.MINUTE, 0);
//                cal.set(Calendar.SECOND, 0);
//                cal.set(Calendar.MILLISECOND, 0);
//                cal.add(Calendar.DAY_OF_MONTH, 1);
//                return cal.getTime();
//            });
            deepChildren.add(childList);
            //childList.add(pChild);
            //deepChildren.put(relationType, childList);
            flatChildren.add(pChild);
            //pChild.setParent(this);
        }
        return this;
    }

    public List<SearchListAttributeList> getDeepChildren() {
        return deepChildren;
    }

    public SearchListAttributeList getDeepChildren(CHILD_REL_TYPE pChildRelType) {
        for (SearchListAttributeList list : deepChildren) {
            if (list.getChildRelType().equals(pChildRelType)) {
                return list;
            }
        }
        return null;
    }

    public SearchListAttribute getFromChild() {
        SearchListAttributeList list = getDeepChildren(CHILD_REL_TYPE.BETWEEN);
        if (list == null) {
            return null;
        }
        for (SearchListAttribute attribute : list) {
            if (attribute.isFrom()) {
                return attribute;
            }
        }
        return null;
    }

    public SearchListAttribute getToChild() {
        SearchListAttributeList list = getDeepChildren(CHILD_REL_TYPE.BETWEEN);
        if (list == null) {
            return null;
        }
        for (SearchListAttribute attribute : list) {
            if (attribute.isTo()) {
                return attribute;
            }
        }
        return null;
    }

    public SearchListAttribute getEqualChild() {
        SearchListAttributeList list = getDeepChildren(CHILD_REL_TYPE.EQUAL);
        if (list == null) {
            return null;
        }
        for (SearchListAttribute attribute : list) {
            if (attribute.isEqual()) {
                return attribute;
            }
        }
        return null;
    }

    public SearchListAttribute getOpenChild() {
        SearchListAttributeList list = getDeepChildren(CHILD_REL_TYPE.OPEN);
        if (list == null) {
            return null;
        }
        for (SearchListAttribute attribute : list) {
            if (attribute.isOpen()) {
                return attribute;
            }
        }
        return null;
    }

    public SearchListAttribute getExpiredChild() {
        SearchListAttributeList list = getDeepChildren(CHILD_REL_TYPE.EXPIRED);
        if (list == null) {
            return null;
        }
        for (SearchListAttribute attribute : list) {
            if (attribute.isExpires()) {
                return attribute;
            }
        }
        return null;
    }

    /**
     * get (flat) list of children attributes
     *
     * @return children
     */
    public List<SearchListAttribute> getChildren() {
        return getFlatChildren();
    }

    /**
     * get list of children attributes
     *
     * @return children
     */
    public List<SearchListAttribute> getFlatChildren() {
        return new LinkedList<>(flatChildren);
    }

    /**
     * has children?
     *
     * @return has children?
     */
    public boolean hasChildren() {
        return !deepChildren.isEmpty();
    }

    protected SearchListAttribute setParent(final SearchListAttribute pParent) {
        parent = pParent;
        setRoot(parent == null);
        return this;
    }

    public SearchListAttribute getParent() {
        return parent;
    }


    /*
    protected SearchListAttribute setName(final String pName) {
      name = (pName==null)?"":pName.trim();
      return this;
    }
     */

 /*
    protected SearchListAttribute setLanguageKey(final String pLanguageKey) {
      language_key = (pLanguageKey==null)?"":pLanguageKey.trim();
      return this;
    }
     */
    public SearchListAttribute setSize(final int pSize) {
        size = pSize;
        return this;
    }

    public SearchListAttribute setNumber(final int pNumber) {
        number = pNumber;
        return this;
    }

    public SearchListAttribute setCsvData(final boolean pIsCsvData) {
        is_csv_data = pIsCsvData;
        return this;
    }

    public SearchListAttribute setLongContent(final boolean pIsLongContent) {
        long_content = pIsLongContent;
        return this;
    }

    /**
     * this information can be used in client to align the text or graphic
     *
     * @param pAlign alignment
     * @return this
     */
    public SearchListAttribute setAlign(final String pAlign) {
        align = pAlign;
        return this;
    }

    /**
     * is this attribute a visible column?
     *
     * @param pVisible visible?
     * @return this
     */
    public SearchListAttribute setVisible(final boolean pVisible) {
        visible = pVisible;
        return this;
    }

    /**
     * is there a control rendered for filtering?
     *
     * @param pNoFilter no filter
     * @return this
     */
    public SearchListAttribute setNoFilter(final boolean pNoFilter) {
        no_filter = pNoFilter;
        return this;
    }

    /**
     * details regarding format of this attribute or filter control (string if
     * nothing was setted)
     *
     * @param pFormat format
     * @return this
     */
    public SearchListAttribute setFormat(final SearchListFormat<? extends Serializable> pFormat) {
        format = pFormat;
        return this;
    }

    /**
     * operator is relevant for server to get information how to query this
     * filter value (=, &gt;=, &lt;...)
     *
     * @param pOperator operator
     * @return this
     */
    protected SearchListAttribute setOperator(final OPERATOR pOperator) {
        operator = pOperator;
        return this;
    }

//    public SearchListAttribute addSibling(final SearchListAttribute pSibling) {
//        siblings.add(pSibling);
//        return this;
//    }
//
//    public List<SearchListAttribute> getSiblings() {
//        return new LinkedList<>(siblings);
//    }
//
//    public boolean hasSiblings() {
//        return !siblings.isEmpty();
//    }
    /**
     * basic data type is relevant to the client to render the filter control
     * (different contrains can fire, e.g. you are not allowed to input
     * characters in a numeric field)
     *
     * @return serializable data type
     */
    public Serializable getDataType() {
        return getFormat().getDataType();
    }

    /**
     * is this column disabled?
     *
     * @param pDisabled disabled
     * @return this
     */
    public SearchListAttribute setDisabled(final boolean pDisabled) {
        disabled = pDisabled;
        return this;
    }

    /*
    public Serializable getDataType() {
      if (dataType == null) {
        return String.class;
      }
      return dataType;
    }
     */

 /*
    public T getValue() {
      return value;
    }
     */
    /**
     * unique key for this attribute (sometimes called attribute name instead of
     * attribute key)
     *
     * @return name/key
     */
    public String getKey() {
        return key;
    }

    /**
     * key for translation
     *
     * @return language key
     */
    public String getLanguageKey() {
        return language_key;
    }

    /**
     * size is an information to the client which initial width should be used
     * to display column in a table view
     *
     * @return int
     */
    public int getSize() {
        return size;
    }

    /**
     * number is an information to the client in which order the columns should
     * be shown in a table view
     *
     * @return int
     */
    public int getNumber() {
        return number;
    }

    /**
     * can this field contain csv data?
     *
     * @return bool
     */
    public boolean isCsvData() {
        return is_csv_data;
    }

    /**
     * can this field contain very long values (strings like CLOB in Oracle or
     * Text in MSSQL?)
     *
     * @return bool
     */
    public boolean isLongContent() {
        return long_content;
    }

    /**
     * this information can be used in client to align the text or graphic
     *
     * @return alignment
     */
    public String getAlign() {
        if (align == null || align.trim().isEmpty()) {
            //Awi-20180412-CPX-901:
            //Change default alignment from center to center_left
            return "CENTER_LEFT";
        }
        return align;
    }

    /**
     * is this attribute a visible column?
     *
     * @return bool
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * is there a control rendered for filtering?
     *
     * @return bool
     */
    public boolean isNoFilter() {
        return no_filter;
    }

    /**
     * details regarding format of this attribute or filter control (string if
     * nothing was setted)
     *
     * @return format
     */
    public SearchListFormat<? extends Serializable> getFormat() {
        if (format == null) {
            return new SearchListFormatString();
        }
        return format;
    }

    /**
     * is this column disabled?
     *
     * @return bool
     */
    public boolean isDisabled() {
        return disabled;
    }

    /**
     * operator is relevant for server to get information how to query this
     * filter value (=, &gt;=, &lt;...)
     *
     * @return operator
     */
    public OPERATOR getOperator() {
        if (operator == null) {
            if (isString()) {
                return OPERATOR.LIKE;
            } else {
                return OPERATOR.EQUAL;
            }
        }
        return operator;
    }

    @Override
    public String toString() {
        CpxLanguageInterface lang = getCpxLanguage();
        String langKey = getLanguageKey();
        if (lang != null) {
            return lang.get(langKey);
        }
        return langKey;
    }

    /**
     * is this attribute name (key) equals to another key?
     *
     * @param otherName other attribute name/key
     * @return bool
     */
    public boolean equalsName(String otherName) {
        return (otherName == null) ? false : key.equals(otherName);
    }

    /**
     * database table (relevant for server to build query, e.g. T_CASE)
     *
     * @return table in database
     */
    public String getDatabaseTable() {
        return database_table;
    }

    /**
     * database field (relevant for server to build query, e.g. CS_CASE_NUMBER)
     *
     * @return field or column in database
     */
    public String getDatabaseField() {
        return database_field;
    }

    /**
     * database table AND column separated by a dot (e.g. T_CASE.CS_CASE_NUMBER)
     *
     * @return full qualified field name
     */
    public String getQualifiedDatabaseField() {
        return qualified_database_field;
    }

    /**
     * unique combination of database table and field with a number (ensures
     * that fields are unique in the same scope).
     *
     * @return unique field name
     */
    public String getUniqueDatabaseField() {
        return unique_database_field;
    }

    /**
     * is not a single value column (can contain multiple values, e.g. fees,
     * procedures, diagnosis, departments).
     *
     * @return bool
     */
    public boolean isNoColumn() {
        return no_column;
    }

    /**
     * is not a single value column (can contain multiple values, e.g. fees,
     * procedures, diagnosis, departments)?
     *
     * @param pNoColumn no column
     * @return attribute
     */
    public SearchListAttribute setNoColumn(boolean pNoColumn) {
        this.no_column = pNoColumn;
        return this;
    }

    /**
     * ordering and filtering is handled on client and not on server
     *
     * @deprecated don't use this anymore
     * @return the isClientSide
     */
    @Deprecated(since = "1.05")
    public boolean isClientSide() {
        return isClientSide;
    }

    /**
     * ordering and filtering is handled on client and not on server?
     *
     * @param isClientSide the isClientSide to set
     * @deprecated don't use this anymore
     * @return this
     */
    @Deprecated(since = "1.05")
    public SearchListAttribute setIsClientSide(boolean isClientSide) {
        this.isClientSide = isClientSide;
        return this;
    }

    /**
     * @return the searchControlDataTyp
     * @deprecated don't use this anymore
     */
    @Deprecated(since = "1.05")
    public Serializable getSearchControlDataTyp() {
        return searchControlDataTyp;
    }

    /**
     * @param searchControlDataTyp the searchControlDataTyp to set
     * @deprecated don't use this anymore
     * @return this
     */
    @Deprecated(since = "1.05")
    public SearchListAttribute setSearchControlDataTyp(Serializable searchControlDataTyp) {
        this.searchControlDataTyp = searchControlDataTyp;
        return this;
    }

    /**
     * has parent?
     *
     * @return the isRoot
     */
    public boolean isRoot() {
        return isRoot;
    }

    /**
     * is parent unequals to null?
     *
     * @param pIsRoot the isRoot to set
     */
    private SearchListAttribute setRoot(boolean pIsRoot) {
        this.isRoot = pIsRoot;
        return this;
    }

    /**
     * is a from field in a between query
     *
     * @return the isFrom
     */
    public boolean isFrom() {
        return isFrom;
    }

    /**
     * is a from field in a between query?
     *
     * @param pIsFrom the isFrom to set
     * @return this
     */
    private SearchListAttribute setFrom(boolean pIsFrom) {
        this.isFrom = pIsFrom;
        return this;
    }

    /**
     * is an equals field in a nested searchlist?
     *
     * @param pIsEqual the isEqual to set
     * @return this
     */
    private SearchListAttribute setEqual(boolean pIsEqual) {
        this.isEqual = pIsEqual;
        return this;
    }

    /**
     * is an equals field in a nested searchlist?
     *
     * @return the isEqual
     */
    public boolean isEqual() {
        return isEqual;
    }

    /**
     * is an equals field in a query?
     *
     * @return the isTo
     */
    public boolean isTo() {
        return isTo;
    }

    /**
     * @param pIsTo the isTo to set
     * @return this
     */
    private SearchListAttribute setTo(boolean pIsTo) {
        this.isTo = pIsTo;
        return this;
    }

    /**
     * is an open field (date greater than or equals today) in a nested
     * searchlist?
     *
     * @return the isOpen
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * is an open field (date greater than or equals today) in a nested
     * searchlist?
     *
     * @param pIsOpen the isOpen to set
     * @return this
     */
    private SearchListAttribute setOpen(boolean pIsOpen) {
        this.isOpen = pIsOpen;
        return this;
    }

    /**
     * is an expire field (date lower than today) in a nested searchlist?
     *
     * @return the isExpires
     */
    public boolean isExpires() {
        return isExpires;
    }

    /**
     * is an expire field (date lower than today) in a nested searchlist?
     *
     * @param pIsExpires the isExpires to set
     * @return this
     */
    private SearchListAttribute setExpires(boolean pIsExpires) {
        this.isExpires = pIsExpires;
        return this;
    }

    /**
     * is a today field (date greater than or equals today) in a nested
     * searchlist?
     *
     * @return the isToday
     */
    public boolean isToday() {
        return isToday;
    }

    /**
     * is a today field (date greater than or equals today) in a nested
     * searchlist?
     *
     * @param pIsToday the isOpen to set
     * @return this
     */
    private SearchListAttribute setToday(boolean pIsToday) {
        this.isToday = pIsToday;
        return this;
    }

    /**
     * is it a date/time field?
     *
     * @return bool
     */
    public boolean isDate() {
        return getFormat().isDate();
    }

    /**
     * is it a double (or float) field?
     *
     * @return bool
     */
    public boolean isDouble() {
        return getFormat().isDouble();
    }
    public boolean is4DecimalDigits() {
        return is4DecimalDigits;
    }
    

    /**
     * is it a boolean field?
     *
     * @return bool
     */
    public boolean isBoolean() {
        return getFormat().isBoolean();
    }

    /**
     * is it a string/text field?
     *
     * @return bool
     */
    public boolean isString() {
        return getFormat().isString();
    }

    /**
     * do you have to expect very long text for this attribute/column?
     *
     * @return bool
     */
    public boolean isLongString() {
        return long_content && isString();
    }

    /**
     * is it an integer (or long, short, byte...) field?
     *
     * @return bool
     */
    public boolean isInteger() {
        return getFormat().isInteger();
    }

    /**
     * is it a number field (can have floating points or not, so it can be
     * integer or double as well)?
     *
     * @return bool
     */
    public boolean isNumber() {
        return getFormat().isNumber();
    }

    /**
     * is it a enumeration (CpxEnumInterface) field?
     *
     * @return bool
     */
    public boolean isEnum() {
        return getFormat().isEnum();
    }

    /**
     * is it a map field?
     *
     * @return bool
     */
    public boolean isMap() {
        return getFormat().isMap();
    }

    /**
     * is it a user map field?
     *
     * @return bool
     */
    public boolean isUserMap() {
        return getFormat().isUserMap();
    }

    /**
     * is it a reminder subject map field?
     *
     * @return bool
     */
    public boolean isReminderSubjectMap() {
        return getFormat().isReminderSubjectMap();
    }

    /**
     * is it a process topic map field?
     *
     * @return bool
     */
    public boolean isProcessTopicMap() {
        return getFormat().isProcessTopicMap();
    }

    /**
     * is it a mdk audit reasons field?
     *
     * @return bool
     */
    public boolean isMdkAuditReasonsMap() {
        return getFormat().isMdkAuditReasonsMap();
    }

    /**
     * is it a action Subject
     *
     * @return bool
     */
    public boolean isActionSubjectMap() {
        return getFormat().isActionSubjectMap();
    }

    public boolean isMdkStatesMap() {
        return getFormat().isMdkStatesMap();
    }

//    public boolean isDepartmentMap() {
//        return getFormat().isDepartmentMap();
//    }
    /**
     * is this field a hospital identifier?
     *
     * @return the isHospital
     */
    public boolean isHospital() {
        return isHospital;
    }

    /**
     * is this field a hospital identifier?
     *
     * @param pIsHospital the isHospital to set
     * @return this
     */
    public SearchListAttribute setHospital(boolean pIsHospital) {
        this.isHospital = pIsHospital;
        return this;
    }

    /**
     * is this field an insurance identifier?
     *
     * @return the isInsurance
     */
    public boolean isInsurance() {
        return isInsurance;
    }

    /**
     * is this field an insurance identifier?
     *
     * @param pIsInsurance the isInsurance to set
     * @return this
     */
    public SearchListAttribute setInsurance(boolean pIsInsurance) {
        this.isInsurance = pIsInsurance;
        return this;
    }

    public SearchListAttribute setIs4DecimalDigits(boolean pFlag){
        is4DecimalDigits = pFlag;
        return this;
    }

    /**
     * is this field a department identifier?
     *
     * @return the isDepartment
     */
    public boolean isDepartment() {
        return isDepartment;
    }

    public boolean isDepartmentName() {
        return isDepartmentName;
    }

    /**
     * is this field a department identifier?
     *
     * @param pIsDepartment the isDepartment to set
     * @return this
     */
    public SearchListAttribute setDepartment(boolean pIsDepartment) {
        this.isDepartment = pIsDepartment;
        return this;
    }

    public SearchListAttribute setDepartmentName(boolean pIsDepartmentName) {
        this.isDepartmentName = pIsDepartmentName;
        return this;
    }

    /**
     * @return the sortable
     */
    public boolean isSortable() {
        return sortable;
    }

    /**
     * @param sortable the sortable to set
     * @return this
     */
    public SearchListAttribute setSortable(boolean sortable) {
        this.sortable = sortable;
        if(!sortable){
            actionAllowed = ACTIONS_ALLOWED.DONT_SORT;
        }
        return this;
    }

    public ACTIONS_ALLOWED getActionAllowed() {
        return actionAllowed;
    }

    public SearchListAttribute setActionAllowed(ACTIONS_ALLOWED actionAllowed) {
        this.actionAllowed = actionAllowed;
        return this;
    }

    /**
     * operator to be used in sql query
     */
    public enum OPERATOR {
        LIKE(""),
        EQUAL(""),
        GREATER_THAN_OR_EQUAL_TO(">="),
        LESS_THAN("<="), //yes, technically an '<' is applied in SQL, but for the customer it seems to be '<='
        LESS_THAN_OR_EQUAL_TO("<="),
        LIKE_BOTH_SIDES("");

        private final String symbol;

        OPERATOR(final String pSymbol) {
            symbol = pSymbol;
        }

        public String getSymbol() {
            return symbol;
        }

    }

    /**
     * relation type between nested search list
     */
    public enum CHILD_REL_TYPE {
        BETWEEN, EQUAL, OPEN, EXPIRED, TODAY;
    }

    public Translation getTranslation() {
        return Lang.get(getLanguageKey());
    }

}
