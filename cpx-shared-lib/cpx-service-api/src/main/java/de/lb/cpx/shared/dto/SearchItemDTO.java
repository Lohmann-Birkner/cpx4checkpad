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
package de.lb.cpx.shared.dto;

import de.lb.cpx.shared.filter.enums.SearchListAttributes;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Dirk Niemeier
 */
public abstract class SearchItemDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    public long id;
    private Date patDateOfBirth; //Geburtsdatum
    private String patName;
    private String insNumber;
    private transient Method[] dtoMethods;
    private Date lockSince;
    private Date lockExpires;
    private String lockUserName;
    private boolean lock;
    private long rowNum;
    private String depDischarge;
    private String depDischarge301;
    //    private String depDischargeName;
    private String depAdmission;
    private String depAdmission301;
    private String depTreating;
    private String depTreating301;

    public void checkAttributes(final SearchListAttributes pAttributes) {
        Field[] dtoFields = this.getClass().getDeclaredFields();
        dtoMethods = this.getClass().getDeclaredMethods();
        Field[] wlaFields = pAttributes.getClass().getDeclaredFields();

        List<String> dtoFieldNames = new ArrayList<>();
        List<String> dtoMethodNames = new ArrayList<>();
        List<String> wlaFieldNames = new ArrayList<>();
        Set<String> wlaAttributeNames = pAttributes.getKeys();

        for (Field field : dtoFields) {
            String fieldName = field.getName();
            if (fieldName.equalsIgnoreCase("serialVersionUID") || fieldName.equalsIgnoreCase("LOG")) {
                continue;
            }
            dtoFieldNames.add(fieldName);
        }

        for (Method method : dtoMethods) {
            String methodName = method.getName();
            dtoMethodNames.add(methodName);
        }

        for (Field field : wlaFields) {
            if (field.getName().equals("DEFAULT_COLUMNS") || field.getName().equals("RULE_COLUMNS") || field.getName().equals("DEADLINES") || field.getName().equals("DEADLINES2")) {
                continue;
            }
            if (field.getName().equalsIgnoreCase("serialVersionUID") || field.getName().equalsIgnoreCase("LOG")) {
                continue;
            }
            if (Modifier.isStatic(field.getModifiers())
                    && Modifier.isFinal(field.getModifiers())) {
                String fieldName = field.getName();
                wlaFieldNames.add(fieldName);
            }
        }

        List<String> errors = new ArrayList<>();

        /*
    for (String dtoFieldName: dtoFieldNames) {
      if (wlaFieldNames.contains(dtoFieldName)) {
        //Enumeration in WorkingListAttributes exists, that's fine!
      } else {
        //throw new NoSuchFieldException("Cannot find corresponding enumeration in " + WorkingListAttributes.class.getSimpleName() + " for field " + this.getClass().getSimpleName() + "." + dtoFieldName); 
        errors.add("Cannot find corresponding enumeration in " + WorkingListAttributes.class.getSimpleName() + " for field " + this.getClass().getSimpleName() + "." + dtoFieldName);
      }
    }
         */
        for (String dtoFieldName : dtoFieldNames) {
            String setterMethod = "set" + Character.toUpperCase(dtoFieldName.charAt(0)) + dtoFieldName.substring(1);
            String getterMethod = "get" + Character.toUpperCase(dtoFieldName.charAt(0)) + dtoFieldName.substring(1);
            if (dtoMethodNames.contains(setterMethod)) {
                //Setter-Method exists, that's finde!
            } else {
                //throw new NoSuchFieldException("Cannot find corresponding enumeration in " + WorkingListAttributes.class.getSimpleName() + " for field " + this.getClass().getSimpleName() + "." + dtoFieldName); 
                errors.add("Cannot find corresponding setter method '" + setterMethod + "' in " + this.getClass().getSimpleName() + " for field " + this.getClass().getSimpleName() + "." + dtoFieldName);
            }
            if (dtoMethodNames.contains(getterMethod)) {
                //Getter-Method exists, that's finde!
            } else {
                //throw new NoSuchFieldException("Cannot find corresponding enumeration in " + WorkingListAttributes.class.getSimpleName() + " for field " + this.getClass().getSimpleName() + "." + dtoFieldName); 
                errors.add("Cannot find corresponding getter method '" + getterMethod + "' in " + this.getClass().getSimpleName() + " for field " + this.getClass().getSimpleName() + "." + dtoFieldName);
            }
        }

        for (String wlaFieldName : wlaFieldNames) {
            /*
      if (dtoFieldNames.contains(wlaFieldName)) {
        //Enumeration in WorkingListAttributes exists, that's fine!
      } else {
        //throw new NoSuchFieldException("Cannot find corresponding field in " + this.getClass().getSimpleName() + " for enumeration " + WorkingListAttributes.class.getSimpleName() + "." + wlaFieldName); 
        errors.add("Cannot find corresponding field in " + this.getClass().getSimpleName() + " for enumeration " + WorkingListAttributes.class.getSimpleName() + "." + wlaFieldName);
      }
             */
            if (wlaAttributeNames.contains(wlaFieldName)) {
                //Enumeration in WorkingListAttributes exists, that's fine!
            } else {
                //throw new NoSuchFieldException("Cannot find corresponding field in " + this.getClass().getSimpleName() + " for enumeration " + WorkingListAttributes.class.getSimpleName() + "." + wlaFieldName); 
                errors.add("Cannot find corresponding attribute name in " + this.getClass().getSimpleName() + " for field " + this.getClass().getSimpleName() + "." + wlaFieldName + " (attribute name and attribute value are unequal!)");
            }
        }

        if (!errors.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String error : errors) {
                if (sb.length() > 0) {
                    sb.append("\r\n");
                }
                sb.append(error);
            }
            throw new IllegalStateException(sb.toString());
        }

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public abstract String getHospitalIdent();

    public abstract String getCaseNumber();

    public abstract String getPatNumber();

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        if (this.getClass() != obj.getClass()) {
            return false;
        }

        SearchItemDTO that = (SearchItemDTO) obj;

        return that.id == this.id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    /**
     * @return the patDateOfBirth
     */
    public Date getPatDateOfBirth() {
        return patDateOfBirth == null ? null : new Date(patDateOfBirth.getTime());
    }

    /**
     * @param patDateOfBirth the patDateOfBirth to set
     */
    public void setPatDateOfBirth(Date patDateOfBirth) {
        this.patDateOfBirth = patDateOfBirth == null ? null : new Date(patDateOfBirth.getTime());
    }

    /**
     * @param patName the patName to set
     */
    public void setPatName(String patName) {
        this.patName = patName;
    }

    public String getPatName() {
        return this.patName;
    }

    /**
     * @return the insNumber
     */
    public String getInsNumber() {
        return insNumber;
    }

    /**
     * @param insNumber the insNumber to set
     */
    public void setInsNumber(String insNumber) {
        this.insNumber = insNumber;
    }

    /**
     * @return the lockSince
     */
    public Date getLockSince() {
        return lockSince == null ? null : new Date(lockSince.getTime());
    }

    /**
     * @param lockSince the lockSince to set
     */
    public void setLockSince(final Date lockSince) {
        this.lockSince = lockSince == null ? null : new Date(lockSince.getTime());
    }

    /**
     * @return the lockExpires
     */
    public Date getLockExpires() {
        return lockExpires == null ? null : new Date(lockExpires.getTime());
    }

    /**
     * @param lockExpires the lockExpires to set
     */
    public void setLockExpires(final Date lockExpires) {
        this.lockExpires = lockExpires == null ? null : new Date(lockExpires.getTime());
    }

    /**
     * @return the lockUserName
     */
    public String getLockUserName() {
        return lockUserName;
    }

    /**
     * @param lockUserName the lockUserName to set
     */
    public void setLockUserName(final String lockUserName) {
        this.lockUserName = lockUserName;
    }

    /**
     * @return the lock
     */
    public boolean getLock() {
        return lock;
    }

    /**
     * @param lock the lock to set
     */
    public void setLock(boolean lock) {
        this.lock = lock;
    }

    /**
     * @return the rowNum
     */
    public long getRowNum() {
        return rowNum;
    }

    /**
     * @param rowNum the rowNum to set
     */
    public void setRowNum(long rowNum) {
        this.rowNum = rowNum;
    }

