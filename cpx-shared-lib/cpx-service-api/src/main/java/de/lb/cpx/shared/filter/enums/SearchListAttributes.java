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
package de.lb.cpx.shared.filter.enums;

import de.lb.cpx.shared.lang.Lang;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.Transient;

/**
 *
 * @author Dirk Niemeier
 */
public abstract class SearchListAttributes {

//    public static final String depDischarge = "depDischarge";
//    public static final String depDischarge301 = "depDischarge301";
    public static final String patDateOfBirth = "patDateOfBirth";
    public static final String patDateOfBirthEqual = "patDateOfBirthEqual";
    public static final String patDateOfBirthFrom = "patDateOfBirthFrom";
    public static final String patDateOfBirthTo = "patDateOfBirthTo";
    public static final String patFirstName = "patFirstName";
    public static final String patSecondName = "patSecondName";
    public static final String patName = "patName";
    public static final String insNumber = "insNumber";
    public static final String lock = "lock";
    public static final String rowNum = "rowNum";
    public static final String rowNumEqual = "rowNumEqual";
    public static final String rowNumFrom = "rowNumFrom";
    public static final String rowNumTo = "rowNumTo";
    public static final String depDischarge = "depDischarge";
    public static final String depDischarge301 = "depDischarge301";
//      public static final String depDischargeName = "depDischargeName";
    public static final String depAdmission = "depAdmission";
    public static final String depAdmission301 = "depAdmission301";
    public static final String depTreating = "depTreating";
    public static final String depTreating301 = "depTreating301";
    @Transient
    private final Map<String, SearchListAttribute> attributeMap = new LinkedHashMap<>();

    protected SearchListAttributes() {
        /* add(depDischarge, "VIEW_DEPARTMENT", "DEP_DESCRIPT_KEY", Lang.DISCHARGE_DEPARTMENT)
                .setFormat(new SearchListFormatCommon().setDataType(String.class))
                .setSize(130);
         */
 /*  add(depDischarge301, "T_CASE_DEPARTMENT", "DEP_SHORT_NAME", Lang.DISCHARGE_DEPARTMENT_SHORT)
                .setSize(80);
         */

        add(rowNum, "", "", Lang.ROW_NUM)
                .setFormat(new SearchListFormatInteger())
                .setSize(100)
                .setSortable(false)
                .addEqualChild(add(rowNumEqual, getByKey(rowNum), Lang.ROW_NUM))
                .addBetweenChildren(
                        add(rowNumFrom, getByKey(rowNum), Lang.ROW_NUM_FROM),
                        add(rowNumTo, getByKey(rowNum), Lang.ROW_NUM_TO)
                );
        //.setNoFilter(true);        

        add(patDateOfBirth, "T_PATIENT", "PAT_DATE_OF_BIRTH", Lang.DATE_OF_BIRTH) //Geburtsdatum
                //                .setFormat(new SearchListFormatDateTime().setTruncTime(true).setShowTruncTime(false))
                //                .setSize(60);
                .setFormat(new SearchListFormatDateTime())
                .setSize(130)
                .addEqualChild(add(patDateOfBirthEqual, getByKey(patDateOfBirth), Lang.DATE_OF_BIRTH))
                .addBetweenChildren(
                        add(patDateOfBirthFrom, getByKey(patDateOfBirth), Lang.DATE_OF_BIRTH_FROM),
                        add(patDateOfBirthTo, getByKey(patDateOfBirth), Lang.DATE_OF_BIRTH_TO)
                );

        add(patName, "", "", Lang.PATIENT_NAME) //Patientenname
                .setFormat(new SearchListFormatString())
                .setSize(120)
                .addEqualChild(add(patSecondName, "T_PATIENT", "PAT_SEC_NAME", Lang.SECOND_NAME))
                .addEqualChild(add(patFirstName, "T_PATIENT", "PAT_FIRST_NAME", Lang.FIRST_NAME));

        add(insNumber, "T_CASE", "INSURANCE_NUMBER_PATIENT ", Lang.INSURANCE_NUMBER) //Versichertennummer
                .setSize(60);

        add(lock, "$ISNULL(T_LOCK.SINCE)", "LOCK", Lang.ITEM_LOCKED) //Case is locked
                .setFormat(new SearchListFormatBoolean())
                //.setFormat(new SearchListImage())
                //.setNoFilter(true)
                .setSize(30);
        add(depDischarge, "VIEW_DEPARTMENT", "DEP_SHORT_NAME_DISCHARGE", Lang.DISCHARGE_DEPARTMENT)
                .setFormat(new SearchListFormatString())
                .setDepartment(true)
                .setSize(100);
        add(depDischarge301, "VIEW_DEPARTMENT", "DEP_KEY_301_DISCHARGE", Lang.DISCHARGE_DEPARTMENT_301)
                .setFormat(new SearchListFormatString())
                .setDepartment(true)
                .setSize(200);
//        add(depDischargeName, "T_CASE_DEPARTMENT", "DEP_KEY_301", Lang.DISCHARGE_DEPARTMENT_301_NAME) //dischargDepartmentNAme
//                .setFormat(new SearchListFormatString())
//                .setDepartmentName(true)
//                .setSize(200);
        add(depAdmission, "VIEW_DEPARTMENT", "DEP_SHORT_NAME_ADMISSION", Lang.ADMISSION_DEPARTMENT)
                .setFormat(new SearchListFormatString())
                .setDepartment(true)
                .setSize(100);
        add(depAdmission301, "VIEW_DEPARTMENT", "DEP_KEY_301_ADMISSION", Lang.ADMISSION_DEPARTMENT_301)
                .setFormat(new SearchListFormatString())
                .setDepartment(true)
                .setSize(200);
        add(depTreating, "VIEW_DEPARTMENT", "DEP_SHORT_NAME_TREATING", Lang.TREATING_DEPARTMENT)
                .setFormat(new SearchListFormatString())
                .setDepartment(true)
                .setSize(100);
        add(depTreating301, "VIEW_DEPARTMENT", "DEP_KEY_301_TREATING", Lang.TREATING_DEPARTMENT_301)
                .setFormat(new SearchListFormatString())
                .setDepartment(true)
                .setSize(200);
    }

