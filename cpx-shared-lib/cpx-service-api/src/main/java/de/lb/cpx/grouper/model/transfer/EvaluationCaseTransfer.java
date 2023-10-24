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
 *    2016  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.grouper.model.transfer;

import de.lb.cpx.grouper.model.enums.GrouperRefFieldsEn;
import static de.lb.cpx.grouper.model.enums.GrouperRefFieldsEn.GPDX_POSITION;
import de.lb.cpx.model.enums.CpxEnumInterface;
import de.lb.cpx.model.enums.GroupResultPdxEn;
import de.lb.cpx.model.enums.GrouperMdcOrSkEn;
import de.lb.cpx.model.enums.GrouperStatusEn;
import de.lb.cpx.system.properties.CpxSystemProperties;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author gerschmann
 */
public class EvaluationCaseTransfer implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(EvaluationCaseTransfer.class.getName());

    private final int[] checkIcdPositions = {0, 2};
    private final int[] checkOpsPositions = {0};

    private String[] parts = null;
    private final boolean isLocal;

    public EvaluationCaseTransfer(String str, boolean isLocal) {
        parts = str.split("\\|");
        this.isLocal = isLocal;
    }

    /**
     * compares Date with its given String representation
     *
     * @param reference String representation of Date in the Reference context
     * @param date Date
     * @return comparison result
     *
     */
    public static boolean checkDate(String reference, Date date) {
        if (reference == null || reference.isEmpty()) {
            return date == null;
        }
        if (date == null) {
            return false;
        }
        DateFormat dfWithTime = new SimpleDateFormat("yyyyMMddHHmm");
        DateFormat dfNoTime = new SimpleDateFormat("yyyyMMdd");

        String dt = "";
        if (reference.length() == 8) {
            dt = dfNoTime.format(date);

        } else if (reference.length() >= 12) {
            dt = dfWithTime.format(date);
        }
        return reference.equals(dt);
    }

    /**
     * converts date to string with format yyyyMMddHHmm, which is used in
     * grouper reference
     *
     * @param date date to convert
     * @return converted date to string
     */
    public static String date2String(Date date) {
        DateFormat dfWithTime = new SimpleDateFormat("yyyyMMddHHmm");
        return dfWithTime.format(date);
    }

    /**
     * compare String values with given reference
     *
     * @param reference reference field, can have Flags seperated with ^sign
     * @param check value to compare
     * @return comparison result
     */
    public static boolean checkStringsWithFlags(String reference, String check) {
        if (reference == null || reference.isEmpty()) {
            return check == null || check.isEmpty();
        }
        String[] ref = reference.split("\\^");
        return ref[0].equalsIgnoreCase(check);
    }

    /**
     * makes the String from Array of strings in reference format
     *
     * @param arr array of strings
     * @return string constracted from array
     *
     */
    public static String getStringFromArray(ArrayList<String> arr) {

        String r = "";
        if (arr == null || arr.isEmpty()) {
            return r;
        }
        r = arr.stream().map((str) -> str + "|").reduce(r, String::concat);
        if (r.endsWith("|")) {
            r = r.substring(0, r.length() - 1);
        }
        return r;
    }

    public String getUniqueKey() {
        if (parts != null && parts.length > GrouperRefFieldsEn.GPDX_POSITION.getId()
                && parts[GrouperRefFieldsEn.KH_POSITION.getId()] != null
                && parts[GrouperRefFieldsEn.CASE_NUMBER_POSITION.getId()] != null) {
            return parts[GrouperRefFieldsEn.KH_POSITION.getId()] + "_" + parts[GrouperRefFieldsEn.CASE_NUMBER_POSITION.getId()];
        }
        return null;
    }

    /**
     * checks the Field with postion
     *
     * @param en GrouperRefFieldsEn
     * @param toCompare - value from Case
     * @return successful?
     */
    public boolean checkField(GrouperRefFieldsEn en, Object toCompare) {
// ignore date of birth, because the value, which is saved in the db was generated from 
// to get the case imported(it is must field bei P21 but not by grouper)
        if (en.equals(GrouperRefFieldsEn.BIRTH_DATE_POSITION)) {
            return true;
        }
        if (parts[en.getId()].isEmpty() && toCompare == null) {
            return true;
        }
        if (toCompare == null) {
            return false;
        }

        Class<?> aClass = toCompare.getClass();

        if (aClass.isEnum()) {
            return checkEnum(en, toCompare);
        }
        int id = en.getId();
        if (aClass.equals(String.class)) {
            return checkStringsWithFlags(parts[id], (String) toCompare);
        }
        if (aClass.equals(Date.class) || aClass.equals(Timestamp.class)) {
            return checkDate(parts[id], (Date) toCompare);
        }

        if (aClass.equals(Integer.class)) {
            if (en.equals(GrouperRefFieldsEn.ADM_WEIGHT_POSITION)
                    && parts[id].equals("2500^00")
                    && (((Number) toCompare).intValue() == 0)) {
                return true;
            }
            return checkIntegerWithFlags(parts[id], (Integer) toCompare);
        }
        if (aClass.equals(Long.class)) {
            Long cmp = (Long) toCompare;
            return checkIntegerWithFlags(parts[id], cmp.intValue());
        }
        return true;
    }

    /**
     * compare int values with given reference
     *
     * @param reference reference field, can have Flags seperated with ^sign
     * @param check value to compare
     * @return comparison result
     */
    private boolean checkIntegerWithFlags(String reference, int check) {
        if (reference == null || reference.isEmpty()) {
            return check == 0;
        }
        String[] ref = reference.split("\\^");
        try {
            return Integer.parseInt(ref[0]) == check;
        } catch (NumberFormatException e) {
            Logger.getLogger(EvaluationCaseTransfer.class.getName()).log(Level.INFO, "error on parsing first part of " + reference + "into integer", e);
            return false;
        }

    }

    /**
     * returns field to fields position
     *
     * @param en fields position
     * @return field
     */
    public String getField(GrouperRefFieldsEn en) {
        if (parts.length > en.getId()) {
            return parts[en.getId()];
        }
        return "";
    }

    /**
     * checks the flag settings for icds
     *
     * @param en Grouper field reference
     * @param toCompare String, which was build from the DB case
     * @return result of the compare action
     * @throws java.io.UnsupportedEncodingException wrong encoding
     */
    public boolean checkMultiField(GrouperRefFieldsEn en, ArrayList<String> toCompare) throws UnsupportedEncodingException {
        if (parts[en.getId()].isEmpty() && !toCompare.isEmpty()) {
            return false;
        }
        if (parts[en.getId()].isEmpty() && toCompare.isEmpty()) {
            return true;
        }
        String[] refParts = parts[en.getId()].split("~");
        if (refParts.length != toCompare.size()) {
            return false;
        }
        ArrayList<String> refs = new ArrayList<>(Arrays.asList(refParts));

        for (String str : toCompare) {
            String[] compParts = str.split("\\^");
            for (int j = 0; j < refs.size(); j++) {
                String ref = refs.get(j);
                if (compareCode(ref.trim(), compParts[0].trim(), en)) {
                    String[] ref1 = ref.split("\\^");
                    if (checkInputFields(en, ref1[0].trim(), compParts[0].trim())) {
                        boolean flagCompare = compareFlags(ref1[1], compParts[1], en);
                        if (flagCompare) {
                            refs.remove(j);
                            break;
                        } else {
                            if (en.equals(GrouperRefFieldsEn.PROCEDURES_POSITION)) {
// the used for grouping flags for the ops which have equals date can be set in the different order            
// check opss with the same date
                                for (int k = j + 1; k < refs.size(); k++) {
                                    ref = refs.get(k);
                                    ref1 = ref.split("\\^");
                                    if (compareOpsDate(ref1[0], compParts[0])) {
                                        flagCompare = compareFlags(ref1[1], compParts[1], en);
                                        if (flagCompare) {
                                            refs.remove(k);
                                            refs.remove(j);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
        return refs.isEmpty();
    }

    private boolean compareCode(String ref, String toCompare, GrouperRefFieldsEn en) {
        String refCode = ref;
        String toCompareCode = toCompare;
        switch (en) {
            case DIAGNOSIS_POSITION:
                refCode = ref.split("\\^")[0];
                toCompareCode = toCompare.split("\\^")[0];
                break;
            case PROCEDURES_POSITION: // procedures, departments and ets codes are separated with '&' from further values

            case DEPARTMENTS_POSITION: // for pepp only

            case ET_POSITION: //for pepp only
                refCode = ref.split("&")[0];
                toCompareCode = toCompare.split("&")[0];
                break;

            default:
                LOG.log(Level.WARNING, "Unknown grouper ref fields enum type: " + en);
        }
        return refCode.trim().equalsIgnoreCase(toCompareCode.trim());
    }

    /**
     * compares the grouping flags with the reference
     *
     * @param ref reference flags
     * @param toCompare flags from db
     * @param positions positions to compare
     * @return true/false
     */
    private boolean compareFlags(String ref, String toCompare, GrouperRefFieldsEn en) throws UnsupportedEncodingException {
        int[] positions = checkOpsPositions;
        if (en.equals(GrouperRefFieldsEn.DIAGNOSIS_POSITION)) {
            positions = checkIcdPositions;
        }
        boolean flagCompare = true;
        if (toCompare.length() == ref.length()) {
            for (int i = 0; i < positions.length; i++) {
                if (toCompare.length() > positions[i]
                        && toCompare.getBytes(CpxSystemProperties.DEFAULT_ENCODING)[positions[i]] != ref.getBytes(CpxSystemProperties.DEFAULT_ENCODING)[positions[i]]) {
                    flagCompare = false;
                }
            }
        }
        return flagCompare;
    }

    /**
     * compares the input fields of different origins
     *
     * @param en field origin
     * @param reference value of the field in reference
     * @param toCompare value of the field in db
     * @return true/false
     */
    private boolean checkInputFields(GrouperRefFieldsEn en, String reference, String toCompare) {

        switch (en) {
            case DIAGNOSIS_POSITION:
                return reference.equalsIgnoreCase(toCompare);
            case PROCEDURES_POSITION:
                return compareOpsInput(reference, toCompare);
            case DEPARTMENTS_POSITION: // for pepp only
                return true;
            case ET_POSITION: //for pepp only
                return true;

            default:
                LOG.log(Level.WARNING, "Unknown grouper ref fields enum type: " + en);
        }
        return true;
    }

    /**
     * compares the input field for ops. It consists of 3 subfields
     *
     * @param reference value of this field in reference
     * @param toCompare value in db
     * @return
     */
    private boolean compareOpsInput(String reference, String toCompare) {
        String[] refs = reference.split("&");
        String[] comps = toCompare.split("&");
        if (refs.length != comps.length) {
            return false;
        }
        if (refs.length != 3) {
            return false;
        }
        if (!StringUtils.equalsIgnoreCase(refs[0], comps[0])) {//ops code
            return false;
        }
        if (!StringUtils.equalsIgnoreCase(refs[1], comps[1])) {// localisation
            return false;
        }
        if (refs[2].length() == comps[2].length()) { //datum
            return StringUtils.equals(refs[2], comps[2]);
        }
        // date from db if not null has 8 syms
        if (refs[2].length() < comps[2].length()) {
            return StringUtils.startsWith(comps[2], refs[2]);
        }
        return false;
    }

    public boolean isIsLocal() {
        return isLocal;
    }

    /**
     * compares ops date
     *
     * @param reference OPS from reference
     * @param toCompare ops from db
     * @return
     */
    private boolean compareOpsDate(String reference, String toCompare) {
        String[] refs = reference.split("&");
        String[] comps = toCompare.split("&");
        if (refs.length != comps.length) {
            return false;
        }
        if (refs.length != 3) {
            return false;
        }
        if (refs[2].length() == comps[2].length()) { //datum
            return StringUtils.equals(refs[2], comps[2]);
        }
        // date from db if not null has 8 syms
        if (refs[2].length() < comps[2].length()) {
            return StringUtils.startsWith(comps[2], refs[2]);
        }
        return false;
    }

    /**
     * compares the enum values with reference
     *
     * @param id
     * @param toCompare
     * @return
     */
    private boolean checkEnum(GrouperRefFieldsEn en, Object toCompare) {
        CpxEnumInterface<?> comp = (CpxEnumInterface<?>) toCompare;
        String ref = parts[en.getId()];
        switch (en) {
            case GPDX_POSITION:
                if (comp instanceof GroupResultPdxEn) {
                    return checkIntegerWithFlags(ref, ((GroupResultPdxEn) comp).getId());
                }
                break;
            case GST_POSITION:
                if (comp instanceof GrouperStatusEn) {
                    return checkStringsWithFlags(ref, ((GrouperStatusEn) comp).getId());
                }
                break;
            case MDC_POSITION:
                if (comp instanceof GrouperMdcOrSkEn) {
                    return checkStringsWithFlags(ref, ((GrouperMdcOrSkEn) comp).getId());
                }
                break;
            default:
                LOG.log(Level.WARNING, "Unknown grouper ref fields enum type: " + en);
        }
        return true;
    }

}
