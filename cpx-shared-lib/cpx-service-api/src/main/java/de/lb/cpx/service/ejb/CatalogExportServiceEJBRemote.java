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
 *    2018 Anna Gerschmann - interface for Export of the DRG/PEPP/Supplementary fees Catalogs into drgm files in WD_SERVER\catalog
 */
package de.lb.cpx.service.ejb;

import de.lb.cpx.service.information.CatalogTypeEn;
import javax.ejb.Remote;

/**
 *
 * @author gerschmann
 */
@Remote
public interface CatalogExportServiceEJBRemote {

    /**
     * exports catalog information from the database into drgm file in the
     * WD_SERVER\catalog directory
     *
     * @param catalogType DRG, PEPP, ZE, ZP, ET
     * @param pYear catalog year
     * @param pCountryEn now Germany(de)
     * @return true/false on succes, failure
     */
    boolean exportCatalog(final CatalogTypeEn catalogType, final int pYear, final String pCountryEn);

    /**
     * cecks whether the drgm file for catalogType ande year exists
     *
     * @param catalogType DRG, PEPP, ZE, ZP, ET
     * @param pYear catalog year
     * @return true/false on succes, failure
     */
    boolean checkDrgmFileExists(final CatalogTypeEn catalogType, final int pYear);
}
