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
package de.lb.cpx.server.commons.dao;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 *
 * @author Dirk Niemeier
 */
@MappedSuperclass
public abstract class AbstractCatalogEntity extends AbstractEntity {

    private static final long serialVersionUID = 1L;
    protected static final String DEFAULT_TEXT = "Keine Angabe";
    private String checksum;
//    private static final DecimalFormat mNumberFormatDrg = new DecimalFormat( "##0.000" ); // cw kann maximal 3 zeichen nach der komma haben
//    private static final  DecimalFormat mNumberFormatPepp = new DecimalFormat( "##0.0000" ); // cw kann maximal 4 zeichen nach der komma haben
//    private static final  DecimalFormat mNumberFormatSuppl = new DecimalFormat( "##0.00" ); // ze oder cp kann maximal 2 zeichen nach der komma haben
//    private static final  SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMdd");
//    private static final  SimpleDateFormat mDateFormatSuppl = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Column(name = "CHECKSUM", nullable = true, length = 64)
    public String getChecksum() {
        return this.checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public static final String getTextRequireNotNull(Object pObject) {
        if (pObject == null) {
            return DEFAULT_TEXT;
        }
        if (pObject instanceof String && ((String) pObject).trim().isEmpty()) {
            return DEFAULT_TEXT;
        }
        return String.valueOf(pObject);
    }

}