//    public static void addParamValues(Map<String, String> map, String key, String value) {
//        if (value == null) {
//            map.put(key, "");
//        } else {
//            map.put(key, value);
//        }
//    }
//
//    public static void addParamValues(Map<String, String> map, String key, Date value) {
//        if (value == null) {
//            map.put(key, "");
//        } else {
//            map.put(key, UtlDateTimeConverter.converter().formatToGermanDate(value, false));
//        }
//    }
//
//    public void addParamValues(Map<String, String> map, String key, Number value) {
//        if (value == null) {
//            map.put(key, "");
//        } else {
//            map.put(key, String.valueOf(value));
//        }
//    }
//
//    public static void addParamValues(Map<String, String> map, String key, Enum<?> value) {
//        if (value == null) {
//            map.put(key, "");
//        } else {
//            map.put(key, String.valueOf(value));
//        }
//    }
//
//    public static void addParamValues(Map<String, String> map, String key, Boolean value) {
//        if (value == null) {
//            map.put(key, "");
//        } else if (value) {
//            map.put(key, "Ja");
//        } else {
//            map.put(key, "Nein");
//        }
//    }
    /**
     *
     * @return depDischarge
     */
    public String getDepDischarge() {
        return depDischarge;
    }

    /**
     * @param depDischarge the DischargDepartment to set
     */
    public void setDepDischarge(String depDischarge) {
        this.depDischarge = depDischarge;
    }

    /**
     *
     * @return depDischarge301
     */
    public String getDepDischarge301() {
        return depDischarge301;
    }

    /**
     * @param depDischarge301 the depDischarge301 to set
     */
    public void setDepDischarge301(String depDischarge301) {
        this.depDischarge301 = depDischarge301;
    }

    /**
     *
     * @return depAdmission
     */
    public String getDepAdmission() {
        return depAdmission;
    }

    /**
     *
     * @return depAdmission301
     */
    public String getDepAdmission301() {
        return depAdmission301;
    }

    /**
     *
     * @param depAdmission the depAdmission to set
     */
    public void setDepAdmission(String depAdmission) {
        this.depAdmission = depAdmission;
    }

    /**
     *
     * @param depAdmission301 the depAdmission301 to set
     */
    public void setDepAdmission301(String depAdmission301) {
        this.depAdmission301 = depAdmission301;
    }

    /**
     *
     * @return depTreating
     */
    public String getDepTreating() {
        return depTreating;
    }

    /**
     *
     * @return depTreating301
     */
    public String getDepTreating301() {
        return depTreating301;
    }

    /**
     *
     * @param depTreating the depTreating to set
     */
    public void setDepTreating(String depTreating) {
        this.depTreating = depTreating;
    }

    /**
     *
     * @param depTreating301 the depTreating301 to set
     */
    public void setDepTreating301(String depTreating301) {
        this.depTreating301 = depTreating301;
    }

}
