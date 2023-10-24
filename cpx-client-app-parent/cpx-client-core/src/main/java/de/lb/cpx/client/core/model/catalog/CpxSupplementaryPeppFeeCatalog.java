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
package de.lb.cpx.client.core.model.catalog;

import de.lb.cpx.service.information.CatalogTypeEn;

/**
 *
 * @author Dirk Niemeier
 */
public class CpxSupplementaryPeppFeeCatalog extends AbstractCpxSupplementaryFeeCatalog {

    private static CpxSupplementaryPeppFeeCatalog instance = null;
    public static final CatalogTypeEn CATALOG = CatalogTypeEn.ZP;

    public static synchronized CpxSupplementaryPeppFeeCatalog instance() {
        if (instance == null) {
            instance = new CpxSupplementaryPeppFeeCatalog();
        }
        return instance;
    }
    //Logger log = Logger.getLogger(getClass().getSimpleName());

    private CpxSupplementaryPeppFeeCatalog() {

    }

//    /**
//     * Returns Dummy-Object if Supplementary Fee Key cannot be found in local
//     * SQLite-DB
//     *
//     * @param pCode Supplementary Fee Code
//     * @param pCountryEn Country ("de", "en")
//     * @param pYear Year
//     * @return SupplementaryFee
//     */
//    @Override
//    public CpxSupplementaryFee getByCode(final String pCode, final String pCountryEn, final Integer pYear) {
//        return super.getByCode(CATALOG, pCode, pCountryEn, pYear);
//    }
//    /**
//     * Returns Dummy-Object if Supplementary Fee Key cannot be found in local
//     * SQLite-DB
//     *
//     * @param pId ID
//     * @param pCountryEn Country ("de", "en")
//     * @param pYear Year
//     * @return SupplementaryFee
//     */
//    @Override
//    public CpxSupplementaryFee getById(final Long pId, final String pCountryEn, final Integer pYear) {
//        return getById(CATALOG, pId, pCountryEn, pYear);
//    }
//    /**
//     * Returns Dummy-Object if Supplementary Fee Key cannot be found in local
//     * SQLite-DB
//     *
//     * @param pCode Supplementary Fee Key
//     * @param pCountryEn Country ("de", "en")
//     * @param pYear Year
//     * @return SupplementaryFee
//     */
//    @Override
//    public List<CpxSupplementaryFee> findManyByCode(final String pCode, final String pCountryEn, final Integer pYear) {
//        return super.findManyByCode(CATALOG, pCode, pCountryEn, pYear);
//    }
    @Override
    public CatalogTypeEn getCatalogType() {
        return CATALOG;
    }

}