    public Map<String, String> getUniqueDatabaseFields() {
        Map<String, String> uniqueDatabaseFields = new HashMap<>();
        for (Map.Entry<String, SearchListAttribute> entry : attributeMap.entrySet()) {
            uniqueDatabaseFields.put(entry.getKey(), entry.getValue().getUniqueDatabaseField());
        }
        return uniqueDatabaseFields;
    }

    public final SearchListAttribute add(final String pKey, final SearchListAttribute pParentAttribute, final String pLanguageKey) {
        if (pParentAttribute == null) {
            throw new IllegalArgumentException("parent cannot be null for attribute '" + pKey + "'");
        }
        SearchListAttribute attr = add(pKey, pParentAttribute.getDatabaseTable(), pParentAttribute.getDatabaseField(), pLanguageKey);
        attr.setParent(pParentAttribute);
        return attr;
    }

    public final SearchListAttribute add(final String pKey, final String pDatabaseTable, final String pDatabaseField, final String pLanguageKey) {

        SearchListAttribute attribute = new SearchListAttribute(pKey, pDatabaseTable, pDatabaseField, pLanguageKey);
        attributeMap.put(pKey, attribute);
        attribute.setNumber(attributeMap.size());
        return attribute;
    }

    protected abstract void initKeys();

    public SearchListAttribute get(final String pKey) {
        return getByKey(pKey);
    }

    public final SearchListAttribute getByKey(final String pKey) {
        SearchListAttribute attr = null;
        String key = (pKey == null) ? "" : pKey.trim();
        for (Map.Entry<String, SearchListAttribute> entry : attributeMap.entrySet()) {
            //System.out.println(entry.getKey() + "/" + entry.getValue());
            //String key = entry.getKey();
            //if (entry.getKey().getName().equalsIgnoreCase(pKey)) {
            if (entry.getKey().equalsIgnoreCase(key)) {
                attr = entry.getValue();
                break;
            }
        }
        return attr;
    }

    /**
     *
     * @param pField Database Field
     * @return List of WorkingListAttributes
     */
    public List<SearchListAttribute> getByField(final String pField) {
        List<SearchListAttribute> results = new LinkedList<>();
        String field = (pField == null) ? "" : pField.trim();
        for (Map.Entry<String, SearchListAttribute> entry : attributeMap.entrySet()) {
            //System.out.println(entry.getKey() + "/" + entry.getValue());
            //String key = entry.getKey();
            //if (entry.getKey().getName().equalsIgnoreCase(pKey)) {
            if (entry.getValue().getDatabaseField().equalsIgnoreCase(field)) {
                results.add(entry.getValue());
            }
        }
        return results;
    }

    /**
     * There are probably more than one result, but it returns only the first
     * match!
     *
     * @param pTable Database Table
     * @param pField Database Field
     * @return SearchListAttribute
     */
    public SearchListAttribute getByQualifiedField(final String pTable, final String pField) {
        SearchListAttribute attr = null;
        String table = (pTable == null) ? "" : pTable.trim();
        String field = (pField == null) ? "" : pField.trim();
        for (SearchListAttribute wla : getByField(field)) {
            //System.out.println(entry.getKey() + "/" + entry.getValue());
            //String key = entry.getKey();
            //if (entry.getKey().getName().equalsIgnoreCase(pKey)) {
            if (wla.getDatabaseTable().equalsIgnoreCase(table)) {
                attr = wla;
                break;
            }
        }
        return attr;
    }

    protected final void removeAll() {
        attributeMap.clear();
    }

    /**
     *
     * @return Copy of attributes list
     */
    public Map<String, SearchListAttribute> getAll() {
        Map<String, SearchListAttribute> attributeListCopy = new LinkedHashMap<>();
        attributeListCopy.putAll(attributeMap);
        return attributeListCopy;
    }

    public Set<String> getKeys() {
        Set<String> resultList = new HashSet<>();
        for (Map.Entry<String, SearchListAttribute> entry : attributeMap.entrySet()) {
            resultList.add(entry.getKey());
        }
        return resultList;
    }

    public Set<SearchListAttribute> getAttributes() {
        Set<SearchListAttribute> resultList = new HashSet<>();
        for (Map.Entry<String, SearchListAttribute> entry : attributeMap.entrySet()) {
            resultList.add(entry.getValue());
        }
        return resultList;
    }

}
