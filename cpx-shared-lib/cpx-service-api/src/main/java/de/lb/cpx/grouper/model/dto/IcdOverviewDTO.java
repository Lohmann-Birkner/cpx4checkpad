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

import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.enums.LocalisationEn;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DTO for the overview over an icd, used in the case mangement, to show the
 * icds of a case
 *
 * @author wilde
 */
public class IcdOverviewDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<String, TCaseIcd> icdVersionMap = new HashMap<>();
    private final String icdCode;
    private final List<Long> occurance = new ArrayList<>();
    private final LocalisationEn localisation;

    /**
     * construct new Dto object
     *
     * @param pIcdCode icd code as identifier
     * @param pLoc lokalisation
     * @param pOccurance string of ids where the id occures
     */
    public IcdOverviewDTO(String pIcdCode, LocalisationEn pLoc, String pOccurance) {
        icdCode = pIcdCode;
        localisation = pLoc;
        if (pOccurance == null || pOccurance.isEmpty()) {
            return;
        }
        for (String id : pOccurance.split(",")) {
            occurance.add(Long.valueOf(id));
        }

    }

    /**
     * @return localisation of the icd
     */
    public LocalisationEn getLocalisation() {
        return localisation;
    }
//    public boolean isHd(){
//        return ishd;
//    }

    /**
     * get the origin type for the ui
     *
     * @return origin type
     */
    public List<Long> getOccurance() {
        return occurance;
    }

    /**
     * get the icd code
     *
     * @return icd code
     */
    public String getIcdCode() {
        return icdCode;
    }

    /**
     * gets a icd to map for the version
     *
     * @param pId name of the version
     * @return icd for that version
     */
    public TCaseIcd getIcdForVersion(String pId) {
        return icdVersionMap.get(pId);
    }

    /**
     * gets a icd to map for the version
     *
     * @param pId name of the version
     * @return icd for that version
     */
    public TCaseIcd getIcdForVersion(long pId) {
        return getIcdForVersion(String.valueOf(pId));
    }

    /**
     * sets an icd object with the version
     *
     * @param pVersionName version name
     * @param pIcd icd for that version
     */
    public void addIcdForVersion(String pVersionName, TCaseIcd pIcd) {
        if (!icdVersionMap.containsKey(pVersionName)) {
            icdVersionMap.put(pVersionName, pIcd);
            return;
        }
        icdVersionMap.replace(pVersionName, pIcd);
    }

    /**
     * checks if a icd is already stored for that version
     *
     * @param pVersionName version name to check
     * @return indicator if an icd is already stored for that specific version
     */
    public boolean hasCodeForVersion(String pVersionName) {
        return icdVersionMap.containsKey(pVersionName);
    }

    /**
     * removes an icd and the version
     *
     * @param versionName version name
     * @param icd icd to remove
     */
    public void removeIcdForVersion(String versionName, TCaseIcd icd) {
        icdVersionMap.remove(versionName, icd);
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
    public Collection<TCaseIcd> getIcds() {
        return icdVersionMap.values();
    }

    @Override
    public String toString() {
        return icdCode;
    }

    public boolean hasHbxFl() {
        for (TCaseIcd icd : getIcds()) {
            if (icd.getIcdcIsHdxFl()) {
                return true;
            }
        }
        return false;
    }

    public boolean isOnlyOccuringIn(long pVersionId) {
        if (occurance.contains(pVersionId)) {
            if (occurance.size() == 1) {
                return true;
            }
        }
        return false;
    }
}
