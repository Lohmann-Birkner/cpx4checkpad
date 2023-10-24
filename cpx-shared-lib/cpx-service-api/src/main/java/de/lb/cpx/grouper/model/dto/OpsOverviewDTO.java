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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.grouper.model.dto;

import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.model.enums.LocalisationEn;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DTO to store and fetch ops data for the case mangement view
 *
 * @author wilde
 */
public class OpsOverviewDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<String, TCaseOps> opsVersionMap = new HashMap<>();
    private final String opsCode;
    private LocalisationEn localisation;
    private Date date;
    private final List<Long> occurance = new ArrayList<>();

    /**
     * construct new Dto object
     *
     * @param pOpsCode ops code as identifier
     * @param pOccurance string of ids where the id occures
     */
    public OpsOverviewDTO(String pOpsCode, String pOccurance) {

        opsCode = pOpsCode;
        if (pOccurance == null || pOccurance.isEmpty()) {
            return;
        }
        for (String id : pOccurance.split(",")) {
            occurance.add(Long.valueOf(id));
        }

    }

    public OpsOverviewDTO(String pOpsCode, LocalisationEn pLoc, Date pDate, String pOccurance) {

        opsCode = pOpsCode;
        date = pDate == null ? null : new Date(pDate.getTime());
        localisation = pLoc;
        if (pOccurance == null || pOccurance.isEmpty()) {
            return;
        }
        for (String id : pOccurance.split(",")) {
            occurance.add(Long.valueOf(id));
        }

    }

    public LocalisationEn getLocalisation() {
        return localisation;
    }

    public Date getDate() {
        return date == null ? null : new Date(date.getTime());
    }

    /**
     * get the origin type for the ui
     *
     * @return origin type
     */
    public List<Long> getOccurance() {
        return occurance;
    }

    /**
     * get the ops code
     *
     * @return ops code
     */
    public String getOpsCode() {
        return opsCode;
    }

    /**
     * gets a ops to map for the version
     *
     * @param pVersionName name of the version
     * @return ops for that version
     */
    public TCaseOps getOpsForVersion(String pVersionName) {
        return opsVersionMap.get(pVersionName);
    }

    /**
     * sets an ops object with the version
     *
     * @param pVersionName version name
     * @param pIcd icd for that version
     */
    public void addOpsForVersion(String pVersionName, TCaseOps pIcd) {
        if (!opsVersionMap.containsKey(pVersionName)) {
            opsVersionMap.put(pVersionName, pIcd);
            return;
        }
        opsVersionMap.replace(pVersionName, pIcd);
    }

    /**
     * checks if a icd is already stored for that version
     *
     * @param pVersionName version name to check
     * @return indicator if an icd is already stored for that specific version
     */
    public boolean hasCodeForVersion(String pVersionName) {
        return opsVersionMap.containsKey(pVersionName);
    }

    /**
     * removes an ops and the version
     *
     * @param versionName version name
     * @param pOps ops to remove
     */
    public void removeOpsForVersion(String versionName, TCaseOps pOps) {
        opsVersionMap.remove(versionName, pOps);
    }

    /**
     * return of version id occures in the item
     *
     * @param pVersionId id of the version
     * @return is occuring
     */
    public boolean isOccuringIn(long pVersionId) {
        return occurance.contains(pVersionId);
    }

    /**
     * get the collection of values in the dto object
     *
     * @return collection of objects
     */
    public Collection<TCaseOps> getOps() {
        return opsVersionMap.values();
    }

    /**
     * @param pVersionId version id
     * @return if item is only occuring in one instance
     */
    public boolean isOnlyOccuringIn(long pVersionId) {
        if (occurance.contains(pVersionId)) {
            if (occurance.size() == 1) {
                return true;
            }
        }
        return false;
    }    
}
